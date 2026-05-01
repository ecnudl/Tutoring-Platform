#!/bin/bash
# 591 家教站 admin 权限/路由健康检查
# 用法: bash check-admin-perms.sh
#
# 检查三件事:
#   1. 前端调用了, 但后端没实现 → "死链" (前端要删或后端要补)
#   2. 前端调用了, 但 sys_menu apis 白名单没包含 → "权限缺失" (admin 会报"没此权限")
#   3. sys_menu apis 包含了, 但前端没调 → "白名单冗余" (信息性, 一般无害)
#
# 出现 1 或 2 就退出 1, CI 应当 fail.

set -u
HERE=$(cd "$(dirname "$0")" && pwd)
ADMIN_DIR=${ADMIN_DIR:-/home/ubuntu/roncoo-education-admin}
SERVER_DIR=${SERVER_DIR:-/home/ubuntu/roncoo-education}
DB_USER=${DB_USER:-root}
DB_PASS=${DB_PASS:-RonCoo.123}
DB_NAME=${DB_NAME:-roncoo_education}
DB_DOCKER=${DB_DOCKER:-tutor-mysql}

TMP=$(mktemp -d)
trap "rm -rf $TMP" EXIT

# ==== Step 1: 前端调用 ====
grep -rhoE '/(user|system)/admin/[a-z0-9/_-]+' \
  "$ADMIN_DIR/src/views" "$ADMIN_DIR/src/components" 2>/dev/null \
  | sort -u \
  | grep -v '/login/'   `# /login/* 是 EXCLUDE_TOKEN_URL, 不需要白名单` \
  > "$TMP/frontend.txt"

# ==== Step 2: 后端实际实现的路由 ====
# 解析 java 里的 @RequestMapping("/...") (类级) 跟 @PostMapping/@GetMapping/@PutMapping/@DeleteMapping (方法级) 拼起来
python3 - <<PY > "$TMP/backend.txt"
import re, os, sys
roots = ['$SERVER_DIR/roncoo-education-service']
RE_CLASS = re.compile(r'@RequestMapping\s*\(\s*(?:value\s*=\s*)?"([^"]+)"')
RE_METHOD = re.compile(r'@(Post|Get|Put|Delete|Request)Mapping\s*\(\s*(?:value\s*=\s*)?"([^"]+)"')
out = set()
for root in roots:
    for dp, dn, fn in os.walk(root):
        if 'target' in dp.split(os.sep): continue
        for f in fn:
            if not f.endswith('.java'): continue
            if 'Controller' not in f: continue
            with open(os.path.join(dp, f), encoding='utf-8') as fh:
                text = fh.read()
            classes = RE_CLASS.findall(text)
            klass_prefix = classes[0].rstrip('/') if classes else ''
            for verb, path in RE_METHOD.findall(text):
                full = (klass_prefix + '/' + path.lstrip('/')).replace('//', '/')
                if '/admin/' in full or '/auth/' in full or '/api/' in full:
                    out.add(full)
for p in sorted(out):
    print(p)
PY

# ==== Step 3: sys_menu apis 白名单 (超管 role_id=1 看到的所有 apis) ====
docker exec "$DB_DOCKER" mysql -u"$DB_USER" -p"$DB_PASS" "$DB_NAME" \
  -N -e "SELECT apis FROM sys_menu m JOIN sys_menu_role mr ON mr.menu_id=m.id WHERE mr.role_id=1 AND m.apis IS NOT NULL AND m.apis != '';" 2>/dev/null \
  | tr ',' '\n' | sort -u > "$TMP/whitelist.txt"

# ==== Step 4: 对比 ====
echo "=========================================="
echo " admin 权限/路由健康检查"
echo "=========================================="
echo "前端调用的 admin 接口数: $(wc -l < $TMP/frontend.txt)"
echo "后端实现的 admin 接口数: $(grep -c '/admin/' $TMP/backend.txt || echo 0)"
echo "sys_menu apis 白名单接口数: $(wc -l < $TMP/whitelist.txt)"
echo

EXIT=0

echo "--- ❌ 死链: 前端调用, 后端不存在 (前端要删或后端要补) ---"
DEAD=$(comm -23 "$TMP/frontend.txt" "$TMP/backend.txt")
if [ -z "$DEAD" ]; then
  echo "  (无)"
else
  echo "$DEAD" | sed 's/^/  /'
  EXIT=1
fi
echo

echo "--- ❌ 权限缺失: 前端调用, sys_menu apis 不包含 (admin 会"没此权限") ---"
MISSING=$(comm -23 "$TMP/frontend.txt" "$TMP/whitelist.txt")
if [ -z "$MISSING" ]; then
  echo "  (无)"
else
  echo "$MISSING" | sed 's/^/  /'
  EXIT=1
fi
echo

echo "--- ⚠ 白名单冗余: apis 含但前端没调 (无害, 信息性) ---"
EXTRA=$(comm -13 "$TMP/frontend.txt" "$TMP/whitelist.txt")
if [ -z "$EXTRA" ]; then
  echo "  (无)"
else
  echo "$EXTRA" | head -20 | sed 's/^/  /'
fi
echo

if [ "$EXIT" = "1" ]; then
  echo "💥 检查未通过, 修完上面 ❌ 部分再部署."
else
  echo "✅ 检查通过, 可以部署."
fi
exit $EXIT

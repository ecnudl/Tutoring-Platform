#!/bin/bash
# ============================================================
# MinIO 初始化脚本 - 创建 bucket 并设置公共读
# 用法: ./scripts/minio-init.sh
# 需要: mc (MinIO Client) 或 curl
# ============================================================

set -e

MINIO_EP="${MINIO_ENDPOINT:-http://127.0.0.1:9000}"
MINIO_AK="${MINIO_ACCESS_KEY:-minioadmin}"
MINIO_SK="${MINIO_SECRET_KEY:-minioadmin}"
MINIO_BK="${MINIO_BUCKET:-education}"

echo "==> 等待 MinIO 就绪..."
for i in $(seq 1 20); do
    if curl -s "${MINIO_EP}/minio/health/live" > /dev/null 2>&1; then
        echo "    MinIO 已就绪"
        break
    fi
    echo "    等待中... ($i/20)"
    sleep 3
done

# 使用 mc 命令 (如果有)
if command -v mc > /dev/null 2>&1; then
    echo "==> 使用 mc 初始化..."
    mc alias set local "$MINIO_EP" "$MINIO_AK" "$MINIO_SK" 2>/dev/null || true
    mc mb "local/$MINIO_BK" 2>/dev/null || echo "    Bucket 已存在"
    mc anonymous set download "local/$MINIO_BK" 2>/dev/null || true
    echo "    完成"
else
    echo "==> 使用 curl 创建 bucket..."
    # 简单的 S3 兼容创建 bucket
    DATE=$(date -R)
    curl -s -X PUT "${MINIO_EP}/${MINIO_BK}" \
        -u "${MINIO_AK}:${MINIO_SK}" \
        > /dev/null 2>&1 || echo "    Bucket 可能已存在"
    echo "    请打开 MinIO 控制台 http://127.0.0.1:9001 手动设置 bucket 为 public"
fi

echo ""
echo "============================================================"
echo " MinIO 初始化完成!"
echo " API:     $MINIO_EP"
echo " 控制台:  http://127.0.0.1:9001"
echo " 账号:    $MINIO_AK / $MINIO_SK"
echo " Bucket:  $MINIO_BK"
echo "============================================================"

#!/bin/bash
# ============================================================
# Nacos 配置初始化脚本
# 用法: ./scripts/nacos-config-init.sh
# 前置条件: Nacos 已启动, 已安装 curl
# ============================================================

set -e

# 从 .env 加载或使用默认值
NACOS_ADDR="${NACOS_HOST:-127.0.0.1}:${NACOS_PORT:-8848}"
NACOS_USER="${NACOS_USERNAME:-nacos}"
NACOS_PASS="${NACOS_PASSWORD:-nacos}"
NAMESPACE="${NACOS_NAMESPACE:-dev}"

MYSQL_H="${MYSQL_HOST:-127.0.0.1}"
MYSQL_P="${MYSQL_PORT:-3306}"
MYSQL_DB="${MYSQL_DATABASE:-roncoo_education}"
MYSQL_U="${MYSQL_USER:-root}"
MYSQL_PW="${MYSQL_PASSWORD:-RonCoo.123}"

REDIS_H="${REDIS_HOST:-127.0.0.1}"
REDIS_P="${REDIS_PORT:-6379}"
REDIS_PW="${REDIS_PASSWORD:-}"

JWT_SECRET="${JWT_TOKEN_SECRET:-tutor-platform-dev-secret-key}"
JASYPT_PW="${JASYPT_PASSWORD:-roncoo_education}"

MINIO_EP="${MINIO_ENDPOINT:-http://127.0.0.1:9000}"
MINIO_AK="${MINIO_ACCESS_KEY:-minioadmin}"
MINIO_SK="${MINIO_SECRET_KEY:-minioadmin}"
MINIO_BK="${MINIO_BUCKET:-education}"
MINIO_DM="${MINIO_DOMAIN:-http://127.0.0.1:9000/education/}"

echo "==> Nacos 地址: $NACOS_ADDR"
echo "==> 目标命名空间: $NAMESPACE"

# 等待 Nacos 就绪
echo "==> 等待 Nacos 就绪..."
for i in $(seq 1 30); do
    if curl -s "http://$NACOS_ADDR/nacos/" > /dev/null 2>&1; then
        echo "    Nacos 已就绪"
        break
    fi
    echo "    等待中... ($i/30)"
    sleep 3
done

# 1. 创建 dev 命名空间
echo "==> 创建 dev 命名空间..."
curl -s -X POST "http://$NACOS_ADDR/nacos/v1/console/namespaces" \
    -d "customNamespaceId=$NAMESPACE&namespaceName=dev&namespaceDesc=开发环境" \
    > /dev/null 2>&1 || true
echo "    完成"

# 发布配置的函数
publish_config() {
    local DATA_ID=$1
    local CONTENT=$2
    echo "==> 发布配置: $DATA_ID"
    HTTP_CODE=$(curl -s -o /dev/null -w "%{http_code}" -X POST \
        "http://$NACOS_ADDR/nacos/v1/cs/configs" \
        --data-urlencode "tenant=$NAMESPACE" \
        --data-urlencode "dataId=$DATA_ID" \
        --data-urlencode "group=DEFAULT_GROUP" \
        --data-urlencode "type=properties" \
        --data-urlencode "content=$CONTENT")
    if [ "$HTTP_CODE" = "200" ]; then
        echo "    成功"
    else
        echo "    失败 (HTTP $HTTP_CODE)"
    fi
}

# 2. application.properties (所有服务共享)
APPLICATION_PROPS="# ---- 数据源 (Druid) ----
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver
spring.datasource.url=jdbc:mysql://${MYSQL_H}:${MYSQL_P}/${MYSQL_DB}?useUnicode=true&characterEncoding=UTF-8&serverTimezone=Asia/Shanghai&useSSL=false&allowPublicKeyRetrieval=true
spring.datasource.username=${MYSQL_U}
spring.datasource.password=${MYSQL_PW}
spring.datasource.type=com.alibaba.druid.pool.DruidDataSource
spring.datasource.druid.initial-size=5
spring.datasource.druid.min-idle=5
spring.datasource.druid.max-active=20

# ---- Redis ----
spring.data.redis.host=${REDIS_H}
spring.data.redis.port=${REDIS_P}
spring.data.redis.password=${REDIS_PW}
spring.data.redis.database=0
spring.data.redis.timeout=3000ms
spring.cache.redis.time-to-live=60000

# ---- MyBatis ----
mybatis.mapper-locations=classpath:mybatis/*.xml

# ---- JWT ----
jwt.token.secret=${JWT_SECRET}

# ---- Jasypt ----
jasypt.encryptor.password=${JASYPT_PW}
jasypt.encryptor.algorithm=PBEWithMD5AndDES
jasypt.encryptor.iv-generator-classname=org.jasypt.iv.NoIvGenerator

# ---- Swagger ----
swagger.title=家教在线平台
swagger.description=家教在线平台 API 文档
swagger.enable=true

# ---- 日志 ----
logging.level.com.roncoo.education=DEBUG"

publish_config "application.properties" "$APPLICATION_PROPS"

# 3. gateway.properties
GATEWAY_PROPS="# ---- Gateway 路由 ----
spring.cloud.gateway.routes[0].id=service-system
spring.cloud.gateway.routes[0].uri=lb://service-system
spring.cloud.gateway.routes[0].predicates[0]=Path=/system/**

spring.cloud.gateway.routes[1].id=service-user
spring.cloud.gateway.routes[1].uri=lb://service-user
spring.cloud.gateway.routes[1].predicates[0]=Path=/user/**

spring.cloud.gateway.routes[2].id=service-course
spring.cloud.gateway.routes[2].uri=lb://service-course
spring.cloud.gateway.routes[2].predicates[0]=Path=/course/**

# ---- Gateway Redis (token 校验) ----
spring.data.redis.host=${REDIS_H}
spring.data.redis.port=${REDIS_P}
spring.data.redis.password=${REDIS_PW}

# ---- JWT ----
jwt.token.secret=${JWT_SECRET}

# ---- CORS ----
spring.cloud.gateway.globalcors.corsConfigurations.[/**].allowedOriginPatterns=*
spring.cloud.gateway.globalcors.corsConfigurations.[/**].allowedMethods=*
spring.cloud.gateway.globalcors.corsConfigurations.[/**].allowedHeaders=*
spring.cloud.gateway.globalcors.corsConfigurations.[/**].allowCredentials=true

# ---- Knife4j ----
knife4j.gateway.enabled=true
knife4j.gateway.strategy=discover
knife4j.gateway.discover.enabled=true"

publish_config "gateway.properties" "$GATEWAY_PROPS"

# 4. service-user.properties
SERVICE_USER_PROPS="# ---- service-user 配置 ----
# XXL-JOB (可选, 启动时如果连不上会报警但不影响主流程)
xxl.job.admin.addresses=${XXL_JOB_ADMIN_ADDRESSES:-http://127.0.0.1:8088/xxl-job-admin}
xxl.job.accessToken=${XXL_JOB_ACCESS_TOKEN:-roncoo_education}
xxl.job.executor.appname=service-user
xxl.job.executor.address=
xxl.job.executor.ip=
xxl.job.executor.port=${XXL_JOB_EXECUTOR_PORT:-9901}
xxl.job.executor.logpath=./logs/xxl-job
xxl.job.executor.logretentiondays=3"

publish_config "service-user.properties" "$SERVICE_USER_PROPS"

# 5. service-system.properties
SERVICE_SYSTEM_PROPS="# ---- service-system 配置 ----
xxl.job.admin.addresses=${XXL_JOB_ADMIN_ADDRESSES:-http://127.0.0.1:8088/xxl-job-admin}
xxl.job.accessToken=${XXL_JOB_ACCESS_TOKEN:-roncoo_education}
xxl.job.executor.appname=service-system
xxl.job.executor.address=
xxl.job.executor.ip=
xxl.job.executor.port=9902
xxl.job.executor.logpath=./logs/xxl-job
xxl.job.executor.logretentiondays=3"

publish_config "service-system.properties" "$SERVICE_SYSTEM_PROPS"

# 6. service-course.properties
SERVICE_COURSE_PROPS="# ---- service-course 配置 ----
xxl.job.admin.addresses=${XXL_JOB_ADMIN_ADDRESSES:-http://127.0.0.1:8088/xxl-job-admin}
xxl.job.accessToken=${XXL_JOB_ACCESS_TOKEN:-roncoo_education}
xxl.job.executor.appname=service-course
xxl.job.executor.address=
xxl.job.executor.ip=
xxl.job.executor.port=9903
xxl.job.executor.logpath=./logs/xxl-job
xxl.job.executor.logretentiondays=3"

publish_config "service-course.properties" "$SERVICE_COURSE_PROPS"

echo ""
echo "============================================================"
echo " Nacos 配置初始化完成!"
echo " 控制台: http://$NACOS_ADDR/nacos/"
echo " 账号/密码: $NACOS_USER / $NACOS_PASS"
echo " 命名空间: $NAMESPACE"
echo "============================================================"

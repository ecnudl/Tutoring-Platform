# nginx 站点配置模板 — 迁移到新服务器时用 envsubst 渲染
# -------------------------------------------------------
# 变量:
#   $BASE_DOMAIN_1      主域名 1，例如 591jiajiao.com
#   $BASE_DOMAIN_2      主域名 2，例如 591jiajiao.cn（如只有 1 个域名可省略对应 server 块）
#   $SSL_CERT_DIR       证书目录（默认 /etc/nginx/ssl）
#   $ADMIN_DIST_PATH    admin 构建产物路径，例如 /home/ubuntu/roncoo-education-admin/dist
#   $GATEWAY_PORT       后端网关端口，默认 7700
#   $NUXT_PORT          Nuxt 端口，默认 3000
#   $WECOM_PORT         企微机器人端口（可选），默认 18789
#
# 用法:
#   envsubst '$BASE_DOMAIN_1 $BASE_DOMAIN_2 $SSL_CERT_DIR $ADMIN_DIST_PATH $GATEWAY_PORT $NUXT_PORT $WECOM_PORT' \
#       < site.conf.tpl > /etc/nginx/sites-available/myapp
#   ln -sf /etc/nginx/sites-available/myapp /etc/nginx/sites-enabled/myapp
#   nginx -t && systemctl reload nginx

# HTTP -> HTTPS redirect
server {
    listen 80 default_server;
    listen [::]:80 default_server;
    server_name _;
    return 301 https://$host$request_uri;
}

# HTTPS - $BASE_DOMAIN_1 and all subdomains
server {
    listen 443 ssl;
    listen [::]:443 ssl;

    server_name $BASE_DOMAIN_1 *.$BASE_DOMAIN_1;

    ssl_certificate $SSL_CERT_DIR/$BASE_DOMAIN_1.pem;
    ssl_certificate_key $SSL_CERT_DIR/$BASE_DOMAIN_1.key;
    ssl_protocols TLSv1.2 TLSv1.3;
    ssl_ciphers HIGH:!aNULL:!MD5;

    client_max_body_size 30m;

    gzip on;
    gzip_vary on;
    gzip_min_length 1024;
    gzip_comp_level 5;
    gzip_types text/plain text/css text/javascript application/javascript application/json application/xml image/svg+xml;
    gzip_proxied any;
    proxy_read_timeout 300s;
    proxy_send_timeout 300s;

    location = /healthz {
        add_header Content-Type text/plain;
        return 200 "ok\n";
    }

    location ~ ^/(plugins/wecom/(agent|bot)(/default)?|wecom/(agent|bot)(/default)?)$ {
        proxy_pass http://127.0.0.1:$WECOM_PORT;
        proxy_http_version 1.1;
        proxy_set_header Host $host;
        proxy_set_header X-Real-IP $remote_addr;
        proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
        proxy_set_header X-Forwarded-Proto $scheme;
        proxy_set_header Connection "";
    }

    location /admin/ {
        alias $ADMIN_DIST_PATH/;
        try_files $uri $uri/ /admin/index.html;
    }

    location /user/ {
        proxy_pass http://127.0.0.1:$GATEWAY_PORT;
        proxy_http_version 1.1;
        proxy_set_header Host $host;
        proxy_set_header X-Real-IP $remote_addr;
        proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
        proxy_set_header X-Forwarded-Proto $scheme;
    }

    location /system/ {
        proxy_pass http://127.0.0.1:$GATEWAY_PORT;
        proxy_http_version 1.1;
        proxy_set_header Host $host;
        proxy_set_header X-Real-IP $remote_addr;
        proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
        proxy_set_header X-Forwarded-Proto $scheme;
    }

    location / {
        proxy_pass http://127.0.0.1:$NUXT_PORT;
        proxy_http_version 1.1;
        proxy_set_header Host $host;
        proxy_set_header X-Real-IP $remote_addr;
        proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
        proxy_set_header X-Forwarded-Proto $scheme;
        proxy_set_header Upgrade $http_upgrade;
        proxy_set_header Connection "upgrade";
    }
}

# HTTPS - $BASE_DOMAIN_2 and all subdomains（只在有第二个域名时启用；否则删除本块）
server {
    listen 443 ssl;
    listen [::]:443 ssl;

    server_name $BASE_DOMAIN_2 *.$BASE_DOMAIN_2;

    ssl_certificate $SSL_CERT_DIR/$BASE_DOMAIN_2.pem;
    ssl_certificate_key $SSL_CERT_DIR/$BASE_DOMAIN_2.key;
    ssl_protocols TLSv1.2 TLSv1.3;
    ssl_ciphers HIGH:!aNULL:!MD5;

    client_max_body_size 30m;

    gzip on;
    gzip_vary on;
    gzip_min_length 1024;
    gzip_comp_level 5;
    gzip_types text/plain text/css text/javascript application/javascript application/json application/xml image/svg+xml;
    gzip_proxied any;
    proxy_read_timeout 300s;
    proxy_send_timeout 300s;

    location = /healthz {
        add_header Content-Type text/plain;
        return 200 "ok\n";
    }

    location ~ ^/(plugins/wecom/(agent|bot)(/default)?|wecom/(agent|bot)(/default)?)$ {
        proxy_pass http://127.0.0.1:$WECOM_PORT;
        proxy_http_version 1.1;
        proxy_set_header Host $host;
        proxy_set_header X-Real-IP $remote_addr;
        proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
        proxy_set_header X-Forwarded-Proto $scheme;
        proxy_set_header Connection "";
    }

    location /admin/ {
        alias $ADMIN_DIST_PATH/;
        try_files $uri $uri/ /admin/index.html;
    }

    location /user/ {
        proxy_pass http://127.0.0.1:$GATEWAY_PORT;
        proxy_http_version 1.1;
        proxy_set_header Host $host;
        proxy_set_header X-Real-IP $remote_addr;
        proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
        proxy_set_header X-Forwarded-Proto $scheme;
    }

    location /system/ {
        proxy_pass http://127.0.0.1:$GATEWAY_PORT;
        proxy_http_version 1.1;
        proxy_set_header Host $host;
        proxy_set_header X-Real-IP $remote_addr;
        proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
        proxy_set_header X-Forwarded-Proto $scheme;
    }

    location / {
        proxy_pass http://127.0.0.1:$NUXT_PORT;
        proxy_http_version 1.1;
        proxy_set_header Host $host;
        proxy_set_header X-Real-IP $remote_addr;
        proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
        proxy_set_header X-Forwarded-Proto $scheme;
        proxy_set_header Upgrade $http_upgrade;
        proxy_set_header Connection "upgrade";
    }
}

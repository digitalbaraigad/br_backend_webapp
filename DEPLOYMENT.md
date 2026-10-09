# Ba Raigad production deployment — Hostinger VPS (without Docker)

This deployment serves the React build with NGINX and runs the Spring Boot API
as a systemd service on an Ubuntu Hostinger VPS. The public domain is
`https://baraigad.com` and `https://www.baraigad.com`.

## 1. Server packages

```bash
sudo apt update
sudo apt install -y openjdk-17-jre-headless nginx postgresql git
```

## 2. Application folders and source

```bash
sudo mkdir -p /opt/baraigad /var/lib/baraigad/uploads/{forms,mohims,forts,gallery,work-banners} /var/log/baraigad /etc/baraigad
sudo git clone https://github.com/digitalbaraigad/br_backend_webapp.git /opt/baraigad/backend
sudo git clone https://github.com/digitalbaraigad/br_frontend_webapp.git /opt/baraigad/frontend
```

Create the PostgreSQL database and application user. Choose a long unique
database password at the prompt; never copy it into Git.

```bash
sudo -u postgres createuser --pwprompt baraigad_user
sudo -u postgres createdb -O baraigad_user baraigad
```

## 3. Protected backend environment

```bash
sudo cp /opt/baraigad/backend/.env.preprod.example /etc/baraigad/baraigad.env
sudo nano /etc/baraigad/baraigad.env
sudo chmod 600 /etc/baraigad/baraigad.env
```

Set the database password, a long random `JWT_SECRET`, SMTP details and, if
used, WhatsApp credentials. Keep `PUBLIC_ORIGIN=https://baraigad.com` and
`CORS_ALLOWED_ORIGINS=https://baraigad.com,https://www.baraigad.com`.

For the very first start only, provide all three `INITIAL_SUPER_ADMIN_*`
values. Set `SPRING_JPA_HIBERNATE_DDL_AUTO=update` for that first start; after
the database is created and verified, change it to `validate` and remove
`INITIAL_SUPER_ADMIN_PASSWORD`.

## 4. Build the API and run it as a service

Build the JAR in GitHub Actions or locally and upload it to
`/opt/baraigad/baraigad-api.jar`. Create `/etc/systemd/system/baraigad.service`:

```ini
[Unit]
Description=Ba Raigad Spring Boot API
After=network.target postgresql.service

[Service]
Type=simple
User=www-data
Group=www-data
WorkingDirectory=/opt/baraigad
EnvironmentFile=/etc/baraigad/baraigad.env
ExecStart=/usr/bin/java -jar /opt/baraigad/baraigad-api.jar
Restart=always
RestartSec=5

[Install]
WantedBy=multi-user.target
```

```bash
sudo chown -R www-data:www-data /var/lib/baraigad /var/log/baraigad
sudo systemctl daemon-reload
sudo systemctl enable --now baraigad
sudo systemctl status baraigad
```

## 5. Build and deploy the frontend

Build the frontend in GitHub Actions or a Node 22 build machine. Upload the
contents of `dist/` to `/var/www/baraigad`:

```bash
sudo mkdir -p /var/www/baraigad
sudo chown -R www-data:www-data /var/www/baraigad
```

## 6. NGINX and HTTPS

Create `/etc/nginx/sites-available/baraigad`:

```nginx
server {
    listen 80;
    server_name baraigad.com www.baraigad.com;
    root /var/www/baraigad;
    index index.html;

    location /api/ { proxy_pass http://127.0.0.1:8080; proxy_set_header Host $host; proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for; proxy_set_header X-Forwarded-Proto $scheme; }
    location /uploads/ { proxy_pass http://127.0.0.1:8080; proxy_set_header Host $host; }
    location / { try_files $uri $uri/ /index.html; }
}
```

Enable it, test NGINX, and obtain the certificate after DNS A records for both
domains point to the VPS:

```bash
sudo ln -s /etc/nginx/sites-available/baraigad /etc/nginx/sites-enabled/baraigad
sudo nginx -t
sudo systemctl reload nginx
sudo apt install -y python3 python3-venv libaugeas0
sudo python3 -m venv /opt/certbot
sudo /opt/certbot/bin/pip install --upgrade pip certbot certbot-nginx
sudo ln -sf /opt/certbot/bin/certbot /usr/bin/certbot
sudo certbot --nginx -d baraigad.com -d www.baraigad.com
```

## 7. Checks and updates

Open `https://baraigad.com` and `https://baraigad.com/#admin`. Verify a public
API endpoint, then check logs with:

```bash
sudo journalctl -u baraigad -f
```

For each release: build frontend and backend, upload the new files, then run
`sudo systemctl restart baraigad` and `sudo systemctl reload nginx`.

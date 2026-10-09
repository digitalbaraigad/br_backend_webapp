# Docker deployment

Keep the two Git repositories as sibling folders on the server:

```text
baraigad/
  br_backend_webapp/
  br_frontend_webapp/
```

```bash
git clone https://github.com/digitalbaraigad/br_backend_webapp.git
git clone https://github.com/digitalbaraigad/br_frontend_webapp.git
cd br_backend_webapp
cp .env.preprod.example .env
```

Edit `.env` with production values, including secure database and JWT secrets.
Set `PUBLIC_ORIGIN` and `CORS_ALLOWED_ORIGINS` to the final HTTPS website URL,
for example `https://baraigad.com`.
For the first startup only, provide the three `INITIAL_SUPER_ADMIN_*` variables.

Start the application:

```bash
docker compose up -d --build
```

The public site is served on `WEB_PORT` (port 80 by default). NGINX forwards
`/api` and `/uploads` to the private backend container. PostgreSQL data,
uploaded files, and logs use named Docker volumes and remain after container
restarts.

After confirming the initial super-admin can sign in, remove
`INITIAL_SUPER_ADMIN_PASSWORD` from `.env` and run `docker compose up -d` again.

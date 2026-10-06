CREATE TABLE IF NOT EXISTS admin_menu_access (
    id BIGSERIAL PRIMARY KEY,
    user_id VARCHAR(100) NOT NULL,
    menu_key VARCHAR(100) NOT NULL,
    CONSTRAINT uk_admin_menu_access_user_menu UNIQUE (user_id, menu_key)
);

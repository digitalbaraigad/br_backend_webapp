-- The Join Us form no longer collects a separate WhatsApp number.
-- IF EXISTS keeps this safe for both existing and freshly created databases.
ALTER TABLE IF EXISTS participants
    DROP COLUMN IF EXISTS whatsapp_number;

-- Mohim schedules are no longer collected or returned by the API.
ALTER TABLE IF EXISTS mohim_info
    DROP COLUMN IF EXISTS schedule_en,
    DROP COLUMN IF EXISTS schedule_mr;

ALTER TABLE IF EXISTS mohim_info
    ADD COLUMN IF NOT EXISTS whatsapp_group_link VARCHAR(2048);

CREATE TABLE IF NOT EXISTS admin_menu_access (
    id BIGSERIAL PRIMARY KEY,
    user_id VARCHAR(100) NOT NULL,
    menu_key VARCHAR(100) NOT NULL,
    CONSTRAINT uk_admin_menu_access_user_menu UNIQUE (user_id, menu_key)
);

CREATE TABLE IF NOT EXISTS work_page_banners (
    banner_id BIGSERIAL PRIMARY KEY,
    page_key VARCHAR(40) NOT NULL,
    image_url VARCHAR(2048) NOT NULL,
    display_order INTEGER NOT NULL DEFAULT 0,
    created_date TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

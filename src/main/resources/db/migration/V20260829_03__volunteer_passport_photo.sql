ALTER TABLE volunteer_info
    ADD COLUMN IF NOT EXISTS volunteer_passport_photo_url VARCHAR(500);

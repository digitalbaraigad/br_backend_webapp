ALTER TABLE volunteer_info
    ADD COLUMN IF NOT EXISTS volunteer_district VARCHAR(255);

ALTER TABLE volunteer_info
    ADD COLUMN IF NOT EXISTS volunteer_taluka VARCHAR(255);

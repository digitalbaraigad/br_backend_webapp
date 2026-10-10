-- Adds a dedicated Marathi district value for bilingual Fort content.
ALTER TABLE forts
    ADD COLUMN IF NOT EXISTS district_mr VARCHAR(255);

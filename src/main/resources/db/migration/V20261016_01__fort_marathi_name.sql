-- Store a dedicated Marathi name for every fort. Existing records retain
-- their current value until an administrator provides the Marathi spelling.
ALTER TABLE forts
    ADD COLUMN IF NOT EXISTS fort_name_mr VARCHAR(255);

UPDATE forts
SET fort_name_mr = fort_name
WHERE fort_name_mr IS NULL OR BTRIM(fort_name_mr) = '';

ALTER TABLE forts
    ALTER COLUMN fort_name_mr SET NOT NULL;

-- Apply this migration before starting the backend that accepts text-based
-- latitude and longitude values. Existing numeric coordinate values are
-- preserved as their text representation.
BEGIN;

ALTER TABLE forts
    ALTER COLUMN latitude TYPE VARCHAR(255)
    USING latitude::text;

ALTER TABLE forts
    ALTER COLUMN longitude TYPE VARCHAR(255)
    USING longitude::text;

COMMIT;

-- Apply through your schema process if ddl-auto=update is disabled. No Flyway is configured.
CREATE TABLE IF NOT EXISTS fort_guides (
    fort_id BIGINT PRIMARY KEY REFERENCES forts(fort_id),
    revision BIGINT,
    draft_json TEXT NOT NULL,
    published_json TEXT,
    package_version INTEGER NOT NULL DEFAULT 0,
    published BOOLEAN NOT NULL DEFAULT FALSE
);

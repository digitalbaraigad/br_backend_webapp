-- Apply this migration in pre-production/production before starting the service.
-- The local profile uses Hibernate ddl-auto=update and creates this table automatically.
CREATE TABLE IF NOT EXISTS admin_audit_log (
    audit_id BIGINT NOT NULL AUTO_INCREMENT,
    admin_username VARCHAR(150),
    action VARCHAR(40) NOT NULL,
    module_name VARCHAR(100),
    request_method VARCHAR(12),
    resource_path VARCHAR(500),
    details VARCHAR(2000),
    ip_address VARCHAR(80),
    response_status INT,
    created_at DATETIME NOT NULL,
    PRIMARY KEY (audit_id),
    INDEX idx_admin_audit_created (created_at),
    INDEX idx_admin_audit_user (admin_username)
);

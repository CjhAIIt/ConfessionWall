-- Run manually on a new deployment after selecting/creating confession_db.
-- Existing installations should inspect their schema before applying migrations.
CREATE TABLE IF NOT EXISTS confession (
    id INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(100) NOT NULL,
    message TEXT NOT NULL,
    create_time TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    INDEX idx_confession_create_time (create_time)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

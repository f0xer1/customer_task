CREATE TABLE IF NOT EXISTS customer (
    id BIGSERIAL PRIMARY KEY,
    created BIGINT NOT NULL DEFAULT EXTRACT(EPOCH FROM NOW()),
    updated BIGINT,
    email VARCHAR(255) UNIQUE,
    full_name VARCHAR(255),
    phone VARCHAR(255),
    is_active BOOLEAN NOT NULL DEFAULT TRUE
    );


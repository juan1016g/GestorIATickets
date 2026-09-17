CREATE TABLE users (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    email VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    role VARCHAR(50) NOT NULL,
    created_date TIMESTAMP,
    modified_date TIMESTAMP,
    created_by VARCHAR(255),
    modified_by VARCHAR(255)
);

CREATE TABLE tickets (
    id BIGSERIAL PRIMARY KEY,
    title VARCHAR(100) NOT NULL,
    description VARCHAR(2000) NOT NULL,
    category VARCHAR(50),
    priority VARCHAR(50),
    ticket_status VARCHAR(50) NOT NULL,
    requester_id BIGINT NOT NULL,
    assignee_id BIGINT,
    ai_summary VARCHAR(500),
    ai_tags JSONB,
    is_active BOOLEAN NOT NULL DEFAULT TRUE,
    version BIGINT NOT NULL DEFAULT 0,
    created_date TIMESTAMP,
    modified_date TIMESTAMP,
    created_by VARCHAR(255),
    modified_by VARCHAR(255),
    CONSTRAINT fk_requester FOREIGN KEY (requester_id) REFERENCES users (id),
    CONSTRAINT fk_assignee FOREIGN KEY (assignee_id) REFERENCES users (id)
);
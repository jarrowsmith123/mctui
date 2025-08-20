-- Add migration script here
CREATE TABLE events(
    id UUID,
    event_type VARCHAR(40) NOT NULL,
    player_name VARCHAR(40),
    created_at TIMESTAMP,
    payload JSONB
);

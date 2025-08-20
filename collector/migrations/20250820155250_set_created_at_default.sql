-- Add migration script here
ALTER TABLE events
ALTER COLUMN created_at SET DEFAULT NOW();
CREATE EXTENSION IF NOT EXISTS pgcrypto;

CREATE TABLE feature_flags (
    id          UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    slug        TEXT NOT NULL UNIQUE,
    type        TEXT NOT NULL CHECK (type IN ('BOOLEAN','NUMBER','STRING','LIST')),
    schema      JSONB NOT NULL,
    description TEXT,
    created_at  TIMESTAMPTZ NOT NULL DEFAULT now(),
    updated_at  TIMESTAMPTZ NOT NULL DEFAULT now()
);

CREATE TABLE feature_flag_values (
    id          UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    flag_id     UUID NOT NULL REFERENCES feature_flags(id) ON DELETE CASCADE,
    value       JSONB NOT NULL,
    version     INT  NOT NULL DEFAULT 1,
    updated_by  TEXT NOT NULL,
    created_at  TIMESTAMPTZ NOT NULL DEFAULT now(),
    updated_at  TIMESTAMPTZ NOT NULL DEFAULT now(),
    UNIQUE (flag_id, version)
);

CREATE INDEX idx_ff_values_flagid ON feature_flag_values (flag_id);
CREATE INDEX idx_ff_values_flagid_version ON feature_flag_values (flag_id, version DESC);
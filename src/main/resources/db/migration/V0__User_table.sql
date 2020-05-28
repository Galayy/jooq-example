CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

CREATE TYPE role AS ENUM ('ROLE_USER', 'ROLE_ADMIN' );

CREATE TABLE "user"
(
    id         UUID PRIMARY KEY   DEFAULT uuid_generate_v4(),
    first_name TEXT      NOT NULL,
    last_name  TEXT,
    phone      TEXT,
    login      TEXT      NOT NULL,
    password   TEXT      NOT NULL,
    created_at timestamp NOT NULL DEFAULT now(),
    updated_at timestamp,
    deleted_at timestamp,
    role       role      NOT NULL
);

CREATE EXTENSION IF NOT EXISTS pg_trgm;

CREATE TABLE IF NOT EXISTS pessoa (
   id UUID NOT NULL,
   apelido VARCHAR(32) UNIQUE NOT NULL,
   nome VARCHAR(100) NOT NULL,
   nascimento date NOT NULL,
   stack TEXT[],
   searchable TEXT NOT NULL,
   CONSTRAINT pk_pessoa PRIMARY KEY (id)
);

CREATE INDEX IF NOT EXISTS idx_pessoa_searchable ON pessoa USING gist (searchable gist_trgm_ops);

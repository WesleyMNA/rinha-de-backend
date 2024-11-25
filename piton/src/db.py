from datetime import date
from typing import List
from uuid import UUID

from sqlalchemy import String, TEXT, text
from sqlalchemy.ext.asyncio import async_sessionmaker, create_async_engine
from sqlalchemy.orm import DeclarativeBase, Mapped, mapped_column
from sqlalchemy.dialects.postgresql import ARRAY

from src.env import env

_url = f'postgresql+asyncpg://postgres:root@{env.db_host}/{env.postgres_db}'
engine = create_async_engine(
    _url,
    pool_pre_ping=True,
    pool_size=10,
    max_overflow=20,
    echo=env.show_sql,
)
async_session = async_sessionmaker(bind=engine)


class Base(DeclarativeBase):
    pass


async def create_schema():
    queries = [
        'CREATE EXTENSION IF NOT EXISTS pg_trgm;',
        '''
            CREATE OR REPLACE FUNCTION generate_searchable(nome VARCHAR, apelido VARCHAR, stack VARCHAR[])
                RETURNS TEXT AS $$
                BEGIN
                RETURN nome || apelido || stack;
                END;
            $$ LANGUAGE plpgsql IMMUTABLE;
        ''',
        '''
            CREATE TABLE IF NOT EXISTS pessoa (
               id UUID DEFAULT gen_random_uuid() NOT NULL,
               apelido VARCHAR(32) UNIQUE NOT NULL,
               nome VARCHAR(100) NOT NULL,
               nascimento date NOT NULL,
               stack VARCHAR(32)[],
               searchable TEXT GENERATED ALWAYS AS (generate_searchable(nome, apelido, stack)) STORED,
               CONSTRAINT pk_pessoa PRIMARY KEY (id)
            );
        ''',
        'CREATE INDEX IF NOT EXISTS idx_pessoa_searchable ON pessoa USING gist (searchable gist_trgm_ops);',
        'CREATE UNIQUE INDEX IF NOT EXISTS idx_pessoa_apelido ON pessoa USING btree (apelido);',
    ]
    async with engine.begin() as db:
        for q in queries:
            query = text(q)
            await db.execute(query)


class Pessoa(Base):
    __tablename__ = 'pessoa'

    id: Mapped[UUID] = mapped_column(primary_key=True)
    apelido: Mapped[str] = mapped_column(String(32), unique=True)
    nome: Mapped[str] = mapped_column(String(100))
    nascimento: Mapped[date]
    stack: Mapped[List[str] | None] = mapped_column(ARRAY(String(32)))
    searchable: Mapped[str] = mapped_column(TEXT)

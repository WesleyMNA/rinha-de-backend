from typing import Annotated, List
from uuid import UUID

from fastapi import APIRouter, Depends, HTTPException
from fastapi.params import Query
from sqlalchemy import select
from sqlalchemy.ext.asyncio import AsyncSession
from starlette import status

from src.db import get_db, Pessoa
from src.dtos import PessoaResponse

DbSession = Annotated[AsyncSession, Depends(get_db)]
pessoa_router = APIRouter(prefix='/pessoas')


@pessoa_router.get(
    '',
    response_model=List[PessoaResponse]
)
async def find_by_termo(
        db: DbSession,
        t: str | None = Query(None),
):
    if t is None:
        raise HTTPException(status_code=status.HTTP_400_BAD_REQUEST)

    query = select(Pessoa).where(Pessoa.searchable.like(f'%{t}%')).limit(50)
    result = await db.execute(query)
    return result.scalars().all()


@pessoa_router.get(
    '/{pessoa_id}',
    response_model=PessoaResponse
)
async def find_by_id(
        pessoa_id: UUID,
        db: DbSession,
):
    query = select(Pessoa).where(Pessoa.id == pessoa_id)
    result = await db.execute(query)
    pessoa = result.scalars().first()

    if pessoa is None:
        raise HTTPException(status_code=status.HTTP_404_NOT_FOUND)
    return pessoa


@pessoa_router.post('')
async def create(
        request,
        db: DbSession,
):
    pass


main_router = APIRouter()
main_router.include_router(pessoa_router)


@main_router.get('/contagem-pessoas')
async def count():
    pass

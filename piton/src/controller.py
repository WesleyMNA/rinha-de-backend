from typing import Annotated
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


@pessoa_router.get('')
async def find_by_termo(
        db: DbSession,
        t: str = Query(),
):
    pass


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

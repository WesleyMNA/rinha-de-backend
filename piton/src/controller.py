import uuid
from typing import Annotated, List

from fastapi import APIRouter, Depends, HTTPException
from fastapi.params import Query
from sqlalchemy import select
from sqlalchemy.dialects.postgresql import insert
from sqlalchemy.ext.asyncio import AsyncSession
from sqlalchemy.sql.functions import func
from starlette import status
from starlette.responses import Response

from src.db import get_db, Pessoa
from src.dtos import PessoaResponse, PessoaRequest

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
        pessoa_id: uuid.UUID,
        db: DbSession,
):
    query = select(Pessoa).where(Pessoa.id == pessoa_id)
    result = await db.execute(query)
    pessoa = result.scalars().first()

    if pessoa is None:
        raise HTTPException(status_code=status.HTTP_404_NOT_FOUND)
    return pessoa


async def invalid(request: PessoaRequest):
    return (
            request.nome.isdigit() or
            request.apelido.isdigit() or
            (
                    request.stack is not None
                    and any(s.isdigit() for s in request.stack)
            )
    )


@pessoa_router.post(
    '',
    status_code=status.HTTP_201_CREATED,
)
async def create(
        request: PessoaRequest,
        db: DbSession,
        response: Response
):
    if await invalid(request):
        raise HTTPException(status_code=status.HTTP_400_BAD_REQUEST)

    pessoa_id = uuid.uuid4()
    query = insert(Pessoa).values({
        'id': pessoa_id,
        **request.model_dump(),
    })

    try:
        await db.execute(query)
        await db.commit()
    except:
        raise HTTPException(status_code=status.HTTP_422_UNPROCESSABLE_ENTITY)

    response.headers['Location'] = f'/pessoas/{pessoa_id}'
    return pessoa_id


main_router = APIRouter()
main_router.include_router(pessoa_router)


@main_router.get('/contagem-pessoas')
async def count(db: DbSession):
    query = select(func.count(Pessoa.id))
    result = await db.execute(query)
    return result.scalar()

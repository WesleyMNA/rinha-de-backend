from uuid import UUID

from fastapi import APIRouter
from fastapi.params import Query

pessoa_router = APIRouter(prefix='/pessoas')


@pessoa_router.get('')
async def find_by_termo(t: str = Query()):
    pass


@pessoa_router.get('/{pessoa_id}')
async def find_by_id(pessoa_id: UUID):
    pass


@pessoa_router.post('')
async def create(request):
    pass


main_router = APIRouter()
main_router.include_router(pessoa_router)


@main_router.get('/contagem-pessoas')
async def count():
    pass

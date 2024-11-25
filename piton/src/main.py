from contextlib import asynccontextmanager

from fastapi import FastAPI

from src.controller import main_router
from src.db import create_schema


@asynccontextmanager
async def lifespan(application: FastAPI):
    await create_schema()
    yield


app = FastAPI(lifespan=lifespan)
app.include_router(main_router)

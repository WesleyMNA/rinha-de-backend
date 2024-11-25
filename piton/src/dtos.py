from datetime import date
from typing import List
from uuid import UUID

from pydantic import BaseModel, Field


class PessoaRequest(BaseModel):
    apelido: str = Field(..., max_length=32)
    nome: str = Field(..., max_length=100)
    nascimento: date = Field(...)
    stack: List[str] | None = None


class PessoaResponse(PessoaRequest):
    id: UUID

FROM python:3.12-slim

ENV TZ America/Sao_Paulo
ENV TIMEZONE America/Sao_Paulo

ENV PYTHONPATH /opt
ENV LOGURU_COLORIZE true
ENV POETRY_VIRTUALENVS_CREATE=false

WORKDIR ${PYTHONPATH}

COPY ./src ./src

ADD poetry.lock .
ADD pyproject.toml .

RUN pip3 install pip --upgrade
RUN pip install poetry
RUN poetry install --no-root

CMD ["uvicorn", "src.main:app", "--host", "0.0.0.0", "--port", "8080"]

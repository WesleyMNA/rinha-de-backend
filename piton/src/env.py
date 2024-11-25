from pydantic_settings import BaseSettings


class Env(BaseSettings):
    db_host: str = 'db'
    postgres_db: str = 'rinha'

    show_sql: bool = False


env = Env()

package com.rinha.java;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

@Configuration
public class DbInitializer {

    private final String url;
    private final String username;
    private final String password;

    public DbInitializer(@Value("${spring.datasource.url}") String url,
                         @Value("${spring.datasource.username}") String username,
                         @Value("${spring.datasource.password}") String password) {
        this.url = url;
        this.username = username;
        this.password = password;
    }

    @PostConstruct
    public void createDbSchema() {
        var query = """
                CREATE EXTENSION IF NOT EXISTS pg_trgm;

                CREATE OR REPLACE FUNCTION generate_searchable(nome VARCHAR, apelido VARCHAR, stack VARCHAR[])
                    RETURNS TEXT AS $$
                    BEGIN
                    RETURN nome || apelido || stack;
                    END;
                $$ LANGUAGE plpgsql IMMUTABLE;

                CREATE TABLE IF NOT EXISTS pessoa (
                   id UUID NOT NULL,
                   apelido VARCHAR(32) UNIQUE NOT NULL,
                   nome VARCHAR(100) NOT NULL,
                   nascimento date NOT NULL,
                   stack VARCHAR(32)[],
                   searchable TEXT GENERATED ALWAYS AS (generate_searchable(nome, apelido, stack)) STORED,
                   CONSTRAINT pk_pessoa PRIMARY KEY (id)
                );

                CREATE INDEX IF NOT EXISTS idx_pessoa_searchable ON pessoa USING gist (searchable gist_trgm_ops);

                CREATE UNIQUE INDEX IF NOT EXISTS idx_pessoa_apelido ON pessoa USING btree (apelido);
                """;
        try (Connection conn = DriverManager.getConnection(url, username, password);
             Statement stmt = conn.createStatement()) {
            stmt.executeUpdate(query);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

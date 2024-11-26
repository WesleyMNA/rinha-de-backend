package com.rinha.java.pessoas;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface PessoaRepository extends JpaRepository<Pessoa, UUID> {

    @Query(value = """
            SELECT
               id,
               apelido,
               nome,
               nascimento,
               stack
            FROM
                pessoa
            WHERE
                searchable ILIKE :termo
            """, nativeQuery = true)
    List<PessoaResponse> findTop50BySearchableLike(String termo);

    @Query(value = """
            SELECT
               id,
               apelido,
               nome,
               nascimento,
               stack
            FROM
                Pessoa
            WHERE
                id = :id
            """, nativeQuery = true)
    Optional<PessoaResponse> findIdApelidoNomeNascimentoStackById(UUID id);

    @Modifying
    @Transactional
    @Query("""
            INSERT INTO
            	Pessoa(
            		id,
            		apelido,
            		nome,
            		nascimento,
            		stack
            	)
            VALUES
            	(
            		:id,
            		:apelido,
            		:nome,
            		:nascimento,
            		:stack
            	)""")
    void insert(UUID id, String apelido, String nome, LocalDate nascimento, String[] stack);
}

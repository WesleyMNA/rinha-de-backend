package com.rinha.java.pessoas;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface PessoaRepository extends JpaRepository<Pessoa, UUID> {

    @Query(value = """
            SELECT
                *
            FROM
                pessoa
            WHERE
                searchable ILIKE :termo
            """, nativeQuery = true)
    List<Pessoa> findTop50BySearchableLike(String termo);
}

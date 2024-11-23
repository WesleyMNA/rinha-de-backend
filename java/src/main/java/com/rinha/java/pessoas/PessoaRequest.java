package com.rinha.java.pessoas;

import jakarta.validation.constraints.Size;

import java.util.List;

public record PessoaRequest(
        @Size(max = 32)
        String apelido,
        @Size(max = 100)
        String nome,
        @Size(max = 10)
        String nascimento,
        List<String> stack
) {
}

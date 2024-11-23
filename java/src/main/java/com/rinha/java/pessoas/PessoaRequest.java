package com.rinha.java.pessoas;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record PessoaRequest(
        @NotNull
        @NotEmpty
        @Max(32)
        String apelido,
        @NotNull
        @NotEmpty
        @Max(100)
        String nome,
        @NotNull
        @NotEmpty
        @Max(10)
        String nascimento,
        List<String> stack
) {
}

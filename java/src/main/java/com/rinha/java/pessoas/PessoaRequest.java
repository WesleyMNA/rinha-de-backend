package com.rinha.java.pessoas;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.List;

public record PessoaRequest(
        @NotNull
        @NotEmpty
        @Size(max = 32)
        String apelido,
        @NotNull
        @NotEmpty
        @Size(max = 100)
        String nome,
        @NotNull
        @NotEmpty
        @Size(max = 10)
        String nascimento,
        List<String> stack
) {
}

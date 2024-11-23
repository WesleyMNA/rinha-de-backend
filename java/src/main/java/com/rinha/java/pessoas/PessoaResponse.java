package com.rinha.java.pessoas;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public record PessoaResponse(
        UUID id,
        String apelido,
        String nome,
        LocalDate nascimento,
        List<String> stack
) {
}

package com.rinha.java.pessoas;

import java.util.List;
import java.util.UUID;

public record PessoaResponse(
        UUID id,
        String apelido,
        String nome,
        String nascimento,
        List<String> stack
) {
}

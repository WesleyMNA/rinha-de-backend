package com.rinha.java.pessoas;

import java.util.UUID;

public interface PessoaResponse {

    UUID getId();

    String getApelido();

    String getNome();

    String getNascimento();

    String[] getStack();
}

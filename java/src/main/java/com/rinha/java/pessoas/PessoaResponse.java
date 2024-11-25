package com.rinha.java.pessoas;

import java.util.List;
import java.util.UUID;

public interface PessoaResponse {

    UUID getId();
    String getApelido();
    String getNome();
    String getNascimento();
    List<String> getStack();
}

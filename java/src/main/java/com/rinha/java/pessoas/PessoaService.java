package com.rinha.java.pessoas;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

@Service
public class PessoaService {

    private final PessoaRepository repository;

    public PessoaService(PessoaRepository repository) {
        this.repository = repository;
    }

    @Async
    public CompletionStage<Long> count() {
        long count = repository.count();
        return CompletableFuture.completedFuture(count);
    }
}

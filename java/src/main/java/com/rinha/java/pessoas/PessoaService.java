package com.rinha.java.pessoas;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

@Service
public class PessoaService {

    private final PessoaRepository repository;

    public PessoaService(PessoaRepository repository) {
        this.repository = repository;
    }

    @Async
    public CompletionStage<Optional<PessoaResponse>> findById(UUID id) {
        Optional<Pessoa> optional = repository.findById(id);

        if (optional.isEmpty())
            return CompletableFuture.completedFuture(Optional.empty());

        Pessoa pessoa = optional.get();
        var res = new PessoaResponse(
                pessoa.getId(),
                pessoa.getApelido(),
                pessoa.getNome(),
                pessoa.getNascimento(),
                pessoa.getStack()
        );
        return CompletableFuture.completedFuture(Optional.of(res));
    }

    @Async
    public CompletionStage<Long> count() {
        long count = repository.count();
        return CompletableFuture.completedFuture(count);
    }
}

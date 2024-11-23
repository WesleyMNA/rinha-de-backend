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

    @Async
    public CompletionStage<UUID> create(PessoaRequest request) {
        validateRequest(request);
        var pessoa = new Pessoa(request);
        repository.save(pessoa);
        return CompletableFuture.completedFuture(pessoa.getId());
    }

    private void validateRequest(PessoaRequest request) {
        if (isNumber(request.apelido()))
            throw new RuntimeException();

        if (isNumber(request.nome()))
            throw new RuntimeException();

        if (isNumber(request.nascimento()))
            throw new RuntimeException();

        if (request.stack() != null)
            request.stack().forEach(value -> {
                if (isNumber(value))
                    throw new RuntimeException();
            });
    }

    private boolean isNumber(String str) {
        try {
            Integer.parseInt(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

}

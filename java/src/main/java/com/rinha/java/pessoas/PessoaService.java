package com.rinha.java.pessoas;

import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class PessoaService {

    private final PessoaRepository repository;

    public PessoaService(PessoaRepository repository) {
        this.repository = repository;
    }

    public List<PessoaResponse> findByTermo(String termo) {
        return repository.findTop50BySearchableLike("%" + termo + "%")
                .stream()
                .map(this::toResponse)
                .toList();
    }

    public Optional<PessoaResponse> findById(UUID id) {
        Optional<Pessoa> optional = repository.findById(id);

        if (optional.isEmpty())
            return Optional.empty();

        Pessoa pessoa = optional.get();
        var res = toResponse(pessoa);
        return Optional.of(res);
    }

    private PessoaResponse toResponse(Pessoa pessoa) {
        return new PessoaResponse(
                pessoa.getId(),
                pessoa.getApelido(),
                pessoa.getNome(),
                pessoa.getNascimento(),
                pessoa.getStack()
        );
    }

    public Long count() {
        return repository.count();
    }

    public UUID create(PessoaRequest request) {
        validateRequest(request);
        var pessoa = new Pessoa(request);
        repository.save(pessoa);
        return pessoa.getId();
    }

    private void validateRequest(PessoaRequest request) {
        if (isNumber(request.apelido()))
            throw new RuntimeException();

        if (isNumber(request.nome()))
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

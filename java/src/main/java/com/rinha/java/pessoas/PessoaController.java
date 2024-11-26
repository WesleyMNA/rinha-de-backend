package com.rinha.java.pessoas;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping
public class PessoaController {

    private final PessoaRepository repository;

    public PessoaController(PessoaRepository repository) {
        this.repository = repository;
    }

    @GetMapping("/pessoas")
    public ResponseEntity<List<PessoaResponse>> findByTermo(@RequestParam(name = "t") String t) {
        List<PessoaResponse> res = repository.findTop50BySearchableLike("%" + t + "%");
        return ResponseEntity.ok(res);
    }

    @GetMapping("/pessoas/{id}")
    public ResponseEntity<PessoaResponse> findById(@PathVariable UUID id) {
        Optional<PessoaResponse> res = repository.findIdApelidoNomeNascimentoStackById(id);
        return res.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/contagem-pessoas")
    public ResponseEntity<Long> count() {
        return ResponseEntity.ok(repository.count());
    }

    @PostMapping("/pessoas")
    public ResponseEntity<Void> create(@RequestBody @Valid PessoaRequest request)
            throws URISyntaxException {
        validate(request);
        var pessoa = new Pessoa(request);
        repository.save(pessoa);
        return ResponseEntity
                .created(new URI("/pessoas/%s".formatted(pessoa.getId())))
                .build();
    }

    private void validate(PessoaRequest request) {
        if (isNumber(request.apelido()))
            throw new RuntimeException();

        if (isNumber(request.nome()))
            throw new RuntimeException();

        if (request.stack() != null)
            for (String value : request.stack()) {
                if (isNumber(value))
                    throw new RuntimeException();
            }
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

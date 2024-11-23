package com.rinha.java.pessoas;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;
import java.util.concurrent.CompletionStage;

@RestController
@RequestMapping
public class PessoaController {

    private final PessoaService service;

    public PessoaController(PessoaService service) {
        this.service = service;
    }

    @GetMapping("/pessoas")
    public CompletionStage<ResponseEntity<?>> findByTermo(@RequestParam String t) {
        return null;
    }

    @GetMapping("/pessoas/{id}")
    public CompletionStage<ResponseEntity<?>> findById(@PathVariable UUID id) {
        return null;
    }

    @Async
    @GetMapping("/contagem-pessoas")
    public CompletionStage<ResponseEntity<?>> count() {
        return service.count().thenApply(ResponseEntity::ok);
    }

    @Async
    @PostMapping("/pessoas")
    public CompletionStage<ResponseEntity<Long>> create(@RequestBody @Valid PessoaRequest request) {
        return null;
    }
}

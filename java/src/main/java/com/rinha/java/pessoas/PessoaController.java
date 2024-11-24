package com.rinha.java.pessoas;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping
public class PessoaController {

    private final PessoaService service;

    public PessoaController(PessoaService service) {
        this.service = service;
    }

    @Async
    @GetMapping("/pessoas")
    public CompletableFuture<ResponseEntity<List<PessoaResponse>>> findByTermo(@RequestParam(name = "t") String t) {
        return service
                .findByTermo(t)
                .thenApply(ResponseEntity::ok);
    }

    @Async
    @GetMapping("/pessoas/{id}")
    public CompletableFuture<ResponseEntity<PessoaResponse>> findById(@PathVariable UUID id) {
        return service
                .findById(id)
                .thenApply(res -> res.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build()));
    }

    @Async
    @GetMapping("/contagem-pessoas")
    public CompletableFuture<ResponseEntity<Long>> count() {
        return service.count().thenApply(ResponseEntity::ok);
    }

    @Async
    @PostMapping("/pessoas")
    public CompletableFuture<ResponseEntity<Void>> create(@RequestBody @Valid PessoaRequest request,
                                                          UriComponentsBuilder builder) {
        return service.create(request)
                .thenApply(id ->
                        ResponseEntity
                                .created(builder.path("/pessoas/{id}").buildAndExpand(id).toUri())
                                .build());
    }
}

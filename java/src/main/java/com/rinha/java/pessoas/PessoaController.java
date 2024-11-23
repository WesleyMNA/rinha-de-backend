package com.rinha.java.pessoas;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;
import java.util.concurrent.CompletionStage;

@RestController
@RequestMapping
public class PessoaController {

    @GetMapping("/pessoas")
    public CompletionStage<ResponseEntity<?>> findByTermo(@RequestParam String t) {
        return null;
    }

    @GetMapping("/pessoas/{id}")
    public CompletionStage<ResponseEntity<?>> findById(@PathVariable UUID id) {
        return null;
    }

    @GetMapping("/contagem-pessoas")
    public CompletionStage<ResponseEntity<?>> count() {
        return null;
    }

    @PostMapping("/pessoas")
    public CompletionStage<ResponseEntity<?>> create(@RequestBody @Valid PessoaRequest request) {
        return null;
    }
}

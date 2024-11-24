package com.rinha.java.pessoas;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping
public class PessoaController {

    private final PessoaService service;

    public PessoaController(PessoaService service) {
        this.service = service;
    }

    @GetMapping("/pessoas")
    public ResponseEntity<List<PessoaResponse>> findByTermo(@RequestParam(name = "t") String t) {
        List<PessoaResponse> res = service.findByTermo(t);
        return ResponseEntity.ok(res);
    }

    @GetMapping("/pessoas/{id}")
    public ResponseEntity<PessoaResponse> findById(@PathVariable UUID id) {
        Optional<PessoaResponse> res = service.findById(id);
        return res.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/contagem-pessoas")
    public ResponseEntity<Long> count() {
        return ResponseEntity.ok(service.count());
    }

    @PostMapping("/pessoas")
    public ResponseEntity<Void> create(@RequestBody @Valid PessoaRequest request)
            throws URISyntaxException {
        UUID id = service.create(request);
        return ResponseEntity
                .created(new URI("/pessoas/%s".formatted(id)))
                .build();
    }
}

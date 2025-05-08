package com.rinha.java.pessoas;

import com.zaxxer.hikari.HikariDataSource;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.sql.*;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping
public class PessoaController {

    private final PessoaRepository repository;
    private final HikariDataSource hikariDataSource;

    public PessoaController(PessoaRepository repository,
                            HikariDataSource hikariDataSource) {
        this.repository = repository;
        this.hikariDataSource = hikariDataSource;
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
            throws URISyntaxException, SQLException {
        validate(request);
        UUID id = UUID.randomUUID();
        Date date = Date.valueOf(request.nascimento());
        var query = """
                INSERT INTO
                	public.pessoa(
                		id,
                		apelido,
                		nome,
                		nascimento,
                		stack
                	)
                VALUES
                	(?, ?, ?, ?, ?);
                """;
        try (Connection conn = hikariDataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setObject(1, id);
            stmt.setString(2, request.apelido());
            stmt.setString(3, request.nome());
            stmt.setDate(4, date);
            stmt.setObject(5, request.stack());
            stmt.executeUpdate();
        }
        return ResponseEntity
                .created(new URI("/pessoas/%s".formatted(id)))
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

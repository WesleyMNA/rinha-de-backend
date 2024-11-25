package com.rinha.java.pessoas;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "pessoa")
public class Pessoa {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id")
    private UUID id;
    @Column(name = "apelido", nullable = false, unique = true, length = 32)
    private String apelido;
    @Column(name = "nome", nullable = false, length = 100)
    private String nome;
    @Column(name = "nascimento", nullable = false)
    private LocalDate nascimento;
    @Column(name = "stack", length = 32)
    private List<String> stack;
    @Column(name = "searchable", insertable = false, updatable = false)
    private String searchable;

    public Pessoa() {
    }

    public Pessoa(PessoaRequest request) {
        if (isNumber(request.apelido()))
            throw new RuntimeException();

        this.apelido = request.apelido();

        if (isNumber(request.nome()))
            throw new RuntimeException();

        this.nome = request.nome();
        this.nascimento = LocalDate.parse(request.nascimento());

        if (request.stack() != null)
            request.stack().forEach(value -> {
                if (isNumber(value))
                    throw new RuntimeException();
            });

        this.stack = request.stack();
    }

    public UUID getId() {
        return id;
    }

    public String getApelido() {
        return apelido;
    }

    public String getNome() {
        return nome;
    }

    public LocalDate getNascimento() {
        return nascimento;
    }

    public List<String> getStack() {
        return stack;
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

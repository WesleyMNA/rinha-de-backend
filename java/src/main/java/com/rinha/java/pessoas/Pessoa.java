package com.rinha.java.pessoas;

import jakarta.persistence.*;

import java.time.LocalDate;
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
    private String[] stack;
    @Column(name = "searchable", insertable = false, updatable = false)
    private String searchable;

    public Pessoa() {
    }

    public Pessoa(PessoaRequest request) {
        this.apelido = request.apelido();
        this.nome = request.nome();
        this.nascimento = LocalDate.parse(request.nascimento());
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

    public String[] getStack() {
        return stack;
    }
}

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
    @Column(name = "searchable", nullable = false)
    private String searchable;

    public Pessoa() {
    }

    public Pessoa(PessoaRequest request) {
        this.apelido = request.apelido();
        this.nome = request.nome();
        this.nascimento = LocalDate.parse(request.nascimento());
        this.stack = request.stack();
        this.searchable = generateSearchable(this.apelido, this.nome, this.stack);
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

    private static String generateSearchable(String nome, String apelido, List<String> stack) {
        StringBuilder result = new StringBuilder(String.format("{%s, %s", nome, apelido));

        if (stack != null && !stack.isEmpty()) {
            result.append(", ");
            result.append(String.join(", ", stack));
        }

        result.append("}");
        return result.toString();
    }
}

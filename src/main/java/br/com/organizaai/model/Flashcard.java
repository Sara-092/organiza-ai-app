package br.com.organizaai.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
public class Flashcard {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 180)
    private String pergunta;

    @Column(nullable = false, length = 1000)
    private String resposta;

    private LocalDateTime criadoEm;

    @PrePersist
    public void antesDeSalvar() {
        if (criadoEm == null) criadoEm = LocalDateTime.now();
    }

    public Long getId() { return id; }
    public String getPergunta() { return pergunta; }
    public void setPergunta(String pergunta) { this.pergunta = pergunta; }
    public String getResposta() { return resposta; }
    public void setResposta(String resposta) { this.resposta = resposta; }
    public LocalDateTime getCriadoEm() { return criadoEm; }
}

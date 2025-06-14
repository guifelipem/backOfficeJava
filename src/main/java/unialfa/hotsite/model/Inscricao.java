package unialfa.hotsite.model;

import java.time.LocalDateTime;

public class Inscricao {
    private int id;
    private Aluno aluno;
    private Evento evento;
    private LocalDateTime dataInscricao;
    private boolean concluido;

    public Inscricao() {}

    public Inscricao(int id, Aluno aluno, Evento evento, LocalDateTime dataInscricao, boolean concluido) {
        this.id = id;
        this.aluno = aluno;
        this.evento = evento;
        this.dataInscricao = dataInscricao;
        this.concluido = concluido;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Aluno getAluno() {
        return aluno;
    }

    public void setAluno(Aluno aluno) {
        this.aluno = aluno;
    }

    public Evento getEvento() {
        return evento;
    }

    public void setEvento(Evento evento) {
        this.evento = evento;
    }

    public LocalDateTime getDataInscricao() {
        return dataInscricao;
    }

    public void setDataInscricao(LocalDateTime dataInscricao) {
        this.dataInscricao = dataInscricao;
    }

    public boolean isConcluido() {
        return concluido;
    }

    public void setConcluido(boolean concluido) {
        this.concluido = concluido;
    }
}

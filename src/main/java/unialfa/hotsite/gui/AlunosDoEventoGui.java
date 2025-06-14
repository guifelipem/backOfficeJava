package unialfa.hotsite.gui;

import unialfa.hotsite.dao.InscricaoDao;
import unialfa.hotsite.model.Aluno;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.util.List;

public class AlunosDoEventoGui extends JFrame {

    private final InscricaoDao inscricaoDao = new InscricaoDao();
    private final int eventoId;

    public AlunosDoEventoGui(int eventoId) {
        this.eventoId = eventoId;

        setTitle("Alunos do Evento - ID: " + eventoId);
        setSize(600, 400);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        getContentPane().add(montarPainelPrincipal());

        setVisible(true);
    }

    private JScrollPane montarPainelPrincipal() {
        JTable tabelaAlunos = new JTable();
        tabelaAlunos.setDefaultEditor(Object.class, null);
        tabelaAlunos.getTableHeader().setReorderingAllowed(false);

        tabelaAlunos.setModel(carregarAlunos());

        return new JScrollPane(tabelaAlunos);
    }

    private DefaultTableModel carregarAlunos() {
        List<Aluno> alunos = inscricaoDao.listarAlunosPorEvento(eventoId);

        var model = new DefaultTableModel();
        model.addColumn("ID");
        model.addColumn("Nome");
        model.addColumn("Email");
        model.addColumn("Curso");
        model.addColumn("RA");

        alunos.forEach(aluno -> {
            model.addRow(new Object[]{
                    aluno.getId(),
                    aluno.getNome(),
                    aluno.getEmail(),
                    aluno.getCurso(),
                    aluno.getRa()
            });
        });

        return model;
    }
}

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

    // Método responsável por montar o painel principal da interface que exibe os alunos do evento
    // Retorna um JScrollPane contendo a tabela com os dados dos alunos
    private JScrollPane montarPainelPrincipal() {
        JTable tabelaAlunos = new JTable();

        // Impede que o usuário edite diretamente os dados na tabela
        tabelaAlunos.setDefaultEditor(Object.class, null);

        // Impede que o usuário altere a ordem das colunas com o mouse
        tabelaAlunos.getTableHeader().setReorderingAllowed(false);

        // Define o modelo da tabela com os dados dos alunos carregados do banco
        tabelaAlunos.setModel(carregarAlunos());

        // Retorna a tabela dentro de um painel com barra de rolagem
        return new JScrollPane(tabelaAlunos);
    }

    // Método responsável por carregar os alunos inscritos no evento selecionado
// Retorna um DefaultTableModel preenchido com os dados dos alunos
    private DefaultTableModel carregarAlunos() {
        // Obtém a lista de alunos inscritos no evento a partir do DAO
        List<Aluno> alunos = inscricaoDao.listarAlunosPorEvento(eventoId);

        // Cria um novo modelo de tabela
        var model = new DefaultTableModel();

        // Define as colunas que a tabela deve exibir
        model.addColumn("ID");
        model.addColumn("Nome");
        model.addColumn("Email");
        model.addColumn("Curso");
        model.addColumn("RA");

        // Preenche o modelo com os dados de cada aluno da lista
        alunos.forEach(aluno -> {
            model.addRow(new Object[]{
                    aluno.getId(),
                    aluno.getNome(),
                    aluno.getEmail(),
                    aluno.getCurso(),
                    aluno.getRa()
            });
        });

        // Retorna o modelo completo para ser usado pela tabela
        return model;
    }
}

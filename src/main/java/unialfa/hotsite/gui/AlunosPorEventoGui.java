package unialfa.hotsite.gui;

import unialfa.hotsite.service.EventoService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;

public class AlunosPorEventoGui extends JFrame {

    private final EventoService eventoService;
    private JTable tabelaEventos;

    public AlunosPorEventoGui(EventoService eventoService) {
        this.eventoService = eventoService;

        setTitle("Relatório - Alunos por Evento");
        setSize(700, 400);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        getContentPane().add(montarPainelPrincipal());
        setVisible(true);
    }

    // Método responsável por montar o painel principal da interface
    private JPanel montarPainelPrincipal() {
        JPanel painel = new JPanel(new BorderLayout());

        // Adiciona a tabela de eventos no centro do painel
        painel.add(montarPainelTabelaEventos(), BorderLayout.CENTER);

        // Adiciona os botões de ação na parte inferior (sul)
        painel.add(montarPainelBotoes(), BorderLayout.SOUTH);

        return painel;
    }

    // Método que monta a tabela onde os eventos serão exibidos
    private JScrollPane montarPainelTabelaEventos() {
        tabelaEventos = new JTable();

        // Impede que o usuário edite diretamente as células da tabela
        tabelaEventos.setDefaultEditor(Object.class, null);

        // Impede que o usuário reordene as colunas com o mouse
        tabelaEventos.getTableHeader().setReorderingAllowed(false);

        // Define o modelo da tabela com os eventos carregados do banco
        tabelaEventos.setModel(carregarEventos());

        // Retorna a tabela dentro de um painel com barra de rolagem
        return new JScrollPane(tabelaEventos);
    }

    // Método que monta o painel inferior com o botão "Visualizar Alunos"
    private JPanel montarPainelBotoes() {
        JPanel painel = new JPanel();

        JButton btVisualizarAlunos = new JButton("Visualizar Alunos");

        // Define a ação executada ao clicar no botão
        btVisualizarAlunos.addActionListener(this::visualizarAlunos);

        painel.add(btVisualizarAlunos);

        return painel;
    }

    // Método que carrega os eventos no modelo da tabela
    private DefaultTableModel carregarEventos() {
        var model = new DefaultTableModel();

        // Define as colunas da tabela
        model.addColumn("ID");
        model.addColumn("Nome");
        model.addColumn("Data");

        // Preenche as linhas com os dados de cada evento
        eventoService.listar().forEach(evento -> {
            model.addRow(new Object[]{
                    evento.getId(),
                    evento.getNome(),
                    evento.getData()
            });
        });

        return model;
    }

    // Método executado quando o botão "Visualizar Alunos" é clicado
    private void visualizarAlunos(ActionEvent e) {
        // Verifica se alguma linha da tabela foi selecionada
        int selectedRow = tabelaEventos.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Selecione um evento para visualizar os alunos.");
            return;
        }

        // Obtém o ID do evento selecionado
        int eventoId = (int) tabelaEventos.getValueAt(selectedRow, 0);

        // Abre uma nova janela para exibir os alunos do evento
        new AlunosDoEventoGui(eventoId);
    }
}


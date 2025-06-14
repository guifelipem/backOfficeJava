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

    private JPanel montarPainelPrincipal() {
        JPanel painel = new JPanel(new BorderLayout());

        painel.add(montarPainelTabelaEventos(), BorderLayout.CENTER);
        painel.add(montarPainelBotoes(), BorderLayout.SOUTH);

        return painel;
    }

    private JScrollPane montarPainelTabelaEventos() {
        tabelaEventos = new JTable();
        tabelaEventos.setDefaultEditor(Object.class, null);
        tabelaEventos.getTableHeader().setReorderingAllowed(false);

        tabelaEventos.setModel(carregarEventos());

        return new JScrollPane(tabelaEventos);
    }

    private JPanel montarPainelBotoes() {
        JPanel painel = new JPanel();

        JButton btVisualizarAlunos = new JButton("Visualizar Alunos");
        btVisualizarAlunos.addActionListener(this::visualizarAlunos);

        painel.add(btVisualizarAlunos);

        return painel;
    }

    private DefaultTableModel carregarEventos() {
        var model = new DefaultTableModel();
        model.addColumn("ID");
        model.addColumn("Nome");
        model.addColumn("Data");

        eventoService.listar().forEach(evento -> {
            model.addRow(new Object[]{
                    evento.getId(),
                    evento.getNome(),
                    evento.getData()
            });
        });

        return model;
    }

    private void visualizarAlunos(ActionEvent e) {
        int selectedRow = tabelaEventos.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Selecione um evento para visualizar os alunos.");
            return;
        }

        int eventoId = (int) tabelaEventos.getValueAt(selectedRow, 0);
        new AlunosDoEventoGui(eventoId);
    }
}

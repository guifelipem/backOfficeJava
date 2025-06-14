package unialfa.hotsite.gui;

import unialfa.hotsite.model.Palestrante;
import unialfa.hotsite.service.PalestranteService;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;

public class PalestranteGui extends JFrame implements GuiUtil {

    private final PalestranteService palestranteService;

    private JTextField tfNome, tfFotoUrl, tfTema;

    private JTextArea taMiniCurriculo;

    private JTable tabela;

    public PalestranteGui(PalestranteService palestranteService) {
        this.palestranteService = palestranteService;
        setTitle("Gerenciar Palestrante");
        setSize(700, 600);

        getContentPane().add(montarPainelEntrada(), BorderLayout.NORTH);
        getContentPane().add(montarPainelSaida(), BorderLayout.CENTER);

        setLocationRelativeTo(null);
        setVisible(true);
    }

    private JPanel montarPainelEntrada() {
        var jPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc;

        JLabel jlNome = new JLabel("Nome:");
        tfNome = new JTextField(30);

        JLabel jlMiniCurriculo = new JLabel("Mini Currículo:");
        taMiniCurriculo = new JTextArea(5, 30);
        taMiniCurriculo.setLineWrap(true);
        taMiniCurriculo.setWrapStyleWord(true);
        JScrollPane scrollMiniCurriculo = new JScrollPane(taMiniCurriculo);

        JLabel jlFotoUrl = new JLabel("Foto URL:");
        tfFotoUrl = new JTextField(30);

        JLabel jlTema = new JLabel("Tema:");
        tfTema = new JTextField(30);

        JButton btSalvar = new JButton("Salvar");
        btSalvar.addActionListener(this::salvar);

        JButton btAtualizar = new JButton("Atualizar");
        btAtualizar.addActionListener(this::atualizar);

        JButton btExcluir = new JButton("Excluir");
        btExcluir.addActionListener(this::excluir);

        JButton btListar = new JButton("Listar");
        btListar.addActionListener(this::listar);

        JButton btLimpar = new JButton("Limpar Campos");
        btLimpar.addActionListener(this::limparCampos);

        // Linha 1: Nome
        jPanel.add(jlNome, montarGrid(0, 1));
        jPanel.add(tfNome, montarGrid(1, 1));

        // Linha 2: Mini Currículo
        jPanel.add(jlMiniCurriculo, montarGrid(0, 2));
        jPanel.add(scrollMiniCurriculo, montarGrid(1, 2));

        // Linha 3: Foto URL
        jPanel.add(jlFotoUrl, montarGrid(0, 3));
        jPanel.add(tfFotoUrl, montarGrid(1, 3));

        // Linha 4: Tema
        jPanel.add(jlTema, montarGrid(0, 4));
        jPanel.add(tfTema, montarGrid(1, 4));

        // Linha 5: Painel com botões
        JPanel painelBotoes = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        painelBotoes.add(btLimpar);
        painelBotoes.add(btSalvar);
        painelBotoes.add(btAtualizar);
        painelBotoes.add(btExcluir);
        painelBotoes.add(btListar);

        gbc = montarGrid(0, 5);
        gbc.gridwidth = 2; // Ocupa as 2 colunas
        jPanel.add(painelBotoes, gbc);

        return jPanel;
    }

    private JPanel montarPainelSaida() {
        var jPanel = new JPanel(new BorderLayout());

        tabela = new JTable();
        tabela.setDefaultEditor(Object.class, null);
        tabela.getTableHeader().setReorderingAllowed(false);

        tabela.setModel(carregarPalestrantes());

        tabela.getSelectionModel().addListSelectionListener(this::selecionarPalestrante);

        var scrollPanel = new JScrollPane(tabela);
        jPanel.add(scrollPanel, BorderLayout.CENTER);

        return jPanel;
    }

    private DefaultTableModel carregarPalestrantes() {
        var model = new DefaultTableModel();
        model.addColumn("ID");
        model.addColumn("Nome");
        model.addColumn("Mini Currículo");
        model.addColumn("Foto URL");
        model.addColumn("Tema");

        palestranteService.listar().forEach(palestrante -> {
            model.addRow(new Object[]{
                    palestrante.getId(),
                    palestrante.getNome(),
                    palestrante.getMiniCurriculo(),
                    palestrante.getFotoUrl(),
                    palestrante.getTema()
            });
        });

        return model;
    }

    private void listar(ActionEvent actionEvent) {
        listar();
    }

    private void listar() {
        tabela.setModel(carregarPalestrantes());
    }

    private void salvar(ActionEvent e) {
        if (!validarCampos()) return;

        try {
            Palestrante palestrante = new Palestrante();
            palestrante.setNome(tfNome.getText());
            palestrante.setMiniCurriculo(taMiniCurriculo.getText());
            palestrante.setFotoUrl(tfFotoUrl.getText());
            palestrante.setTema(tfTema.getText());

            palestranteService.salvar(palestrante);
            listar();
            limparCampos();

            JOptionPane.showMessageDialog(this, "Palestrante salvo com sucesso!");

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Erro ao salvar: " + ex.getMessage());
        }
    }

    private void atualizar(ActionEvent e) {
        try {
            int selectedRow = tabela.getSelectedRow();
            if (selectedRow == -1) {
                JOptionPane.showMessageDialog(this, "Selecione um palestrante na tabela para atualizar.");
                return;
            }

            if (!validarCampos()) return;

            int id = (int) tabela.getValueAt(selectedRow, 0);

            Palestrante palestrante = new Palestrante();
            palestrante.setId(id);
            palestrante.setNome(tfNome.getText());
            palestrante.setMiniCurriculo(taMiniCurriculo.getText());
            palestrante.setFotoUrl(tfFotoUrl.getText());
            palestrante.setTema(tfTema.getText());

            palestranteService.atualizar(palestrante);
            listar();
            limparCampos();

            JOptionPane.showMessageDialog(this, "Palestrante atualizado com sucesso!");

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Erro ao atualizar: " + ex.getMessage());
        }
    }

    private void excluir(ActionEvent e) {
        try {
            int selectedRow = tabela.getSelectedRow();
            if (selectedRow == -1) {
                JOptionPane.showMessageDialog(this, "Selecione um palestrante na tabela para excluir.");
                return;
            }

            int id = (int) tabela.getValueAt(selectedRow, 0);

            palestranteService.excluir(id);
            listar();
            limparCampos();

            JOptionPane.showMessageDialog(this, "Palestrante excluído com sucesso!");

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Erro ao excluir: " + ex.getMessage());
        }
    }

    private void selecionarPalestrante(ListSelectionEvent listSelectionEvent) {
        int selectedRow = tabela.getSelectedRow();
        if (selectedRow != -1) {
            var nome = (String) tabela.getValueAt(selectedRow, 1);
            var miniCurriculo = (String) tabela.getValueAt(selectedRow, 2);
            var fotoUrl = (String) tabela.getValueAt(selectedRow, 3);
            var tema = (String) tabela.getValueAt(selectedRow, 4);

            limparCampos();

            tfNome.setText(nome);
            taMiniCurriculo.setText(miniCurriculo);
            tfFotoUrl.setText(fotoUrl);
            tfTema.setText(tema);
        }
    }

    private void limparCampos(ActionEvent actionEvent) {
        limparCampos();
    }

    private void limparCampos() {
        tfNome.setText(null);
        taMiniCurriculo.setText(null);
        tfFotoUrl.setText(null);
        tfTema.setText(null);
    }

    private boolean validarCampos() {
        if (tfNome.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "O nome é obrigatório.");
            return false;
        }

        if (taMiniCurriculo.getText().trim().length() < 20) {
            JOptionPane.showMessageDialog(this, "O mini currículo deve ter pelo menos 20 caracteres.");
            return false;
        }

        String url = tfFotoUrl.getText().trim();
        if (!url.isEmpty() && !(url.startsWith("http://") || url.startsWith("https://"))) {
            JOptionPane.showMessageDialog(this, "A URL da foto deve começar com http:// ou https://.");
            return false;
        }

        if (tfTema.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "O tema é obrigatório.");
            return false;
        }

        return true; // tudo certo
    }

}

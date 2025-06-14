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

    // Construtor da interface gráfica para gerenciamento de palestrantes
    public PalestranteGui(PalestranteService palestranteService) {
        this.palestranteService = palestranteService;

        setTitle("Gerenciar Palestrante");
        setSize(700, 600); // Define o tamanho da janela

        // Adiciona o painel de entrada na parte superior da tela
        getContentPane().add(montarPainelEntrada(), BorderLayout.NORTH);

        // Adiciona o painel de saída (com a tabela) no centro da tela
        getContentPane().add(montarPainelSaida(), BorderLayout.CENTER);

        setLocationRelativeTo(null); // Centraliza a janela na tela
        setVisible(true); // Torna a janela visível
    }


    // Método responsável por montar o painel de entrada com os campos do formulário de palestrante
    private JPanel montarPainelEntrada() {
        var jPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc;

        // ===== Campo "Nome" =====
        JLabel jlNome = new JLabel("Nome:");
        tfNome = new JTextField(30);

        // ===== Campo "Mini Currículo" =====
        JLabel jlMiniCurriculo = new JLabel("Mini Currículo:");
        taMiniCurriculo = new JTextArea(5, 30);
        taMiniCurriculo.setLineWrap(true); // Quebra automática de linha
        taMiniCurriculo.setWrapStyleWord(true); // Quebra apenas entre palavras
        JScrollPane scrollMiniCurriculo = new JScrollPane(taMiniCurriculo); // Adiciona barra de rolagem

        // ===== Campo "Foto URL" =====
        JLabel jlFotoUrl = new JLabel("Foto URL:");
        tfFotoUrl = new JTextField(30);

        // ===== Campo "Tema" =====
        JLabel jlTema = new JLabel("Tema:");
        tfTema = new JTextField(30);

        // ===== Botões de Ação =====
        JButton btSalvar = new JButton("Salvar");
        btSalvar.addActionListener(this::salvar); // Aciona o método salvar ao clicar

        JButton btAtualizar = new JButton("Atualizar");
        btAtualizar.addActionListener(this::atualizar);

        JButton btExcluir = new JButton("Excluir");
        btExcluir.addActionListener(this::excluir);

        JButton btListar = new JButton("Listar");
        btListar.addActionListener(this::listar);

        JButton btLimpar = new JButton("Limpar Campos");
        btLimpar.addActionListener(this::limparCampos);

        // ===== Adiciona os componentes ao painel com GridBagLayout =====
        jPanel.add(jlNome, montarGrid(0, 1));
        jPanel.add(tfNome, montarGrid(1, 1));

        jPanel.add(jlMiniCurriculo, montarGrid(0, 2));
        jPanel.add(scrollMiniCurriculo, montarGrid(1, 2));

        jPanel.add(jlFotoUrl, montarGrid(0, 3));
        jPanel.add(tfFotoUrl, montarGrid(1, 3));

        jPanel.add(jlTema, montarGrid(0, 4));
        jPanel.add(tfTema, montarGrid(1, 4));

        // ===== Painel para os botões =====
        JPanel painelBotoes = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        // FlowLayout organiza os botões em linha, centralizados, com espaçamento horizontal de 10px

        painelBotoes.add(btLimpar);
        painelBotoes.add(btSalvar);
        painelBotoes.add(btAtualizar);
        painelBotoes.add(btExcluir);
        painelBotoes.add(btListar);

        // Adiciona o painel de botões na posição (0,5), ocupando duas colunas
        gbc = montarGrid(0, 5);
        gbc.gridwidth = 2;
        jPanel.add(painelBotoes, gbc);

        // Retorna o painel de entrada pronto
        return jPanel;
    }


    // Método responsável por montar o painel de saída contendo a tabela de palestrantes
    private JPanel montarPainelSaida() {
        // Cria um painel com layout de borda (BorderLayout)
        var jPanel = new JPanel(new BorderLayout());

        // Cria a tabela que exibirá os dados dos palestrantes
        tabela = new JTable();

        // Impede que o usuário edite as células da tabela diretamente
        tabela.setDefaultEditor(Object.class, null);

        // Impede que o usuário reordene as colunas da tabela com o mouse
        tabela.getTableHeader().setReorderingAllowed(false);

        // Define o modelo da tabela com os dados dos palestrantes carregados do banco
        tabela.setModel(carregarPalestrantes());

        // Adiciona um ouvinte de seleção que reage quando o usuário seleciona uma linha da tabela
        tabela.getSelectionModel().addListSelectionListener(this::selecionarPalestrante);

        // Adiciona a tabela dentro de um painel com barra de rolagem
        var scrollPanel = new JScrollPane(tabela);
        jPanel.add(scrollPanel, BorderLayout.CENTER);

        // Retorna o painel pronto para ser exibido na interface
        return jPanel;
    }


    // Método responsável por criar e preencher o modelo da tabela com os dados dos palestrantes
    private DefaultTableModel carregarPalestrantes() {
        // Cria um novo modelo de tabela (DefaultTableModel) vazio
        var model = new DefaultTableModel();

        // Define as colunas que serão exibidas na tabela
        model.addColumn("ID");
        model.addColumn("Nome");
        model.addColumn("Mini Currículo");
        model.addColumn("Foto URL");
        model.addColumn("Tema");

        // Percorre a lista de palestrantes retornada pelo serviço e adiciona cada um como uma nova linha na tabela
        palestranteService.listar().forEach(palestrante -> {
            model.addRow(new Object[]{
                    palestrante.getId(),
                    palestrante.getNome(),
                    palestrante.getMiniCurriculo(),
                    palestrante.getFotoUrl(),
                    palestrante.getTema()
            });
        });

        // Retorna o modelo preenchido, que será usado para atualizar a tabela na interface
        return model;
    }

    // Método chamado ao clicar no botão "Listar"
    // Apenas chama o método sem parâmetros que executa a lógica de listagem
    private void listar(ActionEvent actionEvent) {
        listar();
    }

    // Método responsável por carregar os dados dos palestrantes na tabela
    private void listar() {
        // Define o modelo da tabela com os dados carregados do banco
        tabela.setModel(carregarPalestrantes());
    }

    // Método para salvar um novo palestrante com base nos dados do formulário
    private void salvar(ActionEvent e) {

        // Valida os campos do formulário antes de continuar
        if (!validarCampos()) return;

        try {
            // Cria um objeto Palestrante e preenche com os dados informados no formulário
            Palestrante palestrante = new Palestrante();
            palestrante.setNome(tfNome.getText());
            palestrante.setMiniCurriculo(taMiniCurriculo.getText());
            palestrante.setFotoUrl(tfFotoUrl.getText());
            palestrante.setTema(tfTema.getText());

            // Chama o serviço responsável por salvar o palestrante no banco de dados
            palestranteService.salvar(palestrante);

            // Atualiza a tabela e limpa os campos após o salvamento
            listar();
            limparCampos();

            // Exibe mensagem de sucesso
            JOptionPane.showMessageDialog(this, "Palestrante salvo com sucesso!");

        } catch (Exception ex) {
            // Em caso de erro inesperado, exibe a mensagem do erro
            JOptionPane.showMessageDialog(this, "Erro ao salvar: " + ex.getMessage());
        }
    }

    // Método para atualizar os dados de um palestrante selecionado na tabela
    private void atualizar(ActionEvent e) {
        try {
            // Verifica se o usuário selecionou uma linha da tabela
            int selectedRow = tabela.getSelectedRow();
            if (selectedRow == -1) {
                JOptionPane.showMessageDialog(this, "Selecione um palestrante na tabela para atualizar.");
                return;
            }

            // Valida os campos do formulário antes de continuar
            if (!validarCampos()) return;

            // Obtém o ID do palestrante selecionado (coluna 0 da linha selecionada)
            int id = (int) tabela.getValueAt(selectedRow, 0);

            // Cria um objeto Palestrante e preenche com os dados do formulário
            Palestrante palestrante = new Palestrante();
            palestrante.setId(id); // mantém o ID, pois é uma atualização
            palestrante.setNome(tfNome.getText());
            palestrante.setMiniCurriculo(taMiniCurriculo.getText());
            palestrante.setFotoUrl(tfFotoUrl.getText());
            palestrante.setTema(tfTema.getText());

            // Chama o serviço para atualizar o palestrante no banco de dados
            palestranteService.atualizar(palestrante);

            // Atualiza a tabela e limpa os campos do formulário
            listar();
            limparCampos();

            // Mostra mensagem de sucesso
            JOptionPane.showMessageDialog(this, "Palestrante atualizado com sucesso!");

        } catch (Exception ex) {
            // Captura e exibe qualquer erro inesperado
            JOptionPane.showMessageDialog(this, "Erro ao atualizar: " + ex.getMessage());
        }
    }

    // Método responsável por excluir o palestrante selecionado na tabela
    private void excluir(ActionEvent e) {
        try {
            // Verifica se uma linha foi selecionada na tabela
            int selectedRow = tabela.getSelectedRow();
            if (selectedRow == -1) {
                // Exibe mensagem se nenhuma linha estiver selecionada
                JOptionPane.showMessageDialog(this, "Selecione um palestrante na tabela para excluir.");
                return;
            }

            // Obtém o ID do palestrante selecionado na primeira coluna da tabela
            int id = (int) tabela.getValueAt(selectedRow, 0);

            // Chama o serviço para excluir o palestrante com base no ID
            palestranteService.excluir(id);

            // Atualiza a tabela e limpa os campos após a exclusão
            listar();
            limparCampos();

            // Exibe mensagem de sucesso
            JOptionPane.showMessageDialog(this, "Palestrante excluído com sucesso!");

        } catch (Exception ex) {
            // Em caso de erro inesperado, exibe a mensagem do erro
            JOptionPane.showMessageDialog(this, "Erro ao excluir: " + ex.getMessage());
        }
    }

    // Método chamado automaticamente quando o usuário seleciona uma linha da tabela de palestrantes.
    // Ele preenche o formulário com os dados da linha selecionada, permitindo edição ou exclusão.
    private void selecionarPalestrante(ListSelectionEvent listSelectionEvent) {

        // Obtém o índice da linha selecionada na tabela.
        // Se nenhuma linha estiver selecionada, o valor será -1.
        int selectedRow = tabela.getSelectedRow();

        // Executa apenas se uma linha estiver de fato selecionada
        if (selectedRow != -1) {

            // Recupera os dados das colunas da linha selecionada e armazena em variáveis locais
            var nome = (String) tabela.getValueAt(selectedRow, 1);
            var miniCurriculo = (String) tabela.getValueAt(selectedRow, 2);
            var fotoUrl = (String) tabela.getValueAt(selectedRow, 3);
            var tema = (String) tabela.getValueAt(selectedRow, 4);

            // Limpa os campos do formulário antes de preenchê-los
            limparCampos();

            // Preenche os campos do formulário com os dados obtidos da tabela
            tfNome.setText(nome);
            taMiniCurriculo.setText(miniCurriculo);
            tfFotoUrl.setText(fotoUrl);
            tfTema.setText(tema);
        }
    }

    // Método que chama a versão sem parâmetros para limpar os campos do formulário
    // É usado quando o botão "Limpar" é clicado
    private void limparCampos(ActionEvent actionEvent) {
        limparCampos();
    }

    // Método que limpa todos os campos do formulário de palestrante
    private void limparCampos() {
        // Limpa o campo de texto do nome
        tfNome.setText(null);

        // Limpa o campo do mini currículo
        taMiniCurriculo.setText(null);

        // Limpa o campo da URL da foto
        tfFotoUrl.setText(null);

        // Limpa o campo do tema
        tfTema.setText(null);
    }

    // Método responsável por validar os campos do formulário antes de salvar ou atualizar um palestrante
    private boolean validarCampos() {

        // Verifica se o campo "Nome" está vazio
        if (tfNome.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "O nome é obrigatório.");
            return false;
        }

        // Verifica se o mini currículo possui pelo menos 20 caracteres
        if (taMiniCurriculo.getText().trim().length() < 20) {
            JOptionPane.showMessageDialog(this, "O mini currículo deve ter pelo menos 20 caracteres.");
            return false;
        }

        // Verifica se o campo da URL da foto não está vazio e se inicia com http:// ou https://
        String url = tfFotoUrl.getText().trim();
        if (!url.isEmpty() && !(url.startsWith("http://") || url.startsWith("https://"))) {
            JOptionPane.showMessageDialog(this, "A URL da foto deve começar com http:// ou https://.");
            return false;
        }

        // Verifica se o campo "Tema" está vazio
        if (tfTema.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "O tema é obrigatório.");
            return false;
        }

        // Se todas as validações forem aprovadas, retorna true
        return true;
    }


}

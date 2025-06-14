package unialfa.hotsite.gui;

import unialfa.hotsite.dao.CoordenadorDao;
import unialfa.hotsite.model.Coordenador;
import unialfa.hotsite.model.Evento;
import unialfa.hotsite.model.Palestrante;
import unialfa.hotsite.service.EventoService;
import unialfa.hotsite.service.PalestranteService;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.time.LocalDate;
import java.time.LocalTime;

public class EventoGui extends JFrame implements GuiUtil {

    private final EventoService eventoService;
    private final CoordenadorDao coordenadorDao = new CoordenadorDao();
    private final PalestranteService palestranteService = new PalestranteService();

    private JTextField tfNome, tfData, tfHoraInicio, tfHoraFim;

    private JTextArea taDescricao;

    private JComboBox<Coordenador> cbCoordenador;
    // JComboBox é um componente gráfico que exibe uma lista suspensa de opções.
    // O usuário pode selecionar apenas uma opção da lista.

    private JComboBox<Palestrante> cbPalestrante;

    private JTable tabela;

    public EventoGui(EventoService eventoService) {
        this.eventoService = eventoService;
        setTitle("Gerenciar Eventos");
        setSize(700, 600);

        getContentPane().add(montarPainelEntrada(), BorderLayout.NORTH);
        // BorderLayout é um gerenciador de layout que organiza os componentes em cinco regiões: Norte, Sul, Leste, Oeste e Centro.
        getContentPane().add(montarPainelSaida(), BorderLayout.CENTER);

        setLocationRelativeTo(null);
        setVisible(true);
    }

    private JPanel montarPainelEntrada() {
        // ===== Campos de entrada =====
        var jPanel = new JPanel(new GridBagLayout());

        JLabel jlNome = new JLabel("Nome:");
        tfNome = new JTextField(30);

        JLabel jlDescricao = new JLabel("Descrição:");
        taDescricao = new JTextArea(3, 30);
        taDescricao.setLineWrap(true); // quebra automatica de linha do textArea
        taDescricao.setWrapStyleWord(true); // melhora a quebra de linha, quebrando apenas entre palavras
        JScrollPane scrollDescricao = new JScrollPane(taDescricao); // envolve a textArea com um componente de rolagem

        JLabel jlData = new JLabel("Data (AAAA-MM-DD):");
        tfData = new JTextField(10);

        JLabel jlHoraInicio = new JLabel("Hora Início (HH:MM):");
        tfHoraInicio = new JTextField(10);

        JLabel jlHoraFim = new JLabel("Hora Fim (HH:MM):");
        tfHoraFim = new JTextField(10);

        JLabel jlCoordenador = new JLabel("Coordenador:");
        cbCoordenador = new JComboBox<>();
        carregarCoordenadores();

        JLabel jlPalestrante = new JLabel("Palestrante:");
        cbPalestrante = new JComboBox<>();
        carregarPalestrantes();

        JButton btSalvar = new JButton("Salvar");
        btSalvar.addActionListener(this::salvarEvento);

        JButton btListar = new JButton("Listar Eventos");
        btListar.addActionListener(this::listarEventos);

        JButton btLimpar = new JButton("Limpar Campos");
        btLimpar.addActionListener(this::limparCampos);

        JButton btAtualizar = new JButton("Atualizar");
        btAtualizar.addActionListener(this::atualizarEvento);

        JButton btExcluir = new JButton("Excluir");
        btExcluir.addActionListener(this::excluirEvento);

        jPanel.add(jlNome, montarGrid(0, 1));
        jPanel.add(tfNome, montarGrid(1, 1));

        jPanel.add(jlDescricao, montarGrid(0, 2));
        jPanel.add(scrollDescricao, montarGrid(1, 2));

        jPanel.add(jlData, montarGrid(0, 3));
        jPanel.add(tfData, montarGrid(1, 3));

        jPanel.add(jlHoraInicio, montarGrid(0, 4));
        jPanel.add(tfHoraInicio, montarGrid(1, 4));

        jPanel.add(jlHoraFim, montarGrid(0, 5));
        jPanel.add(tfHoraFim, montarGrid(1, 5));

        jPanel.add(jlCoordenador, montarGrid(0, 6));
        jPanel.add(cbCoordenador, montarGrid(1, 6));

        jPanel.add(jlPalestrante, montarGrid(0, 7));
        jPanel.add(cbPalestrante, montarGrid(1, 7));


        JPanel painelBotoes = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        // FlowLayout é um gerenciador de layout que organiza os componentes em linha, da esquerda para a direita, como num fluxo de texto.
        // (FlowLayout.CENTER, 10, 0) Cria um painel com FlowLayout centralizado.
        // Os botões ficarão alinhados no centro, com 10px de espaço entre eles na horizontal e 0px na vertical.

        painelBotoes.add(btLimpar);
        painelBotoes.add(btSalvar);
        painelBotoes.add(btAtualizar);
        painelBotoes.add(btExcluir);
        painelBotoes.add(btListar);

        GridBagConstraints gbc;
        gbc = montarGrid(0, 8);
        gbc.gridwidth = 2;
        jPanel.add(painelBotoes, gbc);
        // Usa GridBagConstraints para adicionar o painel de botões na posição (0,8) da grade.
        // O gridwidth = 2 faz com que o painel ocupe duas colunas, permitindo que os botões fiquem centralizados na tela.

        return jPanel;
    }

    private JPanel montarPainelSaida() {
        var jPanel = new JPanel(new BorderLayout());

        tabela = new JTable();
        tabela.setDefaultEditor(Object.class, null);
        // Impede que o usuário edite as células da tabela diretamente ao clicar nelas
        tabela.getTableHeader().setReorderingAllowed(false);
        // Impede que o usuário reordene as colunas da tabela com o mouse

        tabela.setModel(carregarEventos());
        // Define o modelo da tabela com os dados dos eventos carregados do banco

        tabela.getSelectionModel().addListSelectionListener(this::selecionarEvento);

        var scrollPanel = new JScrollPane(tabela);
        jPanel.add(scrollPanel, BorderLayout.CENTER);

        return jPanel;
    }

    // Método responsável por montar e retornar o modelo de dados da tabela de eventos
    private DefaultTableModel carregarEventos() {
        // Cria e retorna um modelo de tabela com os dados dos eventos
        // Cada linha representa um evento e exibe as suas informações principais

        var model = new DefaultTableModel();

        // Define os nomes das colunas que aparecerão no cabeçalho da tabela
        model.addColumn("ID");
        model.addColumn("Nome");
        model.addColumn("Descrição");
        model.addColumn("Data");
        model.addColumn("Hora Início");
        model.addColumn("Hora Fim");
        model.addColumn("Coordenador");
        model.addColumn("Palestrante");

        // Percorre a lista de eventos obtida do serviço e adiciona cada um como uma linha na tabela
        eventoService.listar().forEach(evento -> {
            model.addRow(new Object[]{
                    evento.getId(),
                    evento.getNome(),
                    evento.getDescricao(),
                    evento.getData(),
                    evento.getHorarioInicio(),
                    evento.getHorarioFim(),
                    evento.getCoordenador().getNome(),   // obtém o nome do coordenador associado
                    evento.getPalestrante().getNome()    // obtém o nome do palestrante associado
            });
        });

        // Retorna o modelo de dados pronto para ser usado na JTable
        return model;
    }

    // Método que pode ser associado diretamente a botões ou menus (ActionListener)
    // Ele apenas chama o método abaixo, sem lógica adicional
    private void listarEventos(ActionEvent actionEvent) {
        listarEventos();
    }

    // Atualiza os dados da tabela, substituindo o modelo atual pelo modelo carregado
    private void listarEventos() {
        tabela.setModel(carregarEventos());
    }


    // Método responsável por salvar um novo evento no sistema
    private void salvarEvento(ActionEvent actionEvent) {

        // Valida todos os campos obrigatórios do formulário
        // Se houver erro, a função retorna e o salvamento é cancelado
        if (!validarCamposEvento()) return;

        // Cria um novo objeto Evento e preenche com os dados informados
        var evento = new Evento();

        evento.setNome(tfNome.getText());
        evento.setDescricao(taDescricao.getText());

        try {
            // Conversão protegida para garantir que a data e a hora sejam válidas
            // Mesmo que os campos passem na validação, a conversão pode falhar com valores inválidos (ex: 2024-13-01)
            evento.setData(LocalDate.parse(tfData.getText()));
            evento.setHorarioInicio(LocalTime.parse(tfHoraInicio.getText()));
            evento.setHorarioFim(LocalTime.parse(tfHoraFim.getText()));
        } catch (Exception e) {
            // Exibe mensagem de erro se o formato da data ou hora for inválido
            JOptionPane.showMessageDialog(this, "Formato inválido de data ou hora.\n" +
                    "Data deve ser AAAA-MM-DD.\n" +
                    "Hora deve ser HH:MM.", "Erro de formatação", JOptionPane.ERROR_MESSAGE);
            return;  // Interrompe o processo de salvamento
        }

        // Obtém os itens selecionados nos ComboBoxes e associa ao evento
        var coordenadorSelecionado = (Coordenador) cbCoordenador.getSelectedItem();
        var palestranteSelecionado = (Palestrante) cbPalestrante.getSelectedItem();
        evento.setCoordenador(coordenadorSelecionado);
        evento.setPalestrante(palestranteSelecionado);

        // Salva o evento no banco de dados por meio do serviço
        eventoService.salvar(evento);

        // Limpa os campos do formulário após o salvamento
        limparCampos();

        // Exibe mensagem de sucesso
        JOptionPane.showMessageDialog(this, "Evento salvo com sucesso!");

        // Atualiza a tabela com os novos dados
        tabela.setModel(carregarEventos());
    }

    // Método chamado automaticamente quando o usuário seleciona uma linha da tabela de eventos.
    // Ele preenche o formulário com os dados da linha selecionada, permitindo edição ou exclusão.
    private void selecionarEvento(ListSelectionEvent listSelectionEvent) {

        // Obtém o índice da linha selecionada na tabela.
        // Se nenhuma linha estiver selecionada, o valor será -1.
        int selectedRow = tabela.getSelectedRow();

        // Executa apenas se uma linha estiver de fato selecionada
        if (selectedRow != -1) {

            // Recupera os dados das colunas da linha selecionada e armazena em variáveis locais
            var nome = (String) tabela.getValueAt(selectedRow, 1);
            var descricao = (String) tabela.getValueAt(selectedRow, 2);
            var data = tabela.getValueAt(selectedRow, 3).toString();
            var horaInicio = tabela.getValueAt(selectedRow, 4).toString();
            var horaFim = tabela.getValueAt(selectedRow, 5).toString();

            // Limpa os campos do formulário antes de preenchê-los
            limparCampos();

            // Preenche os campos do formulário com os dados obtidos da tabela
            tfNome.setText(nome);
            taDescricao.setText(descricao);
            tfData.setText(data);
            tfHoraInicio.setText(horaInicio);
            tfHoraFim.setText(horaFim);

            // === Seleciona o coordenador correspondente no ComboBox ===

            // Obtém o nome do coordenador da coluna da tabela
            String coordenadorNome = tabela.getValueAt(selectedRow, 6).toString();

            // Percorre os itens do ComboBox de coordenadores
            for (int i = 0; i < cbCoordenador.getItemCount(); i++) {
                Coordenador c = cbCoordenador.getItemAt(i);

                // Compara o nome do coordenador no ComboBox com o nome obtido da tabela
                if (c.getNome().equals(coordenadorNome)) {
                    // Seleciona o coordenador correspondente
                    cbCoordenador.setSelectedIndex(i);
                    break;
                }
            }

            // === Seleciona o palestrante correspondente no ComboBox ===

            // Obtém o nome do palestrante da coluna da tabela
            String palestranteNome = tabela.getValueAt(selectedRow, 7).toString();

            // Percorre os itens do ComboBox de palestrantes
            for (int i = 0; i < cbPalestrante.getItemCount(); i++) {
                Palestrante p = cbPalestrante.getItemAt(i);

                // Compara o nome do palestrante no ComboBox com o nome da tabela
                if (p.getNome().equals(palestranteNome)) {
                    // Seleciona o palestrante correspondente
                    cbPalestrante.setSelectedIndex(i);
                    break;
                }
            }
        }
    }


    // Método para atualizar os dados de um evento selecionado na tabela
    private void atualizarEvento(ActionEvent e) {

        try {
            int selectedRow = tabela.getSelectedRow();
            // Verifica se o usuário selecionou uma linha da tabela
            if (selectedRow == -1) {
                JOptionPane.showMessageDialog(this, "Selecione um evento na tabela para atualizar.");
                return;
            }

            // Valida os campos do formulário antes de continuar
            if (!validarCamposEvento()) return;

            // Obtém o ID do evento selecionado (coluna 0 da linha selecionada)
            int id = (int) tabela.getValueAt(selectedRow, 0);

            // Cria um objeto Evento e preenche com os dados do formulário
            Evento evento = new Evento();
            evento.setId(id); // mantém o ID, pois é uma atualização
            evento.setNome(tfNome.getText());
            evento.setDescricao(taDescricao.getText());

            // Tenta converter os campos de data e hora para os tipos corretos
            try {
                evento.setData(LocalDate.parse(tfData.getText()));
                evento.setHorarioInicio(LocalTime.parse(tfHoraInicio.getText()));
                evento.setHorarioFim(LocalTime.parse(tfHoraFim.getText()));
            } catch (Exception ex) {
                // Mostra mensagem de erro se a data ou hora estiverem em formato inválido
                JOptionPane.showMessageDialog(this, "Formato inválido de data ou hora.\n" +
                        "Data deve ser AAAA-MM-DD.\n" +
                        "Hora deve ser HH:MM.", "Erro de formatação", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Obtém os itens selecionados nos ComboBox e associa ao evento
            var coordenadorSelecionado = (Coordenador) cbCoordenador.getSelectedItem();
            var palestranteSelecionado = (Palestrante) cbPalestrante.getSelectedItem();

            evento.setCoordenador(coordenadorSelecionado);
            evento.setPalestrante(palestranteSelecionado);

            // Chama o serviço para atualizar o evento no banco de dados
            eventoService.atualizar(evento);

            // Atualiza a tabela e limpa os campos do formulário
            listarEventos();
            limparCampos();

            // Mostra mensagem de sucesso
            JOptionPane.showMessageDialog(this, "Evento atualizado com sucesso!");

        } catch (Exception ex) {
            // Captura e exibe qualquer erro inesperado
            JOptionPane.showMessageDialog(this, "Erro ao atualizar: " + ex.getMessage());
        }
    }

    // Método responsável por excluir o evento selecionado na tabela
    private void excluirEvento(ActionEvent e) {
        try {
            // Verifica se uma linha foi selecionada na tabela
            int selectedRow = tabela.getSelectedRow();
            if (selectedRow == -1) {
                // Exibe mensagem se nenhuma linha estiver selecionada
                JOptionPane.showMessageDialog(this, "Selecione um evento na tabela para excluir.");
                return;
            }

            // Obtém o ID do evento selecionado na primeira coluna da tabela
            int id = (int) tabela.getValueAt(selectedRow, 0);

            // Chama o serviço para excluir o evento com base no ID
            eventoService.excluir(id);

            // Atualiza a tabela e limpa os campos após a exclusão
            listarEventos();
            limparCampos();

            // Exibe mensagem de sucesso
            JOptionPane.showMessageDialog(this, "Evento excluído com sucesso!");

        } catch (Exception ex) {
            // Em caso de erro inesperado, exibe a mensagem do erro
            JOptionPane.showMessageDialog(this, "Erro ao excluir: " + ex.getMessage());
        }
    }

    // Método responsável por carregar os coordenadores no ComboBox
    private void carregarCoordenadores() {
        // Remove todos os itens existentes do ComboBox para evitar duplicações
        cbCoordenador.removeAllItems();

        // Busca a lista de coordenadores no banco e adiciona cada um ao ComboBox
        coordenadorDao.listar().forEach(cbCoordenador::addItem);
    }

    // Método responsável por carregar os palestrantes no ComboBox
    private void carregarPalestrantes() {
        // Remove todos os itens existentes do ComboBox para evitar duplicações
        cbPalestrante.removeAllItems();

        // Busca a lista de palestrantes no banco e adiciona cada um ao ComboBox
        palestranteService.listar().forEach(cbPalestrante::addItem);
    }


    // Método que chama a versão sem parâmetros para limpar os campos do formulário
// É usado quando o botão "Limpar" é clicado
    private void limparCampos(ActionEvent actionEvent) {
        limparCampos();
    }

    // Método que limpa todos os campos do formulário de evento
    private void limparCampos() {
        // Limpa o campo de texto do nome
        tfNome.setText(null);

        // Limpa o campo de descrição
        taDescricao.setText(null);

        // Limpa o campo de data
        tfData.setText(null);

        // Limpa os campos de horário
        tfHoraInicio.setText(null);
        tfHoraFim.setText(null);

        // Desseleciona qualquer item nos ComboBoxes
        cbCoordenador.setSelectedIndex(-1);
        cbPalestrante.setSelectedIndex(-1);
    }

    // Método responsável por validar os campos do formulário antes de salvar ou atualizar um evento
    private boolean validarCamposEvento() {

        // Verifica se o campo "Nome" está vazio
        if (tfNome.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "O nome do evento é obrigatório.");
            return false;
        }

        // Verifica se a descrição possui pelo menos 10 caracteres
        if (taDescricao.getText().trim().length() < 10) {
            JOptionPane.showMessageDialog(this, "A descrição deve conter pelo menos 10 caracteres.");
            return false;
        }

        // Verifica se o campo "Data" está vazio
        if (tfData.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "A data do evento é obrigatória.");
            return false;
        }

        // Verifica se o campo "Hora de Início" está vazio
        if (tfHoraInicio.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "O horário de início é obrigatório.");
            return false;
        }

        // Verifica se o campo "Hora de Término" está vazio
        if (tfHoraFim.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "O horário de término é obrigatório.");
            return false;
        }

        // Verifica se um coordenador foi selecionado
        if (cbCoordenador.getSelectedItem() == null) {
            JOptionPane.showMessageDialog(this, "Selecione um coordenador.");
            return false;
        }

        // Verifica se um palestrante foi selecionado
        if (cbPalestrante.getSelectedItem() == null) {
            JOptionPane.showMessageDialog(this, "Selecione um palestrante.");
            return false;
        }

        try {
            // Tenta converter o texto da data para LocalDate e verifica se é uma data futura ou atual
            LocalDate dataEvento = LocalDate.parse(tfData.getText().trim());
            if (dataEvento.isBefore(LocalDate.now())) {
                JOptionPane.showMessageDialog(this, "A data do evento não pode ser anterior à data atual.");
                return false;
            }
        } catch (Exception e) {
            // Caso a data esteja em formato inválido
            JOptionPane.showMessageDialog(this, "Formato inválido de data. Use o padrão AAAA-MM-DD.");
            return false;
        }

        try {
            // Converte os campos de hora para LocalTime e valida a ordem cronológica
            LocalTime horaInicio = LocalTime.parse(tfHoraInicio.getText().trim());
            LocalTime horaFim = LocalTime.parse(tfHoraFim.getText().trim());

            if (horaFim.isBefore(horaInicio) || horaFim.equals(horaInicio)) {
                JOptionPane.showMessageDialog(this, "O horário de término deve ser depois do horário de início.");
                return false;
            }

        } catch (Exception e) {
            // Caso o horário esteja em formato inválido
            JOptionPane.showMessageDialog(this, "Formato inválido de hora. Use o padrão HH:MM.");
            return false;
        }

        // Se todas as validações forem aprovadas, retorna true
        return true;
    }

}

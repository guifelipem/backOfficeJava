package unialfa.hotsite.gui;

import unialfa.hotsite.service.EventoService;
import unialfa.hotsite.service.PalestranteService;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class MenuGui extends JFrame implements GuiUtil {

    public MenuGui() {
        setTitle("Menu Principal");
        setSize(400, 180);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setJMenuBar(montarMenuBar());
    }

    private JMenuBar montarMenuBar() {
        JMenuBar menuBar = new JMenuBar();
        menuBar.add(montarMenuGerenciar());
        menuBar.add(montarMenuRelatorios());
        menuBar.add(montarMenuSobre());
        return menuBar;
    }

    private JMenu montarMenuGerenciar() {
        var menuGerenciar = new JMenu("Gerenciar");
        var menuItemEvento = new JMenuItem("Eventos");
        var menuItemPalestrante = new JMenuItem("Palestrantes");

        menuItemEvento.setToolTipText("Abrir tela de gerenciamento de eventos"); // função que faz aparecer um texto ao passar o mouse em cima do botão
        menuItemPalestrante.setToolTipText("Abrir tela de gerenciamento de palestrantes");

        menuGerenciar.add(menuItemEvento);
        menuGerenciar.add(menuItemPalestrante);

        menuItemEvento.addActionListener(this::abrirEventoGui);
        menuItemPalestrante.addActionListener(this::abrirPalestranteGui);

        menuGerenciar.setFont(new Font("Arial",Font.PLAIN,20)); // font.PLAIN -> texto normal, sem negrito ou italico
        menuItemEvento.setFont(new Font("Arial",Font.PLAIN,18));
        menuItemPalestrante.setFont(new Font("Arial",Font.PLAIN,18));

        return menuGerenciar;
    }

    private JMenu montarMenuRelatorios() {
        var menuRelatorios = new JMenu("Relatórios");
        var menuItemRelatorioAluno = new JMenuItem("Alunos por Evento");
        menuItemRelatorioAluno.setToolTipText("Abrir tela de aluno por evento");

        menuRelatorios.add(menuItemRelatorioAluno);

        menuItemRelatorioAluno.addActionListener(this::abrirAlunosPorEvento);

        menuRelatorios.setFont(new Font("Arial",Font.PLAIN,20));
        menuItemRelatorioAluno.setFont(new Font("Arial",Font.PLAIN,18));

        return menuRelatorios;
    }

    private JMenu montarMenuSobre() {
        var menuSobre = new JMenu("Sobre");
        var menuItemEquipe = new JMenuItem("Equipe");
        menuItemEquipe.setToolTipText("Abrir integrantes da equipe de desenvolvimento");

        menuSobre.add(menuItemEquipe);

        menuItemEquipe.addActionListener(this::exibirEquipe);

        menuSobre.setFont(new Font("Arial",Font.PLAIN,20));
        menuItemEquipe.setFont(new Font("Arial",Font.PLAIN,18));

        return menuSobre;
    }

    private void abrirEventoGui(ActionEvent actionEvent) {
        var gui = new EventoGui(new EventoService());
        gui.setVisible(true);
    }

    private void abrirPalestranteGui(ActionEvent actionEvent) {
        var gui = new PalestranteGui(new PalestranteService());
        gui.setVisible(true);
    }

    private void abrirAlunosPorEvento(ActionEvent actionEvent) {
        var gui = new AlunosPorEventoGui(new EventoService());
        gui.setVisible(true);
    }

    private void exibirEquipe(ActionEvent actionEvent) {
        JOptionPane.showMessageDialog(this, """
                  Equipe 3 - Hackathon:
                    Andrei Matos Costa
                    Eduardo Vergentino Malaquias
                    Guilherme Felipe De Morais
                    Josue Wellyngtton Marcal De Souza
                    Rafael Colombo Da Silva
                """, "Equipe de Desenvolvimento", JOptionPane.INFORMATION_MESSAGE);
    }
}

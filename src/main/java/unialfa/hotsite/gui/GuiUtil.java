package unialfa.hotsite.gui;

import java.awt.*;

// Interface utilitária para componentes gráficos (GUI)
// Pode ser implementada por classes que montam interfaces usando GridBagLayout
public interface GuiUtil {

    // Método utilitário padrão (default) que cria e retorna uma instância de GridBagConstraints
    // Serve para posicionar componentes em um GridBagLayout, facilitando o reaproveitamento de código
    default GridBagConstraints montarGrid(int coluna, int linha) {
        var constraints = new GridBagConstraints();

        // Define uma margem (espaçamento) de 5px em todos os lados do componente
        constraints.insets = new Insets(5, 5, 5, 5);

        // Define a posição da coluna (eixo X) no layout
        constraints.gridx = coluna;

        // Define a posição da linha (eixo Y) no layout
        constraints.gridy = linha;

        // Retorna o objeto configurado, pronto para ser usado ao adicionar um componente
        return constraints;
    }
}

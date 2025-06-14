package unialfa.hotsite.dao;

import unialfa.hotsite.model.Coordenador;

import java.util.ArrayList;
import java.util.List;

// Classe responsável por acessar os dados dos coordenadores no banco de dados
// Herda funcionalidades da classe Dao, como a obtenção da conexão com o banco
public class CoordenadorDao extends Dao {

    // Método que retorna uma lista com todos os coordenadores cadastrados no banco de dados
    public List<Coordenador> listar() {
        // Cria uma lista vazia para armazenar os coordenadores encontrados
        List<Coordenador> coordenadores = new ArrayList<>();

        // SQL para buscar os campos id, nome e email da tabela de coordenadores
        String sql = "SELECT id, nome, email FROM coordenadores";

        try {
            // Executa a consulta no banco de dados e obtém o resultado
            var resultSet = getConnection().prepareStatement(sql).executeQuery();

            // Percorre cada linha do resultado
            while (resultSet.next()) {
                // Cria um objeto Coordenador e preenche com os dados da linha atual
                var coordenador = new Coordenador();

                coordenador.setId(resultSet.getInt("id"));         // Obtém o ID
                coordenador.setNome(resultSet.getString("nome"));  // Obtém o nome
                coordenador.setEmail(resultSet.getString("email"));// Obtém o e-mail

                // Adiciona o coordenador à lista
                coordenadores.add(coordenador);
            }

            // Fecha o resultSet para liberar recursos
            resultSet.close();

        } catch (Exception e) {
            // Em caso de erro, imprime a mensagem no console
            System.out.println(e.getMessage());
        }

        // Retorna uma nova lista contendo os coordenadores encontrados
        return new ArrayList<Coordenador>(coordenadores);
    }
}


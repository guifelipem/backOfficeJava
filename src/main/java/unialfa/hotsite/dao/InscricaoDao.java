package unialfa.hotsite.dao;

import unialfa.hotsite.model.Aluno;

import java.util.ArrayList;
import java.util.List;

// Classe responsável por acessar os dados de inscrições no banco de dados
// Estende a classe Dao para obter a conexão com o banco
public class InscricaoDao extends Dao {

    // Método que retorna a lista de alunos inscritos em um evento específico
    public List<Aluno> listarAlunosPorEvento(int eventoId) {
        // Cria uma lista vazia para armazenar os alunos encontrados
        List<Aluno> alunos = new ArrayList<>();

        // SQL com JOIN entre a tabela de inscrições e a tabela de alunos
        // Retorna os dados dos alunos vinculados ao evento informado
        String sql = "SELECT a.id, a.nome, a.email, a.curso, a.ra " +
                "FROM inscricoes i " +
                "JOIN alunos a ON i.aluno_id = a.id " +
                "WHERE i.evento_id = ?";

        try {
            // Prepara o comando SQL e define o ID do evento como parâmetro
            var ps = getConnection().prepareStatement(sql);
            ps.setInt(1, eventoId); // Define o valor do parâmetro evento_id

            // Executa a consulta e obtém os resultados
            var rs = ps.executeQuery();

            // Percorre os resultados e cria objetos Aluno com os dados retornados
            while (rs.next()) {
                var aluno = new Aluno();

                aluno.setId(rs.getInt("id"));
                aluno.setNome(rs.getString("nome"));
                aluno.setEmail(rs.getString("email"));
                aluno.setCurso(rs.getString("curso"));
                aluno.setRa(rs.getInt("ra"));

                // Adiciona o aluno à lista
                alunos.add(aluno);
            }

            // Fecha os recursos para liberar memória
            rs.close();
            ps.close();

        } catch (Exception e) {
            // Em caso de erro, imprime a mensagem no console
            System.out.println(e.getMessage());
        }

        // Retorna a lista de alunos inscritos no evento
        return new ArrayList<>(alunos);
    }
}


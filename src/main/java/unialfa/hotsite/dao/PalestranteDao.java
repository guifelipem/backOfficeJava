package unialfa.hotsite.dao;

import unialfa.hotsite.model.Palestrante;

import java.util.ArrayList;
import java.util.List;

// Classe responsável por executar operações no banco de dados relacionadas à entidade Palestrante
// Estende a classe Dao (para acesso à conexão) e implementa a interface DaoInterface
public class PalestranteDao extends Dao implements DaoInterface {

    // Método para salvar um novo palestrante no banco de dados
    @Override
    public boolean salvar(Object entity) {
        try {
            // Faz o cast do objeto genérico para Palestrante
            var palestrante = (Palestrante) entity;

            // Comando SQL para inserir um novo palestrante
            String sqlInsert = "INSERT INTO palestrantes (nome, minicurriculo, foto_url, tema) VALUES (?, ?, ?, ?)";

            // Prepara a instrução e define os parâmetros com os dados do objeto
            var ps = getConnection().prepareStatement(sqlInsert);
            ps.setString(1, palestrante.getNome());
            ps.setString(2, palestrante.getMiniCurriculo());
            ps.setString(3, palestrante.getFotoUrl());
            ps.setString(4, palestrante.getTema());

            // Executa a instrução e fecha o PreparedStatement
            ps.execute();
            ps.close();

            return true; // Retorna true se a inserção foi bem-sucedida

        } catch (Exception e) {
            // Em caso de erro, exibe a mensagem no console
            System.out.println(e.getMessage());
            return false;
        }
    }

    // Método que retorna uma lista com todos os palestrantes cadastrados no banco
    @Override
    public List<Palestrante> listar() {
        List<Palestrante> palestrantes = new ArrayList<>();

        // Comando SQL para selecionar todos os campos da tabela de palestrantes
        String sqlSelect = "SELECT id, nome, minicurriculo, foto_url, tema FROM palestrantes";

        try {
            // Executa a consulta SQL
            var resultSet = getConnection().prepareStatement(sqlSelect).executeQuery();

            // Percorre os resultados e cria objetos Palestrante
            while (resultSet.next()) {
                var palestrante = new Palestrante();

                palestrante.setId(resultSet.getInt("id"));
                palestrante.setNome(resultSet.getString("nome"));
                palestrante.setMiniCurriculo(resultSet.getString("minicurriculo"));
                palestrante.setFotoUrl(resultSet.getString("foto_url"));
                palestrante.setTema(resultSet.getString("tema"));

                // Adiciona o palestrante à lista
                palestrantes.add(palestrante);
            }

            // Fecha o resultSet após uso
            resultSet.close();

        } catch (Exception e) {
            // Em caso de erro, imprime a mensagem
            System.out.println(e.getMessage());
        }

        // Retorna a lista com os palestrantes encontrados
        return new ArrayList<Palestrante>(palestrantes);
    }

    // Método para atualizar os dados de um palestrante existente no banco
    @Override
    public boolean atualizar(Object entity) {
        try {
            // Faz o cast do objeto genérico para Palestrante
            var palestrante = (Palestrante) entity;

            // Comando SQL para atualizar os dados com base no ID
            String sqlUpdate = "UPDATE palestrantes SET nome=?, minicurriculo=?, foto_url=?, tema=? WHERE id=?";

            var ps = getConnection().prepareStatement(sqlUpdate);
            ps.setString(1, palestrante.getNome());
            ps.setString(2, palestrante.getMiniCurriculo());
            ps.setString(3, palestrante.getFotoUrl());
            ps.setString(4, palestrante.getTema());
            ps.setInt(5, palestrante.getId());

            // Executa a atualização e fecha o PreparedStatement
            ps.execute();
            ps.close();

            return true; // Retorna true se a operação foi bem-sucedida

        } catch (Exception e) {
            System.out.println(e.getMessage());
            return false;
        }
    }

    // Método para excluir um palestrante do banco de dados com base no ID
    @Override
    public boolean excluir(int id) {
        try {
            // Comando SQL para exclusão
            String sqlDelete = "DELETE FROM palestrantes WHERE id=?";

            var ps = getConnection().prepareStatement(sqlDelete);
            ps.setInt(1, id); // Define o ID do palestrante a ser excluído

            // Executa o comando e fecha o PreparedStatement
            ps.execute();
            ps.close();

            return true; // Exclusão realizada com sucesso

        } catch (Exception e){
            System.out.println(e.getMessage());
            return false;
        }
    }
}


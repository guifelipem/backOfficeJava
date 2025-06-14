package unialfa.hotsite.dao;

import unialfa.hotsite.model.Coordenador;
import unialfa.hotsite.model.Evento;
import unialfa.hotsite.model.Palestrante;

import java.sql.Date;
import java.sql.Time;
import java.util.ArrayList;
import java.util.List;

// Classe responsável por executar operações de banco de dados relacionadas à entidade Evento
// Estende a classe Dao (que fornece conexão com o banco) e implementa a interface DaoInterface
public class EventoDao extends Dao implements DaoInterface {

    // Método para salvar um novo evento no banco de dados
    @Override
    public boolean salvar(Object entity) {
        try {
            // Faz o cast do objeto genérico para Evento
            var evento = (Evento) entity;

            // Comando SQL para inserir um novo evento
            String sqlInsert = "INSERT INTO eventos (nome, descricao, data, horario_inicio, horario_fim, coordenador_id, palestrante_id) VALUES (?, ?, ?, ?, ?, ?, ?)";

            // Prepara a instrução SQL e atribui os valores usando os dados do objeto Evento
            var ps = getConnection().prepareStatement(sqlInsert);
            ps.setString(1, evento.getNome());
            ps.setString(2, evento.getDescricao());
            ps.setDate(3, Date.valueOf(evento.getData()));
            ps.setTime(4, Time.valueOf(evento.getHorarioInicio()));
            ps.setTime(5, Time.valueOf(evento.getHorarioFim()));
            ps.setInt(6, evento.getCoordenador().getId());
            ps.setInt(7, evento.getPalestrante().getId());

            // Executa o comando e fecha o PreparedStatement
            ps.execute();
            ps.close();

            return true; // Retorna true se a operação foi bem-sucedida

        } catch (Exception e) {
            // Em caso de erro, imprime a mensagem no console e retorna false
            System.out.println(e.getMessage());
            return false;
        }
    }

    // Método que retorna uma lista com todos os eventos cadastrados, incluindo dados dos coordenadores e palestrantes
    @Override
    public List<Object> listar() {
        List<Object> eventos = new ArrayList<>();

        // SQL com JOINs para buscar informações completas dos eventos, coordenadores e palestrantes
        String sql = "SELECT e.id, e.nome, e.descricao, e.data, e.horario_inicio, e.horario_fim,\n" +
                "       c.id as coordenador_id, c.nome as coordenador_nome,\n" +
                "       p.id as palestrante_id, p.nome as palestrante_nome\n" +
                "FROM eventos e\n" +
                "JOIN coordenadores c ON e.coordenador_id = c.id\n" +
                "JOIN palestrantes p ON e.palestrante_id = p.id";

        try {
            // Executa a consulta e obtém os resultados
            var resultSet = getConnection().prepareStatement(sql).executeQuery();

            // Percorre os resultados e cria os objetos Evento, Coordenador e Palestrante correspondentes
            while (resultSet.next()) {
                var evento = new Evento();

                evento.setId(resultSet.getInt("id"));
                evento.setNome(resultSet.getString("nome"));
                evento.setDescricao(resultSet.getString("descricao"));
                evento.setData(resultSet.getDate("data").toLocalDate());
                evento.setHorarioInicio(resultSet.getTime("horario_inicio").toLocalTime());
                evento.setHorarioFim(resultSet.getTime("horario_fim").toLocalTime());

                var coordenador = new Coordenador();
                coordenador.setId(resultSet.getInt("coordenador_id"));
                coordenador.setNome(resultSet.getString("coordenador_nome"));
                evento.setCoordenador(coordenador);

                var palestrante = new Palestrante();
                palestrante.setId(resultSet.getInt("palestrante_id"));
                palestrante.setNome(resultSet.getString("palestrante_nome"));
                evento.setPalestrante(palestrante);

                // Adiciona o evento à lista
                eventos.add(evento);
            }

            resultSet.close(); // Fecha o resultSet para liberar recursos

        } catch (Exception e) {
            // Em caso de erro, exibe a mensagem no console
            System.out.println(e.getMessage());
        }

        // Retorna a lista de eventos
        return new ArrayList<>(eventos);
    }

    // Método para atualizar os dados de um evento já existente no banco
    @Override
    public boolean atualizar(Object entity) {
        try {
            // Faz o cast do objeto genérico para Evento
            var evento = (Evento) entity;

            // Comando SQL para atualizar um evento com base no ID
            String sqlUpdate = "UPDATE eventos SET nome=?, descricao=?, data=?, horario_inicio=?, horario_fim=?, coordenador_id=?, palestrante_id=? WHERE id=?";

            var ps = getConnection().prepareStatement(sqlUpdate);
            ps.setString(1, evento.getNome());
            ps.setString(2, evento.getDescricao());
            ps.setDate(3, Date.valueOf(evento.getData()));
            ps.setTime(4, Time.valueOf(evento.getHorarioInicio()));
            ps.setTime(5, Time.valueOf(evento.getHorarioFim()));
            ps.setInt(6, evento.getCoordenador().getId());
            ps.setInt(7, evento.getPalestrante().getId());
            ps.setInt(8, evento.getId()); // Define o ID do evento que será atualizado

            ps.execute();
            ps.close();

            return true; // Atualização realizada com sucesso

        } catch (Exception e) {
            System.out.println(e.getMessage());
            return false;
        }
    }

    // Método para excluir um evento do banco com base no seu ID
    @Override
    public boolean excluir(int id) {
        try {
            // Comando SQL para excluir um evento pelo ID
            String sqlDelete = "DELETE FROM eventos WHERE id=?";

            var ps = getConnection().prepareStatement(sqlDelete);
            ps.setInt(1, id); // Define o ID do evento a ser excluído

            ps.execute();
            ps.close();

            return true; // Exclusão realizada com sucesso

        } catch (Exception e) {
            System.out.println(e.getMessage());
            return false;
        }
    }
}


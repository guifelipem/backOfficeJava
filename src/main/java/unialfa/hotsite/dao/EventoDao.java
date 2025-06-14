package unialfa.hotsite.dao;

import unialfa.hotsite.model.Coordenador;
import unialfa.hotsite.model.Evento;
import unialfa.hotsite.model.Palestrante;

import java.sql.Date;
import java.sql.Time;
import java.util.ArrayList;
import java.util.List;

public class EventoDao extends Dao implements DaoInterface{

    @Override
    public boolean salvar(Object entity) {
        try {
            var evento = (Evento) entity;

            String sqlInsert = "INSERT INTO eventos (nome, descricao, data, horario_inicio, horario_fim, coordenador_id, palestrante_id) VALUES (?, ?, ?, ?, ?, ?, ?)";

            var ps = getConnection().prepareStatement(sqlInsert);

            ps.setString(1, evento.getNome());
            ps.setString(2, evento.getDescricao());
            ps.setDate(3, Date.valueOf(evento.getData()));
            ps.setTime(4, Time.valueOf(evento.getHorarioInicio()));
            ps.setTime(5, Time.valueOf(evento.getHorarioFim()));
            ps.setInt(6, evento.getCoordenador().getId());
            ps.setInt(7, evento.getPalestrante().getId());

            ps.execute();
            ps.close();

            return true;

        } catch (Exception e) {
            System.out.println(e.getMessage());
            return false;
        }
    }

    @Override
    public List<Object> listar() {
        List<Object> eventos = new ArrayList<>();

        String sql = "SELECT e.id, e.nome, e.descricao, e.data, e.horario_inicio, e.horario_fim,\n" +
                "       c.id as coordenador_id, c.nome as coordenador_nome,\n" +
                "       p.id as palestrante_id, p.nome as palestrante_nome\n" +
                "FROM eventos e\n" +
                "JOIN coordenadores c ON e.coordenador_id = c.id\n" +
                "JOIN palestrantes p ON e.palestrante_id = p.id";

        try {
            var resultSet = getConnection().prepareStatement(sql).executeQuery();

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

                eventos.add(evento);
            }

            resultSet.close();

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        return new ArrayList<>(eventos);
    }

    @Override
    public boolean atualizar(Object entity) {
        try {
            var evento = (Evento) entity;

            String sqlUpdate = "UPDATE eventos SET nome=?, descricao=?, data=?, horario_inicio=?, horario_fim=?, coordenador_id=?, palestrante_id=? WHERE id=?";

            var ps = getConnection().prepareStatement(sqlUpdate);
            ps.setString(1, evento.getNome());
            ps.setString(2, evento.getDescricao());
            ps.setDate(3, Date.valueOf(evento.getData()));
            ps.setTime(4, Time.valueOf(evento.getHorarioInicio()));
            ps.setTime(5, Time.valueOf(evento.getHorarioFim()));
            ps.setInt(6, evento.getCoordenador().getId());
            ps.setInt(7, evento.getPalestrante().getId());
            ps.setInt(8, evento.getId());

            ps.execute();
            ps.close();

            return true;

        } catch (Exception e) {
            System.out.println(e.getMessage());
            return false;
        }
    }

    @Override
    public boolean excluir(int id) {
        try {
            String sqlDelete = "DELETE FROM eventos WHERE id=?";

            var ps = getConnection().prepareStatement(sqlDelete);
            ps.setInt(1, id);

            ps.execute();
            ps.close();

            return true;

        } catch (Exception e) {
            System.out.println(e.getMessage());
            return false;
        }
    }
}

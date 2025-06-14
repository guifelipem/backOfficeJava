package unialfa.hotsite.dao;

import unialfa.hotsite.model.Palestrante;

import java.util.ArrayList;
import java.util.List;

public class PalestranteDao extends Dao implements DaoInterface{
    @Override
    public boolean salvar(Object entity) {
        try {
            var palestrante = (Palestrante) entity;

            String sqlInsert = "INSERT INTO palestrantes (nome, minicurriculo, foto_url, tema) VALUES (?, ?, ?, ?)";

            var ps = getConnection().prepareStatement(sqlInsert);
            ps.setString(1, palestrante.getNome());
            ps.setString(2, palestrante.getMiniCurriculo());
            ps.setString(3, palestrante.getFotoUrl());
            ps.setString(4, palestrante.getTema());

            ps.execute();
            ps.close();

            return true;

        } catch (Exception e) {
            System.out.println(e.getMessage());
            return false;
        }
    }

    @Override
    public List<Palestrante> listar() {
        List<Palestrante> palestrantes = new ArrayList<>();

        String sqlSelect = "SELECT id, nome, minicurriculo, foto_url, tema FROM palestrantes";

        try {
            var resultSet = getConnection().prepareStatement(sqlSelect).executeQuery();

            while (resultSet.next()) {
                var palestrante = new Palestrante();

                palestrante.setId(resultSet.getInt("id"));
                palestrante.setNome(resultSet.getString("nome"));
                palestrante.setMiniCurriculo(resultSet.getString("minicurriculo"));
                palestrante.setFotoUrl(resultSet.getString("foto_url"));
                palestrante.setTema(resultSet.getString("tema"));

                palestrantes.add(palestrante);
            }

            resultSet.close();

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        return new ArrayList<Palestrante>(palestrantes);
    }

    @Override
    public boolean atualizar(Object entity) {
        try {
            var palestrante = (Palestrante) entity;

            String sqlUpdate = "UPDATE palestrantes SET nome=?, minicurriculo=?, foto_url=?, tema=? WHERE id=?";

            var ps = getConnection().prepareStatement(sqlUpdate);

            ps.setString(1, palestrante.getNome());
            ps.setString(2, palestrante.getMiniCurriculo());
            ps.setString(3, palestrante.getFotoUrl());
            ps.setString(4, palestrante.getTema());
            ps.setInt(5, palestrante.getId());

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
            String sqlDelete = "DELETE FROM palestrantes WHERE id=?";

            var ps = getConnection().prepareStatement(sqlDelete);
            ps.setInt(1, id);

            ps.execute();
            ps.close();

            return true;
        } catch (Exception e){
            System.out.println(e.getMessage());
            return false;
        }
    }
}

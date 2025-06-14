package unialfa.hotsite.dao;

import unialfa.hotsite.model.Coordenador;

import java.util.ArrayList;
import java.util.List;

public class CoordenadorDao extends Dao{

    public List<Coordenador> listar() {
        List<Coordenador> coordenadores = new ArrayList<>();
        String sql = "SELECT id, nome, email FROM coordenadores";

        try {
            var resultSet = getConnection().prepareStatement(sql).executeQuery();

            while (resultSet.next()) {
                var coordenador = new Coordenador();

                coordenador.setId(resultSet.getInt("id"));
                coordenador.setNome(resultSet.getString("nome"));
                coordenador.setEmail(resultSet.getString("email"));

                coordenadores.add(coordenador);
            }

            resultSet.close();

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        return new ArrayList<Coordenador>(coordenadores);
    }

}

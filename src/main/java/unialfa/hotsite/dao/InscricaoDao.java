package unialfa.hotsite.dao;

import unialfa.hotsite.model.Aluno;

import java.util.ArrayList;
import java.util.List;

public class InscricaoDao extends Dao{

    public List<Aluno> listarAlunosPorEvento(int eventoId) {
        List<Aluno> alunos = new ArrayList<>();

        String sql = "SELECT a.id, a.nome, a.email, a.curso, a.ra FROM inscricoes i JOIN alunos a ON i.aluno_id = a.id WHERE i.evento_id = ?";

        try {
            var ps = getConnection().prepareStatement(sql);
            ps.setInt(1, eventoId);

            var rs = ps.executeQuery();

            while (rs.next()) {
                var aluno = new Aluno();

                aluno.setId(rs.getInt("id"));
                aluno.setNome(rs.getString("nome"));
                aluno.setEmail(rs.getString("email"));
                aluno.setCurso(rs.getString("curso"));
                aluno.setRa(rs.getInt("ra"));

                alunos.add(aluno);
            }

            rs.close();
            ps.close();

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        return new ArrayList<>(alunos);
    }
}

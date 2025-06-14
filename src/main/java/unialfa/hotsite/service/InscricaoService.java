package unialfa.hotsite.service;

import unialfa.hotsite.dao.InscricaoDao;
import unialfa.hotsite.model.Aluno;
import unialfa.hotsite.model.Inscricao;

import java.util.List;

public class InscricaoService {

    private InscricaoDao incricaoDao = new InscricaoDao();

    public List<Aluno> listarAlunosPorEvento(int eventoId) {
        return incricaoDao.listarAlunosPorEvento(eventoId);
    }
}

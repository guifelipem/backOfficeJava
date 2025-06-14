package unialfa.hotsite.service;

import unialfa.hotsite.dao.PalestranteDao;
import unialfa.hotsite.model.Palestrante;

import java.util.List;

public class PalestranteService {

    private PalestranteDao palestranteDao = new PalestranteDao();

    public boolean salvar(Palestrante palestrante) {
        return palestranteDao.salvar(palestrante);
    }

    public List<Palestrante> listar() {
        return palestranteDao.listar();
    }

    public boolean atualizar(Palestrante palestrante) {
        return palestranteDao.atualizar(palestrante);
    }

    public boolean excluir(int id) {
        return palestranteDao.excluir(id);
    }
}

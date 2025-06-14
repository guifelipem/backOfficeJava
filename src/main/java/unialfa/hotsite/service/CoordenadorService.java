package unialfa.hotsite.service;

import unialfa.hotsite.dao.CoordenadorDao;
import unialfa.hotsite.model.Coordenador;

import java.util.List;

public class CoordenadorService {

    private final CoordenadorDao coordenadorDao = new CoordenadorDao();

    public List<Coordenador> listar() {
        return coordenadorDao.listar();
    }
}

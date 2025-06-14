package unialfa.hotsite.service;

import unialfa.hotsite.dao.EventoDao;
import unialfa.hotsite.model.Evento;

import java.util.ArrayList;
import java.util.List;

public class EventoService {

    private EventoDao eventoDao = new EventoDao();

    public boolean salvar(Evento evento) {
        return eventoDao.salvar(evento);
    }

    public List<Evento> listar() {
        List<Evento> eventos = new ArrayList<>();
        eventoDao.listar().forEach(obj -> eventos.add((Evento) obj));
        return eventos;
    }

    public boolean atualizar(Evento evento) {
        return eventoDao.atualizar(evento);
    }

    public boolean excluir(int id) {
        return eventoDao.excluir(id);
    }

}

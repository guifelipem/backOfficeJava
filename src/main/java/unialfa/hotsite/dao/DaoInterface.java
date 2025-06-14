package unialfa.hotsite.dao;

import java.util.List;

    public interface DaoInterface<T> {

        boolean salvar(Object entity);

        List<T> listar();

        boolean atualizar(Object entity);

        boolean excluir(int id);

}

package one.digital.innovation.gof.service.impl;

import one.digital.innovation.gof.model.Cliente;

/**
 * Interface que define o padrão strategy no domínio de cliente.
 * Com isso, se necessário, podemos ter múltiplas implementações dessa mesma interface.
 */

public interface ClienteService {

    Iterable<Cliente> buscarTodos();

    Cliente buscarPorId(Long id);

    void inserir(Cliente cliente);

    void atualizar(Long id, Cliente cliente);

    void deletar(Long id);
}

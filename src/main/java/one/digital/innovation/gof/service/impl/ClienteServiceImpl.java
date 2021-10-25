package one.digital.innovation.gof.service.impl;

import one.digital.innovation.gof.model.Cliente;
import one.digital.innovation.gof.model.ClienteRepository;
import one.digital.innovation.gof.model.Endereco;
import one.digital.innovation.gof.model.EnderecoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * Implementação da Strategy , a qual pode ser injetada pelo Spring. Com isso essa
 * classe é um @link service, ela será tratada como um Singleton
 */

@Service
public class ClienteServiceImpl implements ClienteService {
    /**
     * Singleton: Injetar os componentes do Spring com @Autowired
     * Strategy: Implementar os métodos definidos na interface
     * Facade: Abstrair integrações com subsistemas, provendo uma interface simples
     * @return
     */

    /**
     * Singleton: Injetar os componenetes do Spring com @Autowired
     */
    @Autowired
    private ClienteRepository clienteRepository;
    @Autowired
    private EnderecoRepository enderecoRepository;
    @Autowired
    private ViaCepService viaCepService;

    @Override
    public Iterable<Cliente> buscarTodos() {
        return clienteRepository.findAll();
    }

    @Override
    public Cliente buscarPorId(Long id) {
        Optional<Cliente> cliente = clienteRepository.findById(id);
        return cliente.get();
    }

    @Override
    public void inserir(Cliente cliente) {
        salvarClienteComCep(cliente);
    }

    private void salvarClienteComCep(Cliente cliente){


        //Verifica se o endereço do cliente já existe(pelo CEP).
        var cep = cliente.getEndereco().getCep();
        Endereco endereco = enderecoRepository.findById(cep).orElseGet(() -> {
            // Caso não exista, integrar com o ViaCep e persistir o retorno.
            Endereco novoEndereco = viaCepService.consultarCep(cep);
            enderecoRepository.save(novoEndereco);
            return novoEndereco;
        });
        cliente.setEndereco(endereco);
        //Inserir cliente,vinculando o endereço(novo ou existente).
        clienteRepository.save(cliente);
    }

    @Override
    public void atualizar(Long id, Cliente cliente) {

        //Buscar cliente por Id, caso exista
        Optional<Cliente> clienteBd = clienteRepository.findById(id);
        if (clienteBd.isPresent()) {
            salvarClienteComCep(cliente);
        }

        // Verificar se o endereço do cliente já existe pelo cep
        //Caso não exista, integrar com o Viacep e persistir o retorno
        //Alterar cliente, vinculando o endereço (novo ou existente)

    }

    @Override
    public void deletar(Long id) {
        /**
         * Deletar cliente por id
         */
        clienteRepository.deleteById(id);
    }
}

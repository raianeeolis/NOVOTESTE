package br.ucb.labbd.ToDoList.services;

import org.springframework.stereotype.Service;
import br.ucb.labbd.ToDoList.entities.Tarefa;
import br.ucb.labbd.ToDoList.repositories.TarefaRepository;
import br.ucb.labbd.ToDoList.entities.Usuario;
import br.ucb.labbd.ToDoList.repositories.UsuarioRepository;
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Service
public class TarefaService {
    private final TarefaRepository tarefaRepo;
    private final UsuarioRepository usuarioRepo;
    private final IdGeneratorService idGenerator; // Necessário para gerar IDs customizados

    // Assumindo que você terá um IdGeneratorService para simular a função SQL
    public TarefaService(TarefaRepository tarefaRepo, UsuarioRepository usuarioRepo) {
        this.tarefaRepo = tarefaRepo;
        this.usuarioRepo = usuarioRepo;
        // Inicializando o serviço simulado de geração de ID
        this.idGenerator = new IdGeneratorService(); 
    }
    
    // Serviço Simples de Geração de ID (Substituindo a Function SQL por simulação Java)
    private static class IdGeneratorService {
        private int counter = 0;
        public String generateId(String prefix) {
            counter++;
            // Garante que o ID gerado tenha o formato TAR-0000X, compatível com VARCHAR(15)
            return String.format("%s-%05d", prefix, counter); 
        }
    }

    // LISTAR: Retorna todas as tarefas de um usuário
    // CORREÇÃO: userId agora é String
    public List<Tarefa> findAllByUserId(String userId) {
        return tarefaRepo.findByUsuarioIdUsuario(userId);
    }

    // BUSCAR: Retorna uma tarefa específica de um usuário. CRÍTICO para segurança.
    // CORREÇÃO: userId agora é String
    public Optional<Tarefa> findByIdAndUserId(String tarefaId, String userId) {
        // Observação: Assumindo que o método no Repositório aceita String para o ID do Usuário
        return tarefaRepo.findByIdTarefaAndUsuarioIdUsuario(tarefaId, userId); 
    }

    // CRIAR: Cria uma nova tarefa, associa ao usuário e gera ID customizado
    @Transactional
    // CORREÇÃO: userId agora é String
    public Tarefa createTarefa(Tarefa tarefa, String userId) {
        // CORREÇÃO: findByIdUsuario agora aceita String
        Optional<Usuario> oUser = usuarioRepo.findByIdUsuario(userId);
        if (oUser.isEmpty()) {
             throw new IllegalArgumentException("Usuário não encontrado.");
        }
        
        tarefa.setIdTarefa(idGenerator.generateId("TAR")); // Gera ID (TAR-0000X)
        tarefa.setUsuario(oUser.get()); 
        
        // Define status e datas padrão
        if (tarefa.getStatus() == null) {
            // Assegura que PENDENTE está em ALL_CAPS conforme a correção final em Tarefa.java
            tarefa.setStatus(Tarefa.Status.PENDENTE); 
        }
        
        return tarefaRepo.save(tarefa);
    }

    // ATUALIZAR: Atualiza uma tarefa, verificando se pertence ao usuário
    @Transactional
    // CORREÇÃO: userId agora é String
    public Optional<Tarefa> updateTarefa(String tarefaId, Tarefa tarefaDetails, String userId) {
        Optional<Tarefa> oTarefa = findByIdAndUserId(tarefaId, userId);

        if (oTarefa.isPresent()) {
            Tarefa existingTarefa = oTarefa.get();
            // Atualiza apenas campos permitidos
            existingTarefa.setTitulo(tarefaDetails.getTitulo());
            existingTarefa.setDescricao(tarefaDetails.getDescricao());
            existingTarefa.setPrioridade(tarefaDetails.getPrioridade());
            existingTarefa.setStatus(tarefaDetails.getStatus());
            existingTarefa.setDataVencimento(tarefaDetails.getDataVencimento());
            
            // A trigger MySQL cuidará de data_conclusao, se configurada.
            return Optional.of(tarefaRepo.save(existingTarefa));
        }
        return Optional.empty(); // Não encontrado ou não pertence ao usuário
    }

    // EXCLUIR: Exclui uma tarefa, verificando se pertence ao usuário
    @Transactional
    // CORREÇÃO: userId agora é String
    public boolean deleteTarefa(String tarefaId, String userId) {
        Optional<Tarefa> oTarefa = findByIdAndUserId(tarefaId, userId);

        if (oTarefa.isPresent()) {
            tarefaRepo.delete(oTarefa.get());
            return true;
        }
        return false;
    }
}
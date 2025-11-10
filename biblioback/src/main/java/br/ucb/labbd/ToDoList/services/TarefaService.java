package br.ucb.labbd.ToDoList.services;

import org.springframework.stereotype.Service;
import br.ucb.labbd.ToDoList.entities.Tarefa;
import br.ucb.labbd.ToDoList.entities.Usuario;
import br.ucb.labbd.ToDoList.repositories.TarefaRepository;
import br.ucb.labbd.ToDoList.repositories.UsuarioRepository;
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Service
public class TarefaService {

    private final TarefaRepository tarefaRepo;
    private final UsuarioRepository usuarioRepo;
    private final IdGeneratorService idGenerator;

    public TarefaService(TarefaRepository tarefaRepo, UsuarioRepository usuarioRepo) {
        this.tarefaRepo = tarefaRepo;
        this.usuarioRepo = usuarioRepo;
        this.idGenerator = new IdGeneratorService();
    }

    private static class IdGeneratorService {
        private int counter = 0;
        public String generateId(String prefix) {
            counter++;
            return String.format("%s-%05d", prefix, counter);
        }
    }

    public List<Tarefa> findAllByUserId(String userId) {
        return tarefaRepo.findByUsuarioIdUsuario(userId);
    }

    public Optional<Tarefa> findByIdAndUserId(String tarefaId, String userId) {
        return tarefaRepo.findByIdTarefaAndUsuarioIdUsuario(tarefaId, userId);
    }

    @Transactional
    public Tarefa createTarefa(Tarefa tarefa, String userId) {
        Optional<Usuario> oUser = usuarioRepo.findByIdUsuario(userId);
        if (oUser.isEmpty()) {
            throw new IllegalArgumentException("Usuário não encontrado.");
        }

        tarefa.setIdTarefa(idGenerator.generateId("TAR"));
        tarefa.setUsuario(oUser.get());

        if (tarefa.getStatus() == null) tarefa.setStatus(Tarefa.Status.PENDENTE);
        if (tarefa.getPrioridade() == null) tarefa.setPrioridade(Tarefa.Prioridade.MEDIA);

        return tarefaRepo.save(tarefa);
    }

    @Transactional
    public Optional<Tarefa> updateTarefa(String tarefaId, Tarefa tarefaDetails, String userId) {
        Optional<Tarefa> oTarefa = findByIdAndUserId(tarefaId, userId);

        if (oTarefa.isPresent()) {
            Tarefa existing = oTarefa.get();
            existing.setTitulo(tarefaDetails.getTitulo());
            existing.setDescricao(tarefaDetails.getDescricao());
            existing.setPrioridade(tarefaDetails.getPrioridade());
            existing.setStatus(tarefaDetails.getStatus());
            existing.setDataVencimento(tarefaDetails.getDataVencimento());
            return Optional.of(tarefaRepo.save(existing));
        }
        return Optional.empty();
    }

    @Transactional
    public boolean deleteTarefa(String tarefaId, String userId) {
        Optional<Tarefa> oTarefa = findByIdAndUserId(tarefaId, userId);
        if (oTarefa.isPresent()) {
            tarefaRepo.delete(oTarefa.get());
            return true;
        }
        return false;
    }
}

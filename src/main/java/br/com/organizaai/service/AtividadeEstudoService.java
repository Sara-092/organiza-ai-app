package br.com.organizaai.service;

import br.com.organizaai.model.AtividadeEstudo;
import br.com.organizaai.repository.AtividadeEstudoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
public class AtividadeEstudoService {
    private final AtividadeEstudoRepository repository;

    public AtividadeEstudoService(AtividadeEstudoRepository repository) {
        this.repository = repository;
    }

    @Transactional(readOnly = true)
    public List<AtividadeEstudo> listarPorData(LocalDate data) {
        return repository.findByDataOrderByHorarioAsc(data);
    }

    @Transactional(readOnly = true)
    public List<AtividadeEstudo> listarProximas() {
        return repository.findAllByOrderByDataAscHorarioAsc().stream()
                .filter(a -> a.getData() != null && !a.getData().isBefore(LocalDate.now()))
                .limit(4).toList();
    }

    @Transactional(readOnly = true)
    public long contarTodas() { return repository.count(); }

    @Transactional(readOnly = true)
    public long contarConcluidas() { return repository.countByConcluidaTrue(); }

    @Transactional
    public AtividadeEstudo salvar(AtividadeEstudo atividade) {
        if (atividade.getMateria() == null || atividade.getMateria().isBlank()
                || atividade.getData() == null || atividade.getHorario() == null) {
            throw new IllegalArgumentException("Preencha a matéria, a data e o horário.");
        }
        atividade.setMateria(atividade.getMateria().trim());
        return repository.save(atividade);
    }

    @Transactional
    public void alternarConclusao(Long id) {
        AtividadeEstudo atividade = repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Atividade não encontrada."));
        atividade.setConcluida(!atividade.isConcluida());
        repository.save(atividade);
    }

    @Transactional
    public void excluir(Long id) { repository.deleteById(id); }
}

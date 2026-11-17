package br.com.organizaai.service;

import br.com.organizaai.model.Meta;
import br.com.organizaai.repository.MetaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class MetaService {

    private final MetaRepository repository;

    public MetaService(MetaRepository repository) {
        this.repository = repository;
    }

    @Transactional(readOnly = true)
    public List<Meta> listar() {
        return repository.findAllByOrderByConcluidaAscDataLimiteAsc();
    }

    @Transactional(readOnly = true)
    public Meta buscarPorId(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Meta não encontrada: " + id));
    }

    @Transactional
    public Meta cadastrar(Meta meta) {
        validar(meta);
        meta.setId(null);
        meta.setConcluida(false);
        return repository.save(meta);
    }

    @Transactional
    public Meta atualizar(Long id, Meta dados) {
        validar(dados);
        Meta existente = buscarPorId(id);
        existente.setTitulo(dados.getTitulo().trim());
        existente.setDescricao(dados.getDescricao());
        existente.setDataLimite(dados.getDataLimite());
        return repository.save(existente);
    }

    @Transactional
    public void alternarConclusao(Long id) {
        Meta meta = buscarPorId(id);
        meta.setConcluida(!meta.isConcluida());
        repository.save(meta);
    }

    @Transactional
    public void excluir(Long id) {
        if (!repository.existsById(id)) {
            throw new IllegalArgumentException("Meta não encontrada: " + id);
        }
        repository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public long totalConcluidas() {
        return repository.countByConcluida(true);
    }

    @Transactional(readOnly = true)
    public long totalPendentes() {
        return repository.countByConcluida(false);
    }

    private void validar(Meta meta) {
        if (meta.getTitulo() == null || meta.getTitulo().isBlank()) {
            throw new IllegalArgumentException("Informe um título para a meta.");
        }
    }
}
package br.com.organizaai.service;

import br.com.organizaai.model.Flashcard;
import br.com.organizaai.repository.FlashcardRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class FlashcardService {
    private final FlashcardRepository repository;

    public FlashcardService(FlashcardRepository repository) {
        this.repository = repository;
    }

    @Transactional(readOnly = true)
    public List<Flashcard> listarTodos() {
        return repository.findAllByOrderByCriadoEmDesc();
    }

    @Transactional(readOnly = true)
    public long contar() { return repository.count(); }

    @Transactional
    public Flashcard salvar(Flashcard flashcard) {
        if (flashcard.getPergunta() == null || flashcard.getPergunta().isBlank()
                || flashcard.getResposta() == null || flashcard.getResposta().isBlank()) {
            throw new IllegalArgumentException("Preencha a pergunta e a resposta.");
        }
        flashcard.setPergunta(flashcard.getPergunta().trim());
        flashcard.setResposta(flashcard.getResposta().trim());
        return repository.save(flashcard);
    }

    @Transactional
    public void excluir(Long id) {
        if (!repository.existsById(id)) {
            throw new IllegalArgumentException("Flashcard não encontrado.");
        }
        repository.deleteById(id);
    }
}

package br.com.organizaai.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import br.com.organizaai.model.Flashcard;
import br.com.organizaai.repository.FlashcardRepository;

@Controller
public class FlashcardController {

    private final FlashcardRepository flashcardRepository;

    public FlashcardController(FlashcardRepository flashcardRepository) {
        this.flashcardRepository = flashcardRepository;
    }

    @GetMapping("/flashcards")
    public String listar(Model model) {
        model.addAttribute("flashcards", flashcardRepository.findAllByOrderByIdDesc());
        return "flashcards";
    }

    @PostMapping("/flashcards/salvar")
    public String salvar(@RequestParam String pergunta,
                         @RequestParam String resposta,
                         RedirectAttributes redirect) {
        String p = pergunta == null ? "" : pergunta.trim();
        String r = resposta == null ? "" : resposta.trim();

        if (p.isEmpty() || r.isEmpty()) {
            redirect.addFlashAttribute("erroFlashcard", "Preencha a pergunta e a resposta.");
            return "redirect:/flashcards";
        }

        flashcardRepository.save(new Flashcard(p, r));
        redirect.addFlashAttribute("sucessoFlashcard", "Flashcard salvo com sucesso!");
        return "redirect:/flashcards";
    }

    @PostMapping("/flashcards/{id}/excluir")
    public String excluir(@PathVariable Long id, RedirectAttributes redirect) {
        flashcardRepository.deleteById(id);
        redirect.addFlashAttribute("sucessoFlashcard", "Flashcard excluído.");
        return "redirect:/flashcards";
    }
}
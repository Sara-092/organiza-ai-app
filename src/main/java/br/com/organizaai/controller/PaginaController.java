package br.com.organizaai.controller;

import br.com.organizaai.model.AtividadeEstudo;
import br.com.organizaai.model.Flashcard;
import br.com.organizaai.service.AtividadeEstudoService;
import br.com.organizaai.service.FlashcardService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;

@Controller
public class PaginaController {
    private final AtividadeEstudoService atividadeService;
    private final FlashcardService flashcardService;

    public PaginaController(AtividadeEstudoService atividadeService,
                            FlashcardService flashcardService) {
        this.atividadeService = atividadeService;
        this.flashcardService = flashcardService;
    }

    @GetMapping("/")
    public String inicio(Model model) {
        model.addAttribute("totalAtividades", atividadeService.contarTodas());
        model.addAttribute("concluidas", atividadeService.contarConcluidas());
        model.addAttribute("totalFlashcards", flashcardService.contar());
        model.addAttribute("proximas", atividadeService.listarProximas());
        return "index";
    }

    @GetMapping("/rotina")
    public String rotina(@RequestParam(required = false) String data, Model model) {
        LocalDate selecionada = (data == null || data.isBlank()) ? LocalDate.now() : LocalDate.parse(data);
        model.addAttribute("atividades", atividadeService.listarPorData(selecionada));
        model.addAttribute("dataSelecionada", selecionada);
        model.addAttribute("novaAtividade", new AtividadeEstudo());
        return "rotina";
    }

    @PostMapping("/rotina/salvar")
    public String salvarAtividade(@ModelAttribute AtividadeEstudo novaAtividade, RedirectAttributes redirect) {
        try {
            atividadeService.salvar(novaAtividade);
            redirect.addFlashAttribute("sucesso", "Atividade adicionada ao calendário!");
        } catch (IllegalArgumentException e) {
            redirect.addFlashAttribute("erro", e.getMessage());
        }
        return "redirect:/rotina" + (novaAtividade.getData() == null ? "" : "?data=" + novaAtividade.getData());
    }

    @PostMapping("/rotina/{id}/concluir")
    public String concluirAtividade(@PathVariable Long id, @RequestParam String data) {
        atividadeService.alternarConclusao(id);
        return "redirect:/rotina?data=" + data;
    }

    @PostMapping("/rotina/{id}/excluir")
    public String excluirAtividade(@PathVariable Long id, @RequestParam String data) {
        atividadeService.excluir(id);
        return "redirect:/rotina?data=" + data;
    }

    @GetMapping("/tempo")
    public String tempo() {
        return "tempo";
    }

    @GetMapping("/flashcards")
    public String flashcards(Model model) {
        model.addAttribute("flashcards", flashcardService.listarTodos());
        model.addAttribute("novoFlashcard", new Flashcard());
        return "flashcards";
    }

    @PostMapping("/flashcards/salvar")
    public String salvarFlashcard(@ModelAttribute Flashcard novoFlashcard, RedirectAttributes redirect) {
        try {
            flashcardService.salvar(novoFlashcard);
            redirect.addFlashAttribute("sucessoFlashcard", "Flashcard salvo no banco de dados!");
        } catch (IllegalArgumentException e) {
            redirect.addFlashAttribute("erroFlashcard", e.getMessage());
        }
        return "redirect:/flashcards";
    }

    @PostMapping("/flashcards/{id}/excluir")
    public String excluirFlashcard(@PathVariable Long id, RedirectAttributes redirect) {
        try {
            flashcardService.excluir(id);
            redirect.addFlashAttribute("sucessoFlashcard", "Flashcard excluído.");
        } catch (IllegalArgumentException e) {
            redirect.addFlashAttribute("erroFlashcard", e.getMessage());
        }
        return "redirect:/flashcards";
    }
}

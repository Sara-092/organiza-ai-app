package br.com.organizaai.controller;

import br.com.organizaai.model.AtividadeEstudoModel;
import br.com.organizaai.repository.FlashcardRepository;
import br.com.organizaai.service.AtividadeEstudoService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;

@Controller
public class PaginaController {

    private final AtividadeEstudoService atividadeService;
    private final FlashcardRepository flashcardRepository;

    public PaginaController(AtividadeEstudoService atividadeService, FlashcardRepository flashcardRepository) {
        this.atividadeService = atividadeService;
        this.flashcardRepository = flashcardRepository;
    }

    @GetMapping("/")
    public String inicio(Model model, HttpServletResponse response) {
        semCache(response);
        model.addAttribute("pagina", "inicio");
        model.addAttribute("totalAtividades", atividadeService.contarTodas());
        model.addAttribute("concluidas", atividadeService.contarConcluidas());
        model.addAttribute("proximas", atividadeService.listarProximas());
        model.addAttribute("totalFlashcards", flashcardRepository.count());
        return "index";
    }

    @GetMapping("/rotina")
    public String rotina(@RequestParam(required = false) String data, Model model, HttpServletResponse response) {
        semCache(response);
        LocalDate selecionada = (data == null || data.isBlank()) ? LocalDate.now() : LocalDate.parse(data);
        model.addAttribute("pagina", "rotina");
        model.addAttribute("atividades", atividadeService.listarPorData(selecionada));
        model.addAttribute("dataSelecionada", selecionada);
        model.addAttribute("novaAtividade", new AtividadeEstudoModel());
        return "rotina";
    }

    @PostMapping("/rotina/salvar")
    public String salvarAtividade(@ModelAttribute AtividadeEstudoModel novaAtividade, RedirectAttributes redirect) {
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
    public String tempo(Model model) {
        model.addAttribute("pagina", "tempo");
        return "tempo";
    }

    private void semCache(HttpServletResponse response) {
        response.setHeader("Cache-Control", "no-store, no-cache, must-revalidate, max-age=0");
        response.setHeader("Pragma", "no-cache");
        response.setDateHeader("Expires", 0);
    }
}
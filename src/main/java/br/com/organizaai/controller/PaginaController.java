package br.com.organizaai.controller;
import br.com.organizaai.model.AtividadeEstudo;
import br.com.organizaai.service.AtividadeEstudoService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;

@Controller
public class PaginaController {
    private final AtividadeEstudoService atividadeService;

    public PaginaController(AtividadeEstudoService atividadeService ) {
        this.atividadeService = atividadeService;
       
    }

    @GetMapping("/")
    public String inicio(Model model) {
        model.addAttribute("totalAtividades", atividadeService.contarTodas());
        model.addAttribute("concluidas", atividadeService.contarConcluidas());
        model.addAttribute("proximas", atividadeService.listarProximas());
        return "index";
    }
    @GetMapping("/flashcards")
public String flashcards() {
    return "flashcards";
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
}

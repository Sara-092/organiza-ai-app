package br.com.organizaai.controller;

import br.com.organizaai.model.Meta;
import br.com.organizaai.service.MetaService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/metas")
public class MetaController {

    private final MetaService service;

    public MetaController(MetaService service) {
        this.service = service;
    }

    // CONSULTAR (lista + formulário de cadastro)
    @GetMapping
    public String listar(Model model) {
        carregarPagina(model, new Meta(), false);
        return "metas";
    }

    // CONSULTAR uma meta para editar (preenche o formulário)
    @GetMapping("/{id}/editar")
    public String editar(@PathVariable Long id, Model model, RedirectAttributes ra) {
        try {
            carregarPagina(model, service.buscarPorId(id), true);
            return "metas";
        } catch (IllegalArgumentException e) {
            ra.addFlashAttribute("erro", e.getMessage());
            return "redirect:/metas";
        }
    }

    // CADASTRAR
    @PostMapping
    public String cadastrar(@ModelAttribute Meta meta, RedirectAttributes ra) {
        try {
            service.cadastrar(meta);
            ra.addFlashAttribute("sucesso", "Meta cadastrada com sucesso!");
        } catch (IllegalArgumentException e) {
            ra.addFlashAttribute("erro", e.getMessage());
        }
        return "redirect:/metas";
    }

    // ATUALIZAR
    @PostMapping("/{id}")
    public String atualizar(@PathVariable Long id, @ModelAttribute Meta meta, RedirectAttributes ra) {
        try {
            service.atualizar(id, meta);
            ra.addFlashAttribute("sucesso", "Meta atualizada com sucesso!");
        } catch (IllegalArgumentException e) {
            ra.addFlashAttribute("erro", e.getMessage());
        }
        return "redirect:/metas";
    }

    // MARCAR / DESMARCAR COMO CONCLUÍDA
    @PostMapping("/{id}/concluir")
    public String concluir(@PathVariable Long id, RedirectAttributes ra) {
        try {
            service.alternarConclusao(id);
        } catch (IllegalArgumentException e) {
            ra.addFlashAttribute("erro", e.getMessage());
        }
        return "redirect:/metas";
    }

    // EXCLUIR
    @PostMapping("/{id}/excluir")
    public String excluir(@PathVariable Long id, RedirectAttributes ra) {
        try {
            service.excluir(id);
            ra.addFlashAttribute("sucesso", "Meta excluída.");
        } catch (IllegalArgumentException e) {
            ra.addFlashAttribute("erro", e.getMessage());
        }
        return "redirect:/metas";
    }

    private void carregarPagina(Model model, Meta meta, boolean editando) {
        model.addAttribute("meta", meta);
        model.addAttribute("editando", editando);
        model.addAttribute("metas", service.listar());
        model.addAttribute("totalPendentes", service.totalPendentes());
        model.addAttribute("totalConcluidas", service.totalConcluidas());
    }
}
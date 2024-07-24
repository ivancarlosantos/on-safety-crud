package ics.on_safety.desafio.crud.web;

import ics.on_safety.desafio.crud.model.Pessoa;
import ics.on_safety.desafio.crud.repository.PessoaRepository;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import lombok.RequiredArgsConstructor;
import org.springframework.web.servlet.ModelAndView;

@RequiredArgsConstructor
@Controller
public class PessoaWebView {

    private final PessoaRepository repository;

    @GetMapping({"/list", "/"})
    public ModelAndView listPessoas() {
        ModelAndView mav = new ModelAndView("index");
        mav.addObject("pessoas", repository.findAll());
        return mav;
    }

    @GetMapping("/addPessoaForm")
    public ModelAndView addPessoaForm() {
        ModelAndView mav = new ModelAndView("novo");
        Pessoa pessoa = new Pessoa();
        mav.addObject("pessoa", pessoa);
        return mav;
    }

    @PostMapping("/savePessoa")
    public String saveEmployee(@ModelAttribute Pessoa pessoa) {
        repository.save(pessoa);
        return "redirect:/list";
    }

    @GetMapping("/showUpdateForm")
    public ModelAndView showUpdateForm(@RequestParam Long id) {
        ModelAndView mav = new ModelAndView("novo");
        Pessoa pessoa = repository.findById(id).get();
        mav.addObject("pessoa", pessoa);
        return mav;
    }

    @GetMapping("/deletePessoa")
    public String deleteEmployee(@RequestParam Long id) {
        repository.deleteById(id);
        return "redirect:/list";
    }
}

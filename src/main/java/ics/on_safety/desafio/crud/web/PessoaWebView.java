package ics.on_safety.desafio.crud.web;

import ics.on_safety.desafio.crud.model.Pessoa;
import ics.on_safety.desafio.crud.repository.PessoaRepository;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import lombok.RequiredArgsConstructor;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.ArrayList;
import java.util.List;

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
    public ModelAndView saveEmployee(@ModelAttribute("pessoa") @Validated Pessoa pessoa, BindingResult result, RedirectAttributes redirect) {
        if (result.hasErrors()) {
            ModelAndView mv = new ModelAndView("redirect:/addPessoaForm");
            List<Pessoa> l = repository.findAll();
            mv.addObject("pessoas", l);
            mv.addObject("pessoa", pessoa);

            List<String> msg = new ArrayList<>();
            for (ObjectError errors : result.getAllErrors()) {
                msg.add(errors.getDefaultMessage());
            }
            mv.addObject("msg", msg);
            return mv;
        }

        repository.save(pessoa);

        ModelAndView view = new ModelAndView("redirect:/list");
        List<Pessoa> l = repository.findAll();
        view.addObject("pessoas", l);
        view.addObject("pessoa", new Pessoa());
        redirect.addFlashAttribute("message", "Pessoa Salva com Sucesso!");
        return view;
    }

    @GetMapping("/showUpdateForm")
    public ModelAndView showUpdateForm(@ModelAttribute("pessoa") @RequestParam Long id) {
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

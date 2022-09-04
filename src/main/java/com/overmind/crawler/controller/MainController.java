package com.overmind.crawler.controller;

import com.overmind.crawler.model.Movie;
import com.overmind.crawler.service.impl.ImdbServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.SortDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Objects;

@Controller
@RequiredArgsConstructor
public class MainController {

    private final ImdbServiceImpl crawlerService;

    @GetMapping(value = "/")
    public String indexPage(@SortDefault.SortDefaults({
                                    @SortDefault( sort="position", direction = Sort.Direction.ASC)
                            }) Pageable pageable,
                            Model model) {

        model.addAttribute("movies", crawlerService.findAll(pageable));
        return "/index";
    }

    @GetMapping(value = "/update")
    public String updateRanking(RedirectAttributes attributes) {
        crawlerService.updateRanking();

        attributes.addFlashAttribute("alertClass", "success");
        attributes.addFlashAttribute("message", "Ranking atualizado com sucesso");
        return "redirect:/";
    }

    @GetMapping(value = "/detail/{id}")
    public String detailPage(@PathVariable("id") Long id,
                             RedirectAttributes attributes,
                             Model model) {

        Movie movie = crawlerService.findMovie(id);
        if(Objects.isNull(movie)) {
            attributes.addFlashAttribute("alertClass", "error");
            attributes.addFlashAttribute("message", "Não foi possível encontrar os detalhes do filme. Atualize o ranking");
            return "redirect:/";
        }

        model.addAttribute("movie", movie);
        return "detail";
    }
}

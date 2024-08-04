package org.tinkoff.labwork.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.tinkoff.labwork.database.TranslationRequestRepository;
import org.tinkoff.labwork.model.LanguagesDTO;
import org.tinkoff.labwork.model.TranslateDto;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;

import static org.tinkoff.labwork.TranslateUtils.getTranslatedText;

@Controller
public class TranslationController {

    private final TranslationRequestRepository repository;

    public TranslationController(TranslationRequestRepository repository) {
        this.repository = repository;
    }

    @GetMapping("/translate")
    public String showLanguages() {
        return "translate";
    }

    @PostMapping("/translate")
    public String translate(LanguagesDTO languagesDTO,
                            HttpServletRequest request,
                            Model model) {

        String userIpAddress = request.getRemoteAddr();
        String translatedText = getTranslatedText(languagesDTO);
        TranslateDto translateDto = new TranslateDto(userIpAddress, languagesDTO.getSourceLang(), languagesDTO.getTargetLang(),
                languagesDTO.getTextToTranslate(), translatedText, LocalDateTime.now());
        repository.save(translateDto);

        model.addAttribute("sourceLang", languagesDTO.getSourceLang());
        model.addAttribute("targetLang", languagesDTO.getTargetLang());
        model.addAttribute("inputText", languagesDTO.getTextToTranslate());
        model.addAttribute("userIpAddress", userIpAddress);
        model.addAttribute("translation", translatedText);

        return "/translate";
    }
}
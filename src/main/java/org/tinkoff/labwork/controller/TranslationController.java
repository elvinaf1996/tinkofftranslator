package org.tinkoff.labwork.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.tinkoff.labwork.model.LanguagesDto;
import org.tinkoff.labwork.service.TranslationService;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@Slf4j
@RequiredArgsConstructor
public class TranslationController {

    private final TranslationService translationService;

    @GetMapping("/translate")
    public String showLanguages() {
        return "translate";
    }

    @PostMapping("/translate")
    public ResponseEntity<String> translateApi(@Valid @RequestBody LanguagesDto languagesDTO, BindingResult result,
                                               HttpServletRequest request) {
        if (result.hasErrors()) {
            List<String> errorsList = result.getAllErrors().stream()
                    .map(DefaultMessageSourceResolvable::getDefaultMessage)
                    .collect(Collectors.toList());

            return ResponseEntity.badRequest().body(String.join(", ", errorsList));
        }

        if (languagesDTO.getSourceLang().equalsIgnoreCase(languagesDTO.getTargetLang())) {
            return ResponseEntity.badRequest().body("Языки перевода совпадают");
        }

        String translatedText = translationService.getTranslatedText(languagesDTO.getSourceLang(), languagesDTO.getTargetLang(),
                languagesDTO.getTextToTranslate());

        translationService.saveTranslation(request.getRemoteAddr(),
                languagesDTO.getSourceLang(), languagesDTO.getTargetLang(), languagesDTO.getTextToTranslate(),
                translatedText);

        return ResponseEntity.ok(translatedText);
    }
}
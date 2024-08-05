package org.tinkoff.labwork.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.tinkoff.labwork.database.TranslationRequestRepository;
import org.tinkoff.labwork.model.LanguagesDto;
import org.tinkoff.labwork.model.TranslateDto;
import org.tinkoff.labwork.model.TranslationResponseDto;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;

import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;
import static org.tinkoff.labwork.TranslateUtils.getTranslatedText;
import static org.tinkoff.labwork.TranslateUtils.languages;

@Controller
@Slf4j
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
    public String translate(LanguagesDto languagesDTO,
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

    @PostMapping("/translate/api")
    public ResponseEntity<TranslationResponseDto> translateApi(@RequestBody LanguagesDto languagesDTO, HttpServletRequest request) {
        String userIpAddress = request.getRemoteAddr();
        String sourceLangError = "Не найден язык исходного сообщения";
        String targetLangError = "Не найден язык перевода";
        String languagesError = "Не найден язык исходного сообщения и перевода";
        String sourceLangIsEmpty = "Не указан язык исходного сообщения";
        String targetLangIsEmpty = "Не указан язык перевода";
        String languagesAreEmpty = "Не указаны языки";
        String languagesIsEqualsError = "Языки перевода совпадают";
        String error = "Ошибка доступа к ресурсу перевода";

        try {
            TranslationResponseDto response = new TranslationResponseDto();

            if (isNull(languagesDTO.getSourceLang()) && isNull(languagesDTO.getTargetLang())) {
                response.setErrorMessage(languagesAreEmpty);
            } else if (!languages.contains(languagesDTO.getSourceLang()) && !languages.contains(languagesDTO.getTargetLang())) {
                response.setErrorMessage(languagesError);
            } else if (isNull(languagesDTO.getSourceLang())) {
                response.setErrorMessage(sourceLangIsEmpty);
            } else if (isNull(languagesDTO.getTargetLang())) {
                response.setErrorMessage(targetLangIsEmpty);
            } else if (!languages.contains(languagesDTO.getSourceLang())) {
                response.setErrorMessage(sourceLangError);
            } else if (!languages.contains(languagesDTO.getTargetLang())) {
                response.setErrorMessage(targetLangError);
            } else if (languagesDTO.getSourceLang().equals(languagesDTO.getTargetLang())) {
                response.setErrorMessage(languagesIsEqualsError);
            }

            if (nonNull(response.getErrorMessage())) {
                response.setCode("http 400");

                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
            }

            String translatedText = getTranslatedText(languagesDTO);
            TranslateDto translateDto = new TranslateDto(userIpAddress, languagesDTO.getSourceLang(), languagesDTO.getTargetLang(),
                    languagesDTO.getTextToTranslate(), translatedText, LocalDateTime.now());
            repository.save(translateDto);

            response.setCode("http 200");
            response.setTranslatedText(translatedText);

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.info(e.getMessage());
            TranslationResponseDto response = new TranslationResponseDto();
            response.setCode("http 400");
            response.setErrorMessage(error);

            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }
}
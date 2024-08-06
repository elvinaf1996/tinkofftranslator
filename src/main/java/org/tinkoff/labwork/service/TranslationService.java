package org.tinkoff.labwork.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.tinkoff.labwork.exception.TranslatorException;
import org.tinkoff.labwork.repository.TranslationRequestRepository;

import java.io.IOException;
import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

@Slf4j
@Service
@RequiredArgsConstructor
public class TranslationService {

    private final ObjectMapper objectMapper;
    private final ExecutorService executorService;
    private final RestTemplate restTemplate;
    private final TranslationRequestRepository requestRepository;
    @Value("${google.translate.api}")
    private String googleApi;
    @Value("${google.translate.client}")
    private String client;

    public String translateWord(String word, String sourceLang, String targetLang) {

        String url = String.format("%s?client=%s&sl=%s&tl=%s&q=%s",
                googleApi,
                client,
                sourceLang,
                targetLang,
                word
        );

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        try {
            ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, new HttpEntity<>(headers), String.class);
            List<String> values = objectMapper.readerForListOf(String.class).readValue(response.getBody());
            return values.get(0);
        } catch (RestClientException | IOException | IllegalArgumentException ex) {
            log.error("Ошибка доступа к ресурсу перевода", ex);
            throw new TranslatorException("Ошибка доступа к ресурсу перевода");
        }
    }

    public String getTranslatedText(String sourceLang, String targetLang, String toTranslate) {
        String[] words = toTranslate.split(" ");

        List<Future<String>> futures = Arrays.stream(words)
                .map(word -> executorService.submit(() -> translateWord(word, sourceLang, targetLang)))
                .toList();

        StringBuilder stringBuilder = new StringBuilder();

        for (Future<String> future : futures) {
            try {
                String translatedWord = future.get();
                stringBuilder.append(translatedWord).append(" ");
            } catch (Exception e) {
                log.error(e.getMessage(), e);
                throw new TranslatorException(e.getMessage());
            }
        }
        String translated = stringBuilder.toString().trim();

        log.debug("Completed translation text: {}, from: {} to {}, result: {}",
                toTranslate, sourceLang, targetLang, translated);

        return translated;
    }

    public void saveTranslation(String ip, String sourceLang, String targetLang, String toTranslate, String translatedText) {
        requestRepository.save(ip, sourceLang, targetLang, toTranslate, translatedText);
    }
}

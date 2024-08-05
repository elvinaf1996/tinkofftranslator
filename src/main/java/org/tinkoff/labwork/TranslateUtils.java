package org.tinkoff.labwork;

import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;
import org.tinkoff.labwork.model.LanguagesDto;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class TranslateUtils {

    private static final String GOOGLE_TRANSLATE_API_URL = "https://clients5.google.com/translate_a/t";
    private static final String CLIENT = "dict-chrome-ex";
    public static final List<String> languages = List.of("ru", "en", "es", "fr", "de", "it", "pt", "zh-CN",
            "ja","ko", "ar", "tr", "hi", "sv", "da", "fi", "no","cs", "pl", "hu");

    public static String translateWord(LanguagesDto languages, String word) {

        RestTemplate restTemplate = new RestTemplate();

        String url = String.format("%s?client=%s&sl=%s&tl=%s&q=%s",
                GOOGLE_TRANSLATE_API_URL,
                CLIENT,
                languages.getSourceLang(),
                languages.getTargetLang(),
                word);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, new HttpEntity<>(headers), String.class);

        return response.getBody().replaceAll("[\\[\\]]", "").replaceAll("\"", "");
    }

    public static String getTranslatedText(LanguagesDto languagesDTO) {

        String[] words = languagesDTO.getTextToTranslate().split(" ");

        ExecutorService executorService = Executors.newFixedThreadPool(10);
        List<Future<String>> futures = new ArrayList<>();

        for (String word : words) {
            Future<String> future = executorService.submit(() -> translateWord(languagesDTO, word));
            futures.add(future);
        }

        executorService.shutdown();

        StringBuilder stringBuilder = new StringBuilder();

        for (Future<String> future : futures) {
            try {
                String translatedWord = future.get();
                stringBuilder.append(translatedWord).append(" ");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return stringBuilder.toString().trim();
    }
}
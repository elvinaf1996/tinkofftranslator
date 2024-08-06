package org.tinkoff.labwork.repository;

public interface TranslationRequestRepository {

    void save(String ip, String sourceLang, String targetLang, String toTranslate, String translatedText);
}
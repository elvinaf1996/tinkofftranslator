package org.tinkoff.labwork.repository.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.tinkoff.labwork.exception.DatabaseException;
import org.tinkoff.labwork.exception.TranslatorException;
import org.tinkoff.labwork.repository.TranslationRequestRepository;

import javax.annotation.PostConstruct;
import java.time.LocalDateTime;

@Slf4j
@Repository
@RequiredArgsConstructor
public class TranslationRequestRepositoryImpl implements TranslationRequestRepository {

    public static final String CREATE_DATABASE_SQL = """
            CREATE TABLE IF NOT EXISTS TranslationRequests (
                id SERIAL PRIMARY KEY,
                userIp VARCHAR(45),
                sourceLang VARCHAR(10),
                targetLang VARCHAR(10),
                inputText TEXT,
                translatedText TEXT,
                timestamp TIMESTAMP DEFAULT CURRENT_TIMESTAMP
            );
            """;
    public static final String SAVE_RECORD_SQL = """
            INSERT INTO TranslationRequests (userIp, sourceLang, targetLang, inputText, translatedText, timestamp)
            VALUES (?, ?, ?, ?, ?, ?)
            """;
    private final JdbcTemplate jdbcTemplate;

    @PostConstruct
    public void init() {
        initDatabase();
    }

    @Override
    public void save(String ip, String sourceLang, String targetLang, String toTranslate, String translatedText) {
        try {
            jdbcTemplate.update(SAVE_RECORD_SQL, ip, sourceLang, targetLang, toTranslate, translatedText, LocalDateTime.now());
        } catch (DataAccessException e) {
            log.error("Failed to save record", e);
            throw new DatabaseException("Ошибка доступа к базе данных");
        }
    }


    private void initDatabase() {
        try {
            jdbcTemplate.execute(CREATE_DATABASE_SQL);
        } catch (DataAccessException e) {
            log.error("Failed to initialize the database", e);
            throw new DatabaseException("Ошибка доступа к базе данных");
        }

    }
}
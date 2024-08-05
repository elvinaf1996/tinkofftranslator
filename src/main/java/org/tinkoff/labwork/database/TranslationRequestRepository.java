package org.tinkoff.labwork.database;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.tinkoff.labwork.model.TranslateDto;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Repository
public class TranslationRequestRepository {

    private final JdbcTemplate jdbcTemplate;

    public TranslationRequestRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void save(TranslateDto translateDto) {
        String sql = """
                INSERT INTO TranslationRequests (userIp, sourceLang, targetLang, inputText, translatedText, timestamp)
                VALUES (?, ?, ?, ?, ?, ?)
                """;

        jdbcTemplate.update(sql, translateDto.getUserIp(), translateDto.getSourceLang(), translateDto.getTargetLang(),
                translateDto.getInputText(), translateDto.getTranslatedText(), translateDto.getTimestamp());
    }
}
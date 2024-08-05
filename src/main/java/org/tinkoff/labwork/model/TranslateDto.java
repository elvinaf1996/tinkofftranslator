package org.tinkoff.labwork.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TranslateDto {

    private String userIp;

    private String sourceLang;

    private String targetLang;

    private String inputText;

    private String translatedText;

    private LocalDateTime timestamp;
}

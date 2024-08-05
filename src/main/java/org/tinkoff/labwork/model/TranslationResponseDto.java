package org.tinkoff.labwork.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Objects;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class TranslationResponseDto {

    private String code;
    private String translatedText;
    private String errorMessage;

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        TranslationResponseDto that = (TranslationResponseDto) object;
        return Objects.equals(code, that.code) && Objects.equals(translatedText, that.translatedText) && Objects.equals(errorMessage, that.errorMessage);
    }
}

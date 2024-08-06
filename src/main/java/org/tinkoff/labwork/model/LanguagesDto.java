package org.tinkoff.labwork.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.tinkoff.labwork.validation.ValidLanguageCode;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class LanguagesDto {

    @NotBlank(message = "Не указан исходный язык")
    @Size(max = 4, message = "Код языка должен состоять максимум из 4 символов")
    @ValidLanguageCode(message = "Не найден язык исходного сообщения")
    private String sourceLang;

    @NotBlank(message = "Не указан язык перевода")
    @Size(max = 4, message = "Код языка должен состоять максимум из 4 символов")
    @ValidLanguageCode(message = "Не найден язык перевода")
    private String targetLang;

    @NotNull(message = "Сообщение не может быть пустым")
    private String textToTranslate;
}

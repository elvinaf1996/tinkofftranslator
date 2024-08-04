package org.tinkoff.labwork.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class LanguagesDTO {

    private String sourceLang;
    private String targetLang;
    private String textToTranslate;
}

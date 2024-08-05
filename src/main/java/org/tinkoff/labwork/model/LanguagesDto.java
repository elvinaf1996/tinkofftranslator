package org.tinkoff.labwork.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class LanguagesDto {

    private String sourceLang;
    private String targetLang;
    private String textToTranslate;
}

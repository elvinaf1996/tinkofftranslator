import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.tinkoff.labwork.TranslateApplication;
import org.tinkoff.labwork.model.LanguagesDto;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@SpringBootTest(classes = TranslateApplication.class)
@AutoConfigureMockMvc
public class TranslateControllerTest {

    @Autowired
    private MockMvc mockMvc;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    public void indexTest() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/").accept(MediaType.TEXT_HTML))
                .andExpect(status().isOk())
                .andExpect(model().hasNoErrors())
                .andExpect(view().name("translate"));
    }

    @Test
    public void successPostTranslateApi() throws Exception {
        String sourceLang = "ru";
        String targetLang = "en";
        String textToTranslate = "меня любит жизнь";
        String translatedText = "me loves life";

        LanguagesDto languagesDTO = new LanguagesDto(sourceLang, targetLang, textToTranslate);

        mockMvc.perform(post("/translate")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(languagesDTO)))
                .andExpect(status().isOk())
                .andExpect(content().string(translatedText));
    }

    @Test
    public void failureTranslateApiBadRequestNoSourceLang() throws Exception {
        LanguagesDto languagesDTO = new LanguagesDto(null, "en", "я люблю жизнь");

        mockMvc.perform(post("/translate")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(languagesDTO)))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(containsString("Не указан исходный язык")));
    }

    @Test
    public void failurePostWithIncorrectSourceLang() throws Exception {
        LanguagesDto languagesDTO = new LanguagesDto("gygju", "ru", "я люблю жизнь");

        mockMvc.perform(post("/translate")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(languagesDTO)))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(containsString("Не найден язык исходного сообщения")));
    }

    @Test
    public void failurePostWithIncorrectTargetLang() throws Exception {
        LanguagesDto languagesDTO = new LanguagesDto("ru", "fghjkl", "я люблю жизнь");

        mockMvc.perform(post("/translate")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(languagesDTO)))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(containsString("Не найден язык перевода")));
    }

    @Test
    public void failureTranslateApiBadRequestSameLanguages() throws Exception {
        LanguagesDto languagesDTO = new LanguagesDto("ru", "ru", "здравствуй мир");

        mockMvc.perform(post("/translate")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(languagesDTO)))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(containsString("Языки перевода совпадают")));
    }
}
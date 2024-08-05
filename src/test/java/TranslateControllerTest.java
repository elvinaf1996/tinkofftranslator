import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.ui.Model;
import org.tinkoff.labwork.controller.TranslationController;
import org.tinkoff.labwork.database.TranslationRequestRepository;
import org.tinkoff.labwork.model.LanguagesDto;
import org.tinkoff.labwork.model.TranslateDto;
import org.tinkoff.labwork.model.TranslationResponseDto;

import javax.servlet.http.HttpServletRequest;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@Slf4j
public class TranslateControllerTest {

    @InjectMocks
    private TranslationController translateController;

    @Mock
    private TranslationRequestRepository repository;

    @Mock
    private Model model;

    @Mock
    private HttpServletRequest request;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void successGetHtml() {
        String sourceLang = "ru";
        String targetLang = "en";
        String textToTranslate = "меня любит жизнь";
        String translatedText = "me loves life";

        LanguagesDto languagesDTO = new LanguagesDto(sourceLang, targetLang, textToTranslate);
        HttpServletRequest request = new MockHttpServletRequest();

        String viewName = translateController.translate(languagesDTO, request, model);

        verify(model).addAttribute("sourceLang", sourceLang);
        verify(model).addAttribute("targetLang", targetLang);
        verify(model).addAttribute("inputText", textToTranslate);
        verify(model).addAttribute("translation", translatedText);

        assertEquals("/translate", viewName);
    }

    @Test
    public void successPostTranslateApi() {
        String sourceLang = "ru";
        String targetLAng = "en";
        String textToTranslate = "меня любит жизнь";
        String translatedText = "me loves life";
        String expectedCode = "http 200";

        LanguagesDto languagesDTO = new LanguagesDto(sourceLang, targetLAng, textToTranslate);
        HttpServletRequest request = new MockHttpServletRequest();
        TranslationResponseDto translationResponseDto = new TranslationResponseDto();
        translationResponseDto.setTranslatedText(translatedText);
        translationResponseDto.setCode(expectedCode);
        TranslationResponseDto response = translateController.translateApi(languagesDTO, request).getBody();
        assertEquals(response, translationResponseDto);
        assertEquals("http 200", response.getCode());
    }

    @Test
    public void failureTranslateApiBadRequestNoSourceLang() {
        LanguagesDto languagesDTO = new LanguagesDto("ru", null, "я люблю жизнь");

        ResponseEntity<TranslationResponseDto> response = translateController.translateApi(languagesDTO, request);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("http 400", response.getBody().getCode());
        assertEquals("Не указан язык перевода", response.getBody().getErrorMessage());
    }

    @Test
    public void failureTranslateApiBadRequestSameLanguages() {
        LanguagesDto languagesDTO = new LanguagesDto("ru", "ru", "я люблю жизнь");

        ResponseEntity<TranslationResponseDto> response = translateController.translateApi(languagesDTO, request);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("http 400", response.getBody().getCode());
        assertEquals("Языки перевода совпадают", response.getBody().getErrorMessage());
    }

    @Test
    public void failureTranslateApiExceptionHandling() {
        LanguagesDto languagesDTO = new LanguagesDto(null, null, "здравствуй мир");

        ResponseEntity<TranslationResponseDto> response = translateController.translateApi(languagesDTO, request);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("http 400", response.getBody().getCode());
        assertEquals("Не указаны языки", response.getBody().getErrorMessage());
    }

    @Test
    public void failurePostWithIncorrectSourceLang() {
        LanguagesDto languagesDTO = new LanguagesDto("gygju", "ru", "здравствуй мир");

        ResponseEntity<TranslationResponseDto> response = translateController.translateApi(languagesDTO, request);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("http 400", response.getBody().getCode());
        assertEquals("Не найден язык исходного сообщения", response.getBody().getErrorMessage());
    }

    @Test
    public void failurePostWithIncorrectTargetLang() {
        LanguagesDto languagesDTO = new LanguagesDto("ru", "fghjkl", "здравствуй мир");

        ResponseEntity<TranslationResponseDto> response = translateController.translateApi(languagesDTO, request);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("http 400", response.getBody().getCode());
        assertEquals("Не найден язык перевода", response.getBody().getErrorMessage());
    }

    @Test
    public void failurePostWithIncorrectTargetAndSourceLang() {
        LanguagesDto languagesDTO = new LanguagesDto("ghbjb", "fghjkl", "здравствуй мир");

        ResponseEntity<TranslationResponseDto> response = translateController.translateApi(languagesDTO, request);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("http 400", response.getBody().getCode());
        assertEquals("Не найден язык исходного сообщения и перевода", response.getBody().getErrorMessage());
    }
}
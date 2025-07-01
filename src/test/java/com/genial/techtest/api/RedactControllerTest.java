package com.genial.techtest.api;

import com.genial.techtest.persistence.RedactRepository;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class RedactControllerTest {

    private final RedactRepository redactRepository = new RedactRepository();
    private final RedactController redactController = new RedactController(redactRepository);

    @BeforeAll
    public void beforeAll() {
        redactRepository.addWordsToRedactedWordSet("dog", "cat", "sNaKe", "dolphin", "mammal");
    }

    @ParameterizedTest
    @CsvSource(value = {
            "test,test",
            "test123,test123",
            "dog123,dog123",
            "dog,REDACTED",
            "cat,REDACTED",
            "sNaKe,REDACTED",
            "dolphin,REDACTED",
            "mammal,REDACTED",
            "snakeMammal,snakeMammal",
            "dog cat dogcat,REDACTED REDACTED dogcat",
            "corn-snake,corn-REDACTED",
            "dog@mammal.cat,REDACTED@REDACTED.REDACTED",
            "DoG,REDACTED",
    })
    public void redactWordsFromText_happyPathTest(String input, String expected) {
        assertEquals(expected, redactController.redactWordsFromText(input));
    }

    @Test
    public void redactWordsFromText_longerStringTest() {
        String testString = "This is a Test sNaKe dog, corn-snake rattle-snake, cat@mammal.horse | SnakeDog";
        String expectedResult = "This is a Test REDACTED REDACTED, corn-REDACTED rattle-REDACTED, REDACTED@REDACTED.horse | SnakeDog";

        assertEquals(expectedResult, redactController.redactWordsFromText(testString));
    }

    @Test
    public void redactWordsFromText_keyTestTest() {
        String testString = "A dog, a monkey or a dolphin are all mammals. A snake, however, is not a mammal, it is a reptile. Who can say what a DogSnake is?";
        String expectedResult = "A REDACTED, a monkey or a REDACTED are all mammals. A REDACTED, however, is not a REDACTED, it is a reptile. Who can say what a DogSnake is?";

        assertEquals(expectedResult, redactController.redactWordsFromText(testString));
    }

    @ParameterizedTest
    @NullSource
    @ValueSource(strings = {
            "",
            " ",
            "  ",
            "\n",
    })
    public void redactWordsFromText_variouslyEmptyFails400Test(String input) {
        try {
            redactController.redactWordsFromText(input);
            fail("redactWordsFromText with empty input should have thrown exception.");
        } catch (Exception e) {
            assertEquals(ResponseStatusException.class, e.getClass());
            assertEquals(HttpStatus.BAD_REQUEST, ((ResponseStatusException) e).getStatusCode());
        }
    }

    @Test
    public void getServiceNameTest() {
        assertEquals("Redaction Service", redactController.getServiceName());
    }
}
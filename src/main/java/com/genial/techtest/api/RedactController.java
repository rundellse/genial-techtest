package com.genial.techtest.api;

import com.genial.techtest.persistence.RedactRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.util.Arrays;
import java.util.List;
import java.util.ListIterator;
import java.util.Set;

@RestController
public class RedactController {
    private static final Logger LOG = LoggerFactory.getLogger(RedactController.class);
    private static final Logger REDACT_REQUEST_LOGGER = LoggerFactory.getLogger("REDACT_REQUEST_LOGGER");

    private final RedactRepository redactRepository;

    private static final String REDACTED = "REDACTED";

    @Autowired
    public RedactController(RedactRepository redactRepository) {
        this.redactRepository = redactRepository;
    }


    @GetMapping("/redact")
    public String getServiceName() {
        return "Redaction Service";
    }

    @PostMapping("/redact")
    public String redactWordsFromText(@RequestBody String requestString) {
        // Log request to specific log file
        REDACT_REQUEST_LOGGER.info(requestString);

        if (requestString == null || requestString.isBlank()) {
            LOG.error("BAD REQUEST for redact call, request body is null or empty");
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }

        String redactedString = redactWordsInString(requestString);
        LOG.debug("Returning redacted string: {}", redactedString);
        return redactedString;
    }

    private String redactWordsInString(String string) {
        Set<String> redactedWordSet = redactRepository.getRedactedWordSet();
        LOG.trace("Attempting string redaction, redacted words: {}", redactedWordSet);

        // Split the input at the beginning and end of each 'word' (alphanumeric sequence)
        List<String> wordList = Arrays.asList(string.split("(\\b)"));

        // Go through each 'word' and replace it if it's in our banned wordlist.
        ListIterator<String> wordIterator = wordList.listIterator();
        String word;
        while (wordIterator.hasNext()) {
            word = wordIterator.next().toLowerCase();
            if (redactedWordSet.contains(word)) {
                LOG.debug("Redacted word '{}' found in string, redacting.", word);
                wordIterator.set(REDACTED);
            }
        }

        // Reassemble the string with redactions now in place and return
        StringBuilder stringBuilder = new StringBuilder();
        for (String resultWord : wordList) {
            stringBuilder.append(resultWord);
        }
        return stringBuilder.toString();
    }
}

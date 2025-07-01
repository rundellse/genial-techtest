package com.genial.techtest.persistence;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import java.util.HashSet;
import java.util.Set;

@Repository
public class RedactRepository {

    private static final Logger LOG = LoggerFactory.getLogger(RedactRepository.class);

    private static final Set<String> redactedWordSet = new HashSet<>();

    public Set<String> getRedactedWordSet() {
        return new HashSet<>(redactedWordSet);
    }

    public void addWordsToRedactedWordSet(String... redactedWords) {
        LOG.trace("Adding redacted words to redacted list");

        for (String redactedWord : redactedWords) {
            if (redactedWord == null || redactedWord.isBlank()) {
                LOG.warn("Attempted to add redacted word which is either null or blank. Redacted words must be alphanumeric. Skipping");
                continue;
            } else if (redactedWord.matches("[^0-z]")) {
                LOG.warn("Attempted to add redacted word '{}' containing non-alphanumeric characters. Redacted words must be alphanumeric. Skipping", redactedWord);
                continue;
            }

            redactedWordSet.add(redactedWord.toLowerCase());
        }
    }

}

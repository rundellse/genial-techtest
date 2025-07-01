package com.genial.techtest;

import com.genial.techtest.persistence.RedactRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GenialTechTestConfiguration {


    @Bean
    public CommandLineRunner commandLineRunner(@Value("${genial.techtest.redacted.wordlist}") String... wordList) {
        return args -> {
            redactRepository().addWordsToRedactedWordSet(wordList);
        };
    }

    @Bean
    public RedactRepository redactRepository() {
        return new RedactRepository();
    }

}

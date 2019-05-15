package com.mozcalti.devops.tools;


import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class AppTest {

    public static final String URL = "http://127.0.0.1:5000";
    public static final String URL_TAILING_SLASH = "http://127.0.0.1:5000/";
    public static final String INVALID_URL = "URL";
    public static final String TOKEN = "TOKEN";
    public static final String PROJECT_KEY = "PROJECT_KEY";
    public static final String SINCE = "SINCE";
    public static final String RETIRES = "RETIRES";
    public static final String WAIT_TIME = "WAIT_TIME";

    @Test
    @DisplayName("At least 4 arguments needed")
    public void atLeastFourArgumentsNeeded() {

        String[] args = {URL, TOKEN, PROJECT_KEY};

        RuntimeException e = assertThrows(RuntimeException.class, () -> App.main(args));

        assertThat(e.getMessage()).isEqualTo("Patametes: URL TOKEN PROJECT_KEY SINCE [RETRIES [WAIT_TIME]]");

    }

    @Test
    @DisplayName("No more than 6 arguments accepted")
    public void noMoreThanSixArguments() {

        String[] args = {URL, TOKEN, PROJECT_KEY, SINCE, RETIRES, WAIT_TIME, "SOMETHING EXTRA"};

        RuntimeException e = assertThrows(RuntimeException.class, () -> App.main(args));

        assertThat(e.getMessage()).isEqualTo("Patametes: URL TOKEN PROJECT_KEY SINCE [RETRIES [WAIT_TIME]]");

    }

    @Test
    @DisplayName("Invalid URL")
    public void invalidUrl() {

        String[] args = {INVALID_URL, TOKEN, PROJECT_KEY, SINCE};

        RuntimeException e = assertThrows(RuntimeException.class, () -> App.main(args));

        assertThat(e.getMessage()).startsWith("Invalid URL");

    }


}

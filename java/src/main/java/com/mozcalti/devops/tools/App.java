package com.mozcalti.devops.tools;

import com.mozcalti.devops.tools.sonar.model.SonarAnalysesResponse;
import com.mozcalti.devops.tools.sonar.model.SonarQualityGateResponse;
import lombok.NonNull;
import lombok.SneakyThrows;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.net.URLEncoder;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.mozcalti.devops.tools.utilities.UrlFactory.analysesUrl;
import static com.mozcalti.devops.tools.utilities.UrlFactory.qualityGateUrl;

public class App {

    private static final Integer TIMEOUT = 2;
    private static final Integer QUALITY_GATE_NOT_MET = 1;
    private static final Long DEFAULT_SLEEP_TIME_MILISECONDS = 30000L;
    private static final Integer DEFAULT_RETRIES = 6;

    private static final Pattern PATTERN = Pattern.compile("^(https?://[^/]+)/?$");


    @SneakyThrows
    public static void main(@NonNull String[] args) {

        validateArgs(args);

        long sleepTime = DEFAULT_SLEEP_TIME_MILISECONDS;
        int retries = DEFAULT_RETRIES;

        if (args.length == 5) {
            retries = Integer.parseInt(args[4]);
        } else if (args.length == 6) {

            retries = Integer.parseInt(args[4]);
            sleepTime = Long.parseLong(args[5]);
        }

        if (sleepTime < 0 || retries < 0) {
            throw new RuntimeException(String.format("Retries [%d] and sleep time [%d] must be positive", retries, sleepTime));
        }

        RestTemplate restTemplate = new RestTemplate();

        String host = args[0].replaceAll("^(.*)/?$", "$1");
        String token = args[1];
        String projectKey = args[2];
        String sinceTime = args[3];

        ResponseEntity<SonarAnalysesResponse> analysesList;

        HttpHeaders headers = new HttpHeaders();
        headers.setBasicAuth(token, "");

        HttpEntity<Void> request = new HttpEntity<>(null, headers);

        do {
            try {
                analysesList = restTemplate.exchange(analysesUrl(host, projectKey, sinceTime), HttpMethod.GET, request, SonarAnalysesResponse.class);

                if (retries > 0 && analysesList.getBody().getAnalyses().size() == 0) {
                    retries--;
                    Thread.sleep(sleepTime);
                } else {
                    break;
                }

            } catch (HttpClientErrorException e) {
                System.err.println(e.getStatusCode());
                System.err.println(e.getStatusText());
                System.err.println(e.getCause());
                System.err.println(e.getMessage());
                System.exit(3);
            }

        } while (true);

        if (retries == 0) {
            System.exit(TIMEOUT);
        }


        String analysisKey = analysesList.getBody().getAnalyses().get(0).getKey();


        ResponseEntity<SonarQualityGateResponse> qualityGateResponse = restTemplate.exchange(qualityGateUrl(host, analysisKey), HttpMethod.GET, request, SonarQualityGateResponse.class);


        String status = qualityGateResponse.getBody().getProjectStatus().getStatus();

        if (!status.equals("OK")) {
            System.exit(QUALITY_GATE_NOT_MET);
        }


    }

    private static void validateArgs(@NonNull String[] args) {
        if (args.length < 4 || args.length > 6) {
            throw new RuntimeException("Patametes: URL TOKEN PROJECT_KEY SINCE [RETRIES [WAIT_TIME]]");
        }

        Matcher matcher = PATTERN.matcher(args[0]);
        if (!matcher.matches()) {
            throw new RuntimeException(String.format("Invalid URL [%s]", args[0]));
        }
    }
}


package com.mozcalti.devops.tools;

import com.mozcalti.devops.tools.sonar.model.SonarAnalysesResponse;
import com.mozcalti.devops.tools.sonar.model.SonarQualityGateResponse;
import lombok.SneakyThrows;
import org.springframework.web.client.RestTemplate;

import static com.mozcalti.devops.tools.utilities.UrlFactory.analysesUrl;
import static com.mozcalti.devops.tools.utilities.UrlFactory.qualityGateUrl;

public class App {

    private static final Integer TIMEOUT = 2;
    private static final Integer QUALITY_GATE_NOT_MET = 1;


    @SneakyThrows
    public static void main(String[] args) {

        long sleepTime = 60000L; //1 min

        RestTemplate restTemplate = new RestTemplate();

        String host = args[0];
        String token = args[1];
        String projectKey = args[2];
        String sinceTime = args[3];

        SonarAnalysesResponse analysesList;
        do {
            analysesList = restTemplate.getForObject(analysesUrl(host, token, projectKey, sinceTime), SonarAnalysesResponse.class);

            if (analysesList.getAnalyses().size() == 0) {
                Thread.sleep(sleepTime);
            } else {
                break;
            }
        } while (true);


        String analysisKey = analysesList.getAnalyses().get(0).getKey();

        SonarQualityGateResponse qualityGateResponse = restTemplate.getForObject(qualityGateUrl(host, analysisKey), SonarQualityGateResponse.class);


        String status = qualityGateResponse.getProjectStatus().getStatus();

        if (!status.equals("OK")) {
            System.exit(QUALITY_GATE_NOT_MET);
        }


    }
}


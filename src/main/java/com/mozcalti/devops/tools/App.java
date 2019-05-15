package com.mozcalti.devops.tools;

import lombok.Data;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.List;

/**
 * Hello world!
 */
public class App {

    @Data
    private static class SonarPaging {
        private Integer pageIndex;
        private Integer pageSize;
        private Integer total;
    }

    @Data
    private static class SonarAnalysis {
        private String key;
        private String date;
        private String projectVersion;
        private String buildString;
        private List<SonarEvent> events;

    }

    @Data
    private static class SonarEvent {
        private String key;
        private String category;
        private String name;
    }

    @Data
    private static class SonarAnalysesSearchResponse {
        private SonarPaging paging;
        private List<SonarAnalysis> analyses;
    }

    @Data
    private static class SonarQualityGateResponse {
        private SonarProjectStatus projectStatus;
    }

    @Data
    private static class SonarProjectStatus {
        private String status;
        private Boolean ignoreConditions;
        private List<SonarqubeCondition> conditions;
        private List<SonarqubePeriod> periods;
    }

    @Data
    private static class SonarqubeCondition {
        private String status;
        private String metricKey;
        private String comparator;
        private String periodIndex;
        private String errorThreshold;
        private String actualValue;
    }

    @Data
    private static class SonarqubePeriod {
        private String index;
        private String mode;
        private String date;
        private String parameter;
    }


    public static void main(String[] args) {
        String host = args[0];
        String token = args[1];
        String projectKey = args[2];
        String sinceTime = args[3];

        System.out.println("Host: " + host);
        System.out.println("Token: " + token);
        System.out.println("Project Key: " + projectKey);
        System.out.println("Since time: " + sinceTime);

        String since = sinceTime.replaceAll(":(\\d{2})$", "$1");


        RestTemplate restTemplate = new RestTemplate();

        String url = String.format("%s/api/project_analyses/search?project=%s&from=%s", host, projectKey, since);

        System.out.println("Requesting: " + url);

        ResponseEntity<SonarAnalysesSearchResponse> response = restTemplate.getForEntity(url, SonarAnalysesSearchResponse.class);


        String analysisKey = response.getBody().getAnalyses().get(0).getKey();
        System.out.println("Last Analysis key: " + analysisKey);

        String url2 = String.format("%s/api/qualitygates/project_status?analysisId=%s", host, analysisKey);
        System.out.println("Requesting: " + url2);

        ResponseEntity<SonarQualityGateResponse> qualityGateResponse = restTemplate.getForEntity(url2, SonarQualityGateResponse.class);


        System.out.println("Estatus del proyecto: " + qualityGateResponse.getBody().getProjectStatus().getStatus());




    }
}


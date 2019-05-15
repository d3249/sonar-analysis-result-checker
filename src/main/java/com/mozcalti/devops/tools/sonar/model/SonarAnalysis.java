package com.mozcalti.devops.tools.sonar.model;

import lombok.Data;

import java.util.List;

@Data
public class SonarAnalysis {
    private String key;
    private String date;
    private String projectVersion;
    private String buildString;
    private List<SonarEvent> events;

}



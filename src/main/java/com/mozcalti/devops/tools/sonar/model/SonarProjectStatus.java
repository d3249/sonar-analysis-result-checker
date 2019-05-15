package com.mozcalti.devops.tools.sonar.model;

import lombok.Data;

import java.util.List;


@Data
public class SonarProjectStatus {
    private String status;
    private Boolean ignoreConditions;
    private List<SonarqubeCondition> conditions;
    private List<SonarqubePeriod> periods;
}


package com.mozcalti.devops.tools.sonar.model;

import lombok.Data;


@Data
public class SonarqubeCondition {
    private String status;
    private String metricKey;
    private String comparator;
    private String periodIndex;
    private String errorThreshold;
    private String actualValue;
}


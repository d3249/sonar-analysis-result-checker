package com.mozcalti.devops.tools.sonar.model;

import lombok.Data;

@Data
public class SonarQualityGateResponse {
    private SonarProjectStatus projectStatus;
}


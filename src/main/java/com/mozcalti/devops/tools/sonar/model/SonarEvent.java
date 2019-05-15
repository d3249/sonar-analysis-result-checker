package com.mozcalti.devops.tools.sonar.model;

import lombok.Data;

@Data
public class SonarEvent {
    private String key;
    private String category;
    private String name;
}


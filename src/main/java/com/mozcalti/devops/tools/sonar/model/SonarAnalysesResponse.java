package com.mozcalti.devops.tools.sonar.model;

import lombok.Data;

import java.util.List;

@Data
public class SonarAnalysesResponse {

    private SonarPaging paging;
    private List<SonarAnalysis> analyses;

}

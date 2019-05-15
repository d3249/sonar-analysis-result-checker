package com.mozcalti.devops.tools.utilities;

import lombok.experimental.UtilityClass;

@UtilityClass
public class UrlFactory {

    public static String analysesUrl(String host, String token, String projectKey, String since) {

        String sinceFixed = since.replaceAll(":(\\d{2})$", "$1");

        return String.format("%s/api/project_analyses/search?project=%s&from=%s", host, projectKey, sinceFixed);
    }

    public static String qualityGateUrl(String host, String analysisKey) {

        return String.format("%s/api/qualitygates/project_status?analysisId=%s", host, analysisKey);
    }
}

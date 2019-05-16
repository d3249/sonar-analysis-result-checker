package com.mozcalti.devops.tools.utilities;

import lombok.SneakyThrows;
import lombok.experimental.UtilityClass;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.net.URLEncoder;


@UtilityClass
public class UrlFactory {


    public static final String ANALYSES_LIST_TEMPLATE = "%s/api/project_analyses/search";
    public static final String QUALITY_GATES_TEMPLATE = "%s/api/qualitygates/project_status";

    public static URI analysesUrl(String host, String token, String projectKey, String since) {

        String sinceFixed = since.replaceAll(":(\\d{2})$", "$1");
        String url = String.format(ANALYSES_LIST_TEMPLATE, host);

        return UriComponentsBuilder.fromHttpUrl(url).queryParam("project", encodeUTF8(projectKey)).queryParam("from", encodeUTF8(sinceFixed)).build(true).toUri();

    }

    @SneakyThrows
    private static String encodeUTF8(String sinceFixed) {
        return URLEncoder.encode(sinceFixed,"UTF-8");
    }

    public static URI qualityGateUrl(String host, String analysisKey) {
        String url = String.format(QUALITY_GATES_TEMPLATE, host);

        return UriComponentsBuilder.fromHttpUrl(url).queryParam("analysisId", encodeUTF8(analysisKey)).build(true).toUri();
    }

}

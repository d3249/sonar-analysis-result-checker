package com.mozcalti.devops.tools.sonar.model;

import lombok.Data;

@Data
public class SonarPaging {
        private Integer pageIndex;
        private Integer pageSize;
        private Integer total;

}

package io.github.imsejin.study.springframework.batch.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class KanClassification {

    private String code;

    private String name;

    private Integer level;

    private String parentCode;

    private Integer parentLevel;

    private String description;

}

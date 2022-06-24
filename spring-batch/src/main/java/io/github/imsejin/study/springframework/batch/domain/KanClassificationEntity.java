package io.github.imsejin.study.springframework.batch.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Getter
@Setter
@Entity
@Table(name = "KAN_CLS")
@ToString
public class KanClassificationEntity {

    @Id
    @Column(name = "KAN_CODE", nullable = false)
    private String code;

    @Column(name = "CLS_NM", nullable = false)
    private String name;

    @Column(name = "OPP_CLS_LVL_NO", nullable = false)
    private Integer level;

    @Column(name = "KAN_PRT_CODE", nullable = false)
    private String parentCode;

    @Column(name = "PRT_CLS_LVL_NO", nullable = false)
    private Integer parentLevel;

    @Column(name = "KAN_DESC")
    private String description;

}

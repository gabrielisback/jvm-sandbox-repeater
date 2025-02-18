package com.alibaba.repeater.console.dal.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * {@link ModuleConfig}
 * <p>
 *
 * @author zhaoyb1990
 */
@Entity
@Table(name = "module_config")
@Getter
@Setter
public class ModuleConfig implements java.io.Serializable {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id")
    private Long id;

    @Column(name = "gmt_create")
    private LocalDateTime gmtCreate;

    @Column(name = "gmt_modified")
    private LocalDateTime gmtModified;

    @Column(name = "app_name")
    private String appName;

    private String environment;

    private String config;
}

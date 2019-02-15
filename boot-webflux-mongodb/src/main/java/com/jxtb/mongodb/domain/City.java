package com.jxtb.mongodb.domain;

import lombok.Data;
import org.springframework.data.annotation.Id;

/**
 * Created by jxtb on 2019/2/15.
 */
@Data
public class City {
    /**
     * 城市编号
     * @Id 注解标记对应库表的主键或者唯一标识符。因为这个是我们的 DO，数据访问对象一一映射到数据存储。
     */
    @Id
    private Long id;

    /**
     * 省份编号
     */
    private Long provinceId;

    /**
     * 城市名称
     */
    private String cityName;

    /**
     * 描述
     */
    private String description;
}

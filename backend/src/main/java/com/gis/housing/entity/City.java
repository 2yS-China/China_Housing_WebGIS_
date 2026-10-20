package com.gis.housing.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;

@Data
@TableName("t_city")
public class City {

    @TableId
    private String cityCode;
    private String cityName;
    private String provinceName;
    private BigDecimal lon;
    private BigDecimal lat;
}

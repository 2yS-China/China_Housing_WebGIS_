package com.gis.housing.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;

@Data
@TableName("t_province")
public class Province {

    @TableId
    private String provinceCode;
    private String provinceName;
    private BigDecimal centerLon;
    private BigDecimal centerLat;
}

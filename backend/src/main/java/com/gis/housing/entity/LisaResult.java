package com.gis.housing.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;

@Data
@TableName("t_lisa_result")
public class LisaResult {

    @TableId(type = IdType.AUTO)
    private Integer id;
    private Integer moranId;
    private String cityCode;
    private BigDecimal localI;
    private BigDecimal pValue;
    private String clusterType;
}

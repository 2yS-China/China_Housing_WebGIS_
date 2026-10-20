package com.gis.housing.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;

@Data
@TableName("t_yearly_price")
public class YearlyPrice {

    @TableId(type = IdType.AUTO)
    private Integer id;
    private String cityCode;
    private Short year;
    private BigDecimal avgPrice;
    private BigDecimal medianPrice;
    private BigDecimal yearEndPrice;
}

package com.gis.housing.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;

@Data
@TableName("t_monthly_price")
public class MonthlyPrice {

    @TableId(type = IdType.AUTO)
    private Integer id;
    private String cityCode;
    private Short year;
    private Short month;
    private BigDecimal price;
}

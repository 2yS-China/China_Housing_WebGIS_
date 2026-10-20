package com.gis.housing.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@TableName("t_moran_result")
public class MoranResult {

    @TableId(type = IdType.AUTO)
    private Integer id;
    private Short analysisYear;
    private Short analysisMonth;
    private BigDecimal globalMoranI;
    private BigDecimal pValue;
    private BigDecimal zScore;
    private LocalDateTime createdAt;
}

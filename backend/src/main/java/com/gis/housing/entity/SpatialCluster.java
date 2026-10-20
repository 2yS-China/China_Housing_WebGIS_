package com.gis.housing.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("t_spatial_cluster")
public class SpatialCluster {

    @TableId(type = IdType.AUTO)
    private Integer id;
    private String taskName;
    private Short clusterK;
    private String cityCode;
    private Short clusterLabel;
    private String clusterDesc;
    private LocalDateTime createdAt;
}

package com.gis.housing.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("t_city_group")
public class CityGroup {

    @TableId(type = IdType.AUTO)
    private Integer groupId;
    private String groupName;
    private String description;
}

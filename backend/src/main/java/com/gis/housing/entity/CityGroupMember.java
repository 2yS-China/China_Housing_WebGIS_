package com.gis.housing.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("t_city_group_member")
public class CityGroupMember {

    @TableId(type = IdType.AUTO)
    private Integer id;
    private Integer groupId;
    private String cityCode;
}

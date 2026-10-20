package com.gis.housing;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.gis.housing.mapper")
public class ChinaHousingApplication {

    public static void main(String[] args) {
        SpringApplication.run(ChinaHousingApplication.class, args);
    }
}

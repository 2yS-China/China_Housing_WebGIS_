package com.gis.housing.service.impl;

import com.gis.housing.dto.CityPriceDTO;
import com.gis.housing.dto.FilterParam;
import com.gis.housing.mapper.CityMapper;
import com.gis.housing.service.ExportService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import java.io.OutputStream;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ExportServiceImpl implements ExportService {

    private final CityMapper cityMapper;

    @Override
    public void exportFilteredData(FilterParam param, HttpServletResponse response) {
        List<CityPriceDTO> data = cityMapper.filterCities(param);

        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("筛选结果");

            Row header = sheet.createRow(0);
            header.createCell(0).setCellValue("行政区划代码");
            header.createCell(1).setCellValue("城市");
            header.createCell(2).setCellValue("所属省份");
            header.createCell(3).setCellValue("经度");
            header.createCell(4).setCellValue("纬度");
            header.createCell(5).setCellValue("年份");
            header.createCell(6).setCellValue("月份");
            header.createCell(7).setCellValue("二手房均价(元/㎡)");

            for (int i = 0; i < data.size(); i++) {
                CityPriceDTO item = data.get(i);
                Row row = sheet.createRow(i + 1);
                row.createCell(0).setCellValue(item.getCityCode());
                row.createCell(1).setCellValue(item.getCityName());
                row.createCell(2).setCellValue(item.getProvinceName());
                row.createCell(3).setCellValue(item.getLon() != null ? item.getLon().doubleValue() : 0);
                row.createCell(4).setCellValue(item.getLat() != null ? item.getLat().doubleValue() : 0);
                row.createCell(5).setCellValue(item.getYear() != null ? item.getYear() : 0);
                row.createCell(6).setCellValue(item.getMonth() != null ? item.getMonth() : 0);
                row.createCell(7).setCellValue(item.getPrice() != null ? item.getPrice().doubleValue() : 0);
            }

            response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
            String fileName = URLEncoder.encode("房价筛选结果.xlsx", StandardCharsets.UTF_8);
            response.setHeader("Content-Disposition", "attachment;filename=" + fileName);

            OutputStream out = response.getOutputStream();
            workbook.write(out);
            out.flush();
        } catch (Exception e) {
            throw new RuntimeException("导出失败: " + e.getMessage(), e);
        }
    }
}

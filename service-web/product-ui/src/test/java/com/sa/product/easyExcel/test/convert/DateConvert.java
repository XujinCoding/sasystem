package com.sa.product.easyExcel.test.convert;

import com.alibaba.excel.converters.Converter;
import com.alibaba.excel.metadata.GlobalConfiguration;
import com.alibaba.excel.metadata.data.ReadCellData;
import com.alibaba.excel.metadata.data.WriteCellData;
import com.alibaba.excel.metadata.property.ExcelContentProperty;
import org.apache.poi.ss.usermodel.DateUtil;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;

public class DateConvert implements Converter<ZonedDateTime> {

    @Override
    public ZonedDateTime convertToJavaData(ReadCellData<?> cellData, ExcelContentProperty contentProperty,
                                           GlobalConfiguration globalConfiguration) {
        if (contentProperty == null || contentProperty.getDateTimeFormatProperty() == null) {
            return DateUtil.getLocalDateTime(cellData.getNumberValue().doubleValue(),
                    globalConfiguration.getUse1904windowing()).atZone(ZoneId.of("Africa/Abidjan"));//atZone(ZoneId.of("Africa/Abidjan"));

        } else {
             return DateUtil.getLocalDateTime(cellData.getNumberValue().doubleValue(),
                    contentProperty.getDateTimeFormatProperty().getUse1904windowing()).atZone(ZoneId.systemDefault());

        }
    }

    @Override
    public WriteCellData<?> convertToExcelData(ZonedDateTime value, ExcelContentProperty contentProperty,
                                               GlobalConfiguration globalConfiguration) {
        LocalDateTime localDateTime = value.toLocalDateTime();
        if (contentProperty == null || contentProperty.getDateTimeFormatProperty() == null) {
            return new WriteCellData<>(
                    BigDecimal.valueOf(DateUtil.getExcelDate(localDateTime, globalConfiguration.getUse1904windowing())));
        } else {
            return new WriteCellData<>(BigDecimal.valueOf(
                    DateUtil.getExcelDate(localDateTime, contentProperty.getDateTimeFormatProperty().getUse1904windowing())));
        }
    }
}

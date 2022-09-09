package com.sa.product.easyExcel.test.convert;

import com.alibaba.excel.converters.Converter;
import com.alibaba.excel.metadata.GlobalConfiguration;
import com.alibaba.excel.metadata.data.ReadCellData;
import com.alibaba.excel.metadata.data.WriteCellData;
import com.alibaba.excel.metadata.property.ExcelContentProperty;
import com.sa.product.easyExcel.test.dto.SexEnum;
import lombok.extern.slf4j.Slf4j;
import org.junit.platform.commons.util.StringUtils;


@Slf4j
public class EnumConvert implements Converter<SexEnum> {
    @Override
    public SexEnum convertToJavaData(ReadCellData<?> cellData, ExcelContentProperty contentProperty, GlobalConfiguration globalConfiguration) throws Exception {
        String stringValue = cellData.getStringValue();
        if (!StringUtils.isBlank(stringValue)){
            if (stringValue.equals(SexEnum.MAN.getName())){
                return SexEnum.MAN;
            }else if (stringValue.equals(SexEnum.WOMAN.getName())){
                return SexEnum.WOMAN;
            }
        }
        log.info("---------输入错误");
        return null;
    }

    @Override
    public WriteCellData<?> convertToExcelData(SexEnum value, ExcelContentProperty contentProperty, GlobalConfiguration globalConfiguration) throws Exception {
        return new WriteCellData<>(SexEnum.WOMAN.equals(value)? "女":"男");
    }
}

package com.sa.common.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;
import org.apache.poi.ss.formula.functions.T;

import java.util.List;
@Getter
@Setter
@ToString
@NoArgsConstructor
@Accessors(chain = true)
public class PageResult {
    private Integer pageNum;
    private Integer pageSize;
    private List<?> data;

    public PageResult(Integer pageNum, Integer pageSize, List<T> data) {
        this.pageNum = pageNum;
        this.pageSize = pageSize;
        this.data = data;
    }
}

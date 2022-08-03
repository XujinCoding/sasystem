package com.sa.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.apache.poi.ss.formula.functions.T;

import java.util.List;
@Getter
@Setter
@ToString
@NoArgsConstructor
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

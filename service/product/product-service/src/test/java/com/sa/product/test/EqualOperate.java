package com.sa.product.test;

import com.ql.util.express.Operator;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
@Slf4j
public class EqualOperate extends Operator {
    @Override
    public Object executeInner(Object[] list) throws Exception {
        Object opdata1 = list[0];
        Object opdata2 = list[1];
        if (opdata1 == null || opdata2 == null) {
            log.warn("EqualOperator error operator param is null...");
            return false;
        }
        if (opdata1 instanceof String) {
            if (opdata2 instanceof List) {
                for (Object data : (List) opdata2) {
                    if (String.valueOf(data).equalsIgnoreCase(String.valueOf(opdata1))) {
                        return true;
                    }
                }
                return false;
            } else {
                return ((String) opdata1).equalsIgnoreCase(String.valueOf(opdata2));
            }
        } else if (opdata1 instanceof List) {
            for (Object data : (List) opdata1) {
                if (String.valueOf(data).equalsIgnoreCase(String.valueOf(opdata2))) {
                    return true;
                }
            }
        } else if (opdata1 instanceof Number) {
            return String.valueOf(opdata1).equalsIgnoreCase(String.valueOf(opdata2));
        }
        return opdata1 == opdata2;
    }
}

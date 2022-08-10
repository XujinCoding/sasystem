package com.sa.handle;

import com.sa.dto.job.Operate;
import org.apache.ibatis.type.EnumOrdinalTypeHandler;
import org.apache.ibatis.type.MappedTypes;

@MappedTypes({Operate.class})
public class OperateHandle extends EnumOrdinalTypeHandler<Operate> {

    public OperateHandle(Class<Operate> type) {
        super(type);
    }
}

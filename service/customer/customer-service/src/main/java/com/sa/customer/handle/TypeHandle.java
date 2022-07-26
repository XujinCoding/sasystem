package com.sa.customer.handle;

import com.sa.common.dto.job.Type;
import org.apache.ibatis.type.EnumOrdinalTypeHandler;
import org.apache.ibatis.type.MappedTypes;

@MappedTypes({Type.class})
public class TypeHandle extends EnumOrdinalTypeHandler<Type> {

    public TypeHandle(Class<Type> type) {
        super(type);
    }
}

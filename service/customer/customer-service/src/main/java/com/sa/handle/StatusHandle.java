package com.sa.handle;

import com.sa.dto.job.Status;
import org.apache.ibatis.type.EnumOrdinalTypeHandler;
import org.apache.ibatis.type.MappedTypes;

@MappedTypes({Status.class})
public class StatusHandle extends EnumOrdinalTypeHandler<Status> {

    public StatusHandle(Class<Status> type) {
        super(type);
    }
}
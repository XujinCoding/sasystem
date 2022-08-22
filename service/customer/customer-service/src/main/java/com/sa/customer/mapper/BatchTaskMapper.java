package com.sa.customer.mapper;

import com.sa.common.dto.job.BatchTaskDTO;
import com.sa.common.mapper.ICustomFieldMap;
import com.sa.customer.domain.BatchTask;
import ma.glasnost.orika.CustomMapper;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * @author starttimesxj
 * BatchTask - > BatchTaskDTO
 */
@Component
public class BatchTaskMapper extends CustomMapper<BatchTask, BatchTaskDTO> implements ICustomFieldMap {
    private static final Map<String, String> FIELDS_MAPPING = new HashMap<>();
    static {
        FIELDS_MAPPING.put("taskId","taskId");
    }

    @Override
    public Map<String, String> getFieldMap() {
        return FIELDS_MAPPING;
    }
}

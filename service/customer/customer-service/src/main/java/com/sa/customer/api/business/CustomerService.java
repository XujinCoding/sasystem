package com.sa.customer.api.business;

import com.sa.common.dto.job.BatchTaskDTO;
import com.sa.common.dto.job.Status;
import com.sa.common.dto.job.TaskLevel;
import com.sa.common.utils.OrikaMapperUtils;
import com.sa.customer.domain.BatchTask;
import com.sa.customer.domain.Customer;
import com.sa.customer.domain.ProductInstance;
import com.sa.customer.dto.CustomerDTO;
import com.sa.customer.dto.ProductInstanceDTO;
import com.sa.customer.mapper.jpa.BatchTaskRepository;
import com.sa.customer.mapper.mybatis.CustomerMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Objects;

@Service("customerService")
public class CustomerService implements ICustomerService {

    @Autowired
    private CustomerMapper customerMapper;

    @Autowired
    private BatchTaskRepository batchTaskRepository;


    /**
     * 根据CustomerId查询CustomerDTO
     * @param customerId 客户ID
     * @return 客户
     */
    @Override
    public CustomerDTO findById(Long customerId) {
        Customer customer = customerMapper.findById(customerId);
        return OrikaMapperUtils.getOrikaMapperFaceCode().map(customer, CustomerDTO.class);
    }

    /**
     * 根据前端传递的客户产品关系映射,赋给时间之后入库
     * @param list 只包含 customerID 和 productId
     * @return
     */
    @Override
    public List<ProductInstanceDTO> buyProduct(List<ProductInstanceDTO> list) {
        List<ProductInstance> productInstances = OrikaMapperUtils.getOrikaMapperFaceCode().mapAsList(list, ProductInstance.class);
        productInstances.forEach((instant)->{
            instant.setCreateTime(ZonedDateTime.now());
            customerMapper.buyProduct(instant);
        });
        return list;
    }


    /**
     * 根据客户ID 查询客户购买的商品清单
     * @return
     */
    @Override
    public List<ProductInstanceDTO> findOfferByCustomerId(Long customerId) {
        return customerMapper.findOfferByCustomerId(customerId);
    }

    @Override
    public List<ProductInstanceDTO> getProductListByProduct(Long customerId) {
        return customerMapper.getProductListByProduct(customerId);
    }

    @Override
    public List<BatchTaskDTO> getTaskByStatus(Status status) {
        return customerMapper.getTaskByStatus(status);

    }

    @Override
    public BatchTaskDTO createAsynchronouslyTask(BatchTaskDTO batchTaskDTO) {
        batchTaskDTO.setState(Status.PREPARING);
        BatchTask map = OrikaMapperUtils.getOrikaMapperFaceCode().map(batchTaskDTO, BatchTask.class);
        if (Objects.isNull(map.getTaskLevel())){
            map.setTaskLevel(TaskLevel.DEFAULT);
        }
        BatchTask batchTask = batchTaskRepository.save(map);

        return OrikaMapperUtils.getOrikaMapperFaceCode().map(batchTask, BatchTaskDTO.class);
    }
}

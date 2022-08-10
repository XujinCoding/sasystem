package com.sa.customer.api.business.job;

import com.sa.domain.BatchTaskItem;
import com.sa.domain.Product;
import com.sa.dto.job.Operate;
import com.sa.mapper.ProductRepository;
import com.sa.mapper.mybaits.ProductMapper;
import com.sa.product.dto.ProductDTO;
import com.sa.utils.OrikaMapperUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;

@Component
@Slf4j
public class BatchAcceptExecuteProductJob {
    @Autowired
    ProductMapper productMapper;

    @Autowired
    ProductRepository productRepository;

    /**
     * 定时任务每100秒执行一次
     */
    @Scheduled(fixedDelay = 10000)
    public void execute() {
        List<BatchTaskItem> taskingList = productMapper.getAllTasking();
        taskingList.forEach((task -> {
            //查看任务的操作类型
            Operate type = productMapper.getTaskOperate(task.getTaskId());

            //根据不同的操作类型,进行不同的逻辑
            try {
                if (type == Operate.BATCH_ADD_PRODUCT){
                    //处理任务, 根据任务的类型判断处理逻辑
                    addProductService(task);

                    //根据这个拆分任务ID 修改这个拆分任务的状态
                    productMapper.changeTaskState(task.getId(),1,"");
                }
            }catch(Exception e){
                //捕捉所有错误,并打印出来
                e.printStackTrace();
                log.error("-------------执行任务出错");
                //报错就讲将这个状态设置为处理失败,报错信息添加进去
                productMapper.changeTaskState(task.getId(),-1,e.getMessage());
            }


        }));
    }

    private void addProductService(BatchTaskItem task) {
        ProductDTO productDTO = new ProductDTO();
        productDTO.setProductRemark(task.getProductRemark());
        productDTO.setProductNum(task.getProductNum());
        productDTO.setProductPrice(task.getProductPrice());
        productDTO.setProductName(task.getProductName());

        Product map = OrikaMapperUtils.getOrikaMapperFaceCode().map(productDTO, Product.class);
        //如果查到的是null那么就说明数据库中没有这个数据, 就将这个数据入库
        //如果查询到的是一个对象, 那么就说明数据库中有这个数据, 就将这个库存的数量进行累加
        Product p  = productMapper.getProductDistinct(map);
        if (Objects.nonNull(p)){
           //如果这个数据在数据库中存在
            throw new RuntimeException("商品重复异常");
        }else{
            //如果这个商品的信息在数据库中不存在就添加一个
            productRepository.save(map);
        }
    }
}

package com.sa.product.api.business;


import com.sa.common.dto.PageResult;
import com.sa.common.dto.job.BatchTaskDTO;
import com.sa.common.utils.OrikaMapperUtils;
import com.sa.product.conditon.ProductQueryCondition;
import com.sa.product.domain.Product;
import com.sa.product.dto.ProductDTO;
import com.sa.product.mapper.ProductRepository;
import com.sa.product.mapper.mybaits.ProductMapper;
import io.jsonwebtoken.lang.Assert;
import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.impl.DefaultMapperFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service("productService")
public class ProductService implements IProductService {
    @Autowired
    private ProductMapper productMapper;

    @Autowired
    private ProductRepository productRepository;
    @Override
    public List<ProductDTO> getAll() {
        //使用MyBatis进行查询
        List<Product> all = productMapper.getAll();

        //----------对象关系映射------------
//        //将product 映射到productDTO中
//        //普通方法
//        List<ProductDTO> result = new ArrayList<>();
//        all.forEach((p)->{
//            result.add(new ProductDTO().setProductId(p.getProductId()).setProductName(p.getProductName()).setProductPrice(p.getProductPrice()).setProductNum(p.getProductNum()).setProductRemark(p.getProductRemark()));
//        });
//        System.out.println(result);

        //使用Orika复制工具
        MapperFactory build = new DefaultMapperFactory.Builder().build();
        List<ProductDTO> productDTOS = build.getMapperFacade().mapAsList(all, ProductDTO.class);
        return productDTOS;
    }

    /**
     * 查询全部内容
     * 使用JPA自带的方法进行查询
     * @return 从数据库中查询的数据
     */
    public  List<ProductDTO>findAllByFunction(){
        List<Product> all1 = productRepository.findAll();
        all1.forEach((s)->{
            System.out.println(s.toString());
        });
        MapperFactory build = new DefaultMapperFactory.Builder().build();
        List<ProductDTO> productDTOS = build.getMapperFacade().mapAsList(all1, ProductDTO.class);
        return productDTOS;
    }
    public ProductDTO findById(Long productId) {
        Product byProductId = productRepository.findByProductId(1L);
        ProductDTO productDTO = OrikaMapperUtils.getOrikaMapperFaceCode().map(byProductId, ProductDTO.class);
        return productDTO;
    }



//        //使用JPA方法编写规范编写的方法
//        Product allByProductId = productRepository.findByProductId(1L);
//        System.out.println(allByProductId.toString());
//
//
//        //使用@Query(...)进行查询
//        Product product1 = productRepository.getProduct1(1L);
//        System.out.println(product1.toString());
//
//        分页查询
//        PageRequest pageRequest = PageRequest.of(0, 3);
//        Page<Product> all2 = productRepository.findAll(pageRequest);
//        System.out.println("分页查询");
//        all2.forEach((s)->{
//            System.out.println(s.toString());
//        });



    @Override
    public ProductDTO update(ProductDTO productDTO) {
        Assert.notNull(productDTO);
        MapperFactory build = new DefaultMapperFactory.Builder().build();
        Product product = build.getMapperFacade().map(productDTO, Product.class);
        //去数据库中查询商品, 看是否存在这个商品
        Product findProduct = productMapper.getProductById(product.getProductId());
        //如果存在这个商品,就去修改商品的属性,这样做就可以屏蔽save 新增的属性
        if (findProduct != null) {
            Product productResult = productRepository.save(product);
            //返回修改之后的数据
            return build.getMapperFacade().map(productResult, ProductDTO.class);
        }
        //如果数据库中没有这个商品就返回null 让控制器去判断.
        return null;
    }
    @Override
    public List<ProductDTO> saveAll(List<ProductDTO> data){
        List<Product> list = OrikaMapperUtils.getOrikaMapperFaceCode().mapAsList(data, Product.class);
        List<Product> listMid = productRepository.saveAll(list);
        List<ProductDTO> listReturn = OrikaMapperUtils.getOrikaMapperFaceCode().mapAsList(listMid, ProductDTO.class);
        return listReturn;
    }

    @Override
    public PageResult findByParameters(ProductQueryCondition condition){
        PageResult pageResult = new PageResult();
        List<Product> listMid = productMapper.findByParameters(condition);
        List<ProductDTO> listReturn = OrikaMapperUtils.getOrikaMapperFaceCode().mapAsList(listMid, ProductDTO.class);
        pageResult.setPageNum(condition.getPageNum());
        pageResult.setPageSize(condition.getPageSize());
        pageResult.setData(listReturn);
        return pageResult;
    }

    @Override
    public PageResult findByParametersUseJPA(ProductQueryCondition condition) {
        PageResult pageResult = new PageResult();
        List<Product> productList;
        Specification<Product> specification = new Specification<Product>() {//相当于一个过滤器,或者说是配置文件, 根据写的配置进行过滤
            @Override
            public Predicate toPredicate(Root<Product> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
                List<Predicate> list = new ArrayList<>();
                if (condition.getProductId()!=null){
                    Predicate equalProductId = criteriaBuilder.equal(root.get("productId").as(Integer.class), condition.getProductId());
                    list.add(equalProductId);
                }
                if (condition.getProductName()!=null && !condition.getProductName().equals("")){
                    Predicate equalProductName = criteriaBuilder.equal(root.get("productName").as(String.class), condition.getProductName());
                    list.add(equalProductName);
                }
                if (condition.getProductNum()!=null){
                    Predicate equalProductNum = criteriaBuilder.equal(root.get("productNum").as(Integer.class), condition.getProductNum());
                    list.add(equalProductNum);
                }
                if (condition.getProductPrice()!=null){
                    Predicate equalProductPrice = criteriaBuilder.equal(root.get("productPrice").as(Integer.class), condition.getProductPrice());
                    list.add(equalProductPrice);
                }
                if (condition.getProductRemark()!=null && !condition.getProductRemark().equals("")){
                    Predicate equalProductRemark = criteriaBuilder.equal(root.get("productRemark").as(String.class), condition.getProductRemark());
                    list.add(equalProductRemark);
                }
                Predicate[] pre = new Predicate[list.size()];
                return criteriaBuilder.and(list.toArray(pre));
            }
        };
        if (Objects.isNull(condition.getPageNum()) || Objects.isNull(condition.getPageSize())){
            productList = productRepository.findAll(specification);

        }else{
            PageRequest pageRequest = PageRequest.of(condition.getPageNum(), condition.getPageSize());
            Page<Product> productS = productRepository.findAll(specification,pageRequest);
            productList = productS.getContent();
        }
        List<ProductDTO> list = OrikaMapperUtils.getOrikaMapperFaceCode().mapAsList(productList, ProductDTO.class);
        pageResult.setPageNum(condition.getPageNum());
        pageResult.setPageSize(condition.getPageSize());
        pageResult.setData(list);

        return pageResult;
    }

    /**
     * 前端提交任务之后将任务入库
     * @param batchTaskDTO 定时任务的DTO
     * @return
     */

    @Override
    public boolean submitTask(BatchTaskDTO batchTaskDTO) {
        productMapper.submitTask(batchTaskDTO);
        return true;
    }


//    @Override
//    public List<ProductDTO> findByParameters(Long productId,String productName,Integer productPrice,Integer productNum,String productRemark){
//        Product product = new Product(productId,productName,productPrice,productNum,productRemark);
//        List<Product> listMid = productMapper.findByParameters(product);
//        List<ProductDTO> listReturn = OrikaMapperUtils.getOrikaMapperFaceCode().mapAsList(listMid, ProductDTO.class);
//        return listReturn;
//    }


}

package com.sa.product.api.business;


import com.sa.common.dto.PageResult;
import com.sa.common.mapper.BeanMapper;
import com.sa.product.conditon.ProductQueryCondition;
import com.sa.product.dao.ProductRepository;
import com.sa.product.dao.mybaits.ProductMapper;
import com.sa.product.domain.Product;
import com.sa.product.dto.ProductDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
public class ProductService implements IProductService {
    @Autowired
    private ProductMapper productMapper;

    @Autowired
    private BeanMapper beanMapper;

    @Autowired
    private ProductRepository productRepository;
    @Override
    public List<ProductDTO> getAll() {
        //----------对象关系映射------------
//        //将product 映射到productDTO中
//        //普通方法
//        List<ProductDTO> result = new ArrayList<>();
//        all.forEach((p)->{
//            result.add(new ProductDTO().setProductId(p.getProductId()).setProductName(p.getProductName()).setProductPrice(p.getProductPrice()).setProductNum(p.getProductNum()).setProductRemark(p.getProductRemark()));
//        });
//        System.out.println(result);

        //使用Orika复制工具
        return beanMapper.mapAsList(productMapper.getAll(), ProductDTO.class);
    }

    public ProductDTO findById(Long productId) {
        return beanMapper.map(productRepository.findByProductId(productId), ProductDTO.class);
    }

    @Override
    public ProductDTO update(ProductDTO productDTO) {
        //去数据库中查询商品, 看是否存在这个商品
        Product product = productMapper.getProductById(productDTO.getProductId());
        //TODO:-------null字段映射
        product.setProductNum(product.getProductNum()==null?product.getProductNum(): productDTO.getProductNum());
        product.setProductPrice(product.getProductPrice()==null?product.getProductPrice(): productDTO.getProductPrice());
        product.setProductRemark(product.getProductRemark()==null?product.getProductRemark(): productDTO.getProductRemark());
        product.setProductName(product.getProductName()==null?product.getProductName(): productDTO.getProductName());
        return beanMapper.map( productRepository.save(product), ProductDTO.class);
    }
    @Override
    public List<ProductDTO> saveAll(List<ProductDTO> data){
        List<Product> list = beanMapper.mapAsList(data, Product.class);
        return beanMapper.mapAsList( productRepository.saveAll(list), ProductDTO.class);
    }

    @Override
    public PageResult findByParameters(ProductQueryCondition condition){
        PageResult pageResult = new PageResult();
        List<ProductDTO> listReturn = beanMapper.mapAsList(productMapper.findByParameters(condition), ProductDTO.class);
        pageResult.setPageNum(condition.getPageNum())
                .setPageSize(condition.getPageSize())
                .setData(listReturn);
        return pageResult;
    }

    @Override
    public PageResult findByParametersUseJPA(ProductQueryCondition condition) {
        PageResult pageResult = new PageResult();
        List<Product> productList;
        //相当于一个过滤器,或者说是配置文件, 根据写的配置进行过滤
        Specification<Product> specification = new Specification<Product>() {
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
        List<Sort.Order> orders = new ArrayList<Sort.Order>();
        orders.add(new Sort.Order(Sort.Direction.ASC,"productId"));
        if (Objects.isNull(condition.getPageNum()) || Objects.isNull(condition.getPageSize())){
            productList = productRepository.findAll(specification,Sort.by(orders));
        }else{
            PageRequest pageRequest = PageRequest.of(condition.getPageNum(), condition.getPageSize(),Sort.by(orders));
            Page<Product> productS = productRepository.findAll(specification,pageRequest);
            productList = productS.getContent();
        }
        List<ProductDTO> list = beanMapper.mapAsList(productList, ProductDTO.class);
        pageResult.setPageNum(condition.getPageNum());
        pageResult.setPageSize(condition.getPageSize());
        pageResult.setData(list);
        return pageResult;
    }


}

package com.sa.service;


import com.sa.domain.Product;
import com.sa.mapper.ProductRepository;
import com.sa.mapper.mybaits.ProductMapper;
import com.sa.product.api.business.IProductService;
import com.sa.product.dto.ProductDTO;
import com.sa.utils.OrikaMapperUtils;
import io.jsonwebtoken.lang.Assert;
import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.impl.DefaultMapperFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

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
        System.out.println(productDTOS);
        System.out.println("service");
        return productDTOS;
    }
    //使用JPA自带的方法进行实现
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
//    public ProductDTO updateTest(ProductDTO productDTO){
//        //测试使用MyBatis 进行数据的修改
//
//        //测试使用@Query(...)进行修改
//
//
//
//    }

}

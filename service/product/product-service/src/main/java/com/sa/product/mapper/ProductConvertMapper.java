package com.sa.product.mapper;

import com.sa.common.mapper.ICustomFieldMap;
import com.sa.product.domain.Product;
import com.sa.product.dto.ProductDTO;
import ma.glasnost.orika.CustomMapper;
import ma.glasnost.orika.MappingContext;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * @author starttimesxj
 */
@Component
public class ProductConvertMapper extends CustomMapper<Product, ProductDTO> implements ICustomFieldMap {
    public static final Map<String,String> FILED_MAP = new HashMap<>();
    static {
        FILED_MAP.put("productNum","productNum");
    }
    @Override
    public Map<String, String> getFieldMap() {
        return FILED_MAP;
    }

    @Override
    public void mapAtoB(Product product, ProductDTO productDTO, MappingContext context) {
        if (Objects.nonNull(product)){
            productDTO.setProductName(product.getProductName());
        }
    }
}

package com.sa.product.easyExcel.test;

import com.google.common.cache.LoadingCache;
import com.sa.product.guava.cache.GuavaCache;
import com.sa.product.dto.ProductDTO;
import org.junit.Test;

public class DTOTest {


    @Test
    public void ss(){
        LoadingCache<Long, ProductDTO> cache = GuavaCache.getCache();
        LoadingCache<Long, ProductDTO> cache1 = GuavaCache.getCache();
        System.out.println(cache1==cache);
    }
}

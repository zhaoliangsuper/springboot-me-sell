package com.sell.dataobject.mapper;

import com.sell.dataobject.ProductCategory;
import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author 赵亮
 * @date 2018-06-11 17:46
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
public class ProductCategoryMapperTest
{

    @Autowired
    private ProductCategoryMapper mapper;

    @Test
    public void insertByMap()
    {
        Map<String, Object> map = new HashMap<>();
        map.put("categoryName", "最大热销");
        map.put("categoryType", 5);
        int result = mapper.insertByMap(map);
        Assert.assertEquals(1, result);
    }

    @Test
    public void findBycategoryType()
    {
        ProductCategory bycategoryType = mapper.findBycategoryType(5);
        Assert.assertNotNull(bycategoryType);
    }

    @Test
    public void findBycategoryName()
    {
        List<ProductCategory> bycategoryName = mapper.findBycategoryName("特价");
        Assert.assertNotNull(bycategoryName.size() != 0);
    }

    @Test
    public void updateByCategoryType()
    {
        int result = mapper.updateByCategoryType("测试特价", 5);
        Assert.assertEquals(1, result);
    }

    @Test
    public void updateByObject()
    {
        ProductCategory productCategory = new ProductCategory();
        productCategory.setCategoryType(5);
        productCategory.setCategoryName("测试用");
        int result = mapper.updateByObject(productCategory);
        Assert.assertEquals(1, result);
    }
}
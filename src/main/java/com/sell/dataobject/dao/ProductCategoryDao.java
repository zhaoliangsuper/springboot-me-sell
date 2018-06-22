package com.sell.dataobject.dao;

import com.sell.dataobject.mapper.ProductCategoryMapper;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Map;

/**
 * @author 赵亮
 * @date 2018-06-12 16:27
 */
public class ProductCategoryDao
{
    @Autowired
    ProductCategoryMapper mapper;
    public int insertByMap(Map<String, Object> map)
    {
        return mapper.insertByMap(map);
    }
}

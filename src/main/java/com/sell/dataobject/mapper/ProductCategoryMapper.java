package com.sell.dataobject.mapper;

import com.sell.dataobject.ProductCategory;
import org.apache.ibatis.annotations.*;

import java.util.List;
import java.util.Map;

/**
 * @author 赵亮
 * @date 2018-06-11 17:40
 */
public interface ProductCategoryMapper
{
    @Insert("insert into product_category(category_name,category_type) values(#{categoryName, jdbcType=VARCHAR},#{categoryType, jdbcType=INTEGER})")
    int insertByMap(Map<String, Object> map);

    @Insert("insert into product_category(category_name,category_type) values(#{categoryName, jdbcType=VARCHAR},#{categoryType, jdbcType=INTEGER})")
    int insertByProductCategory(ProductCategory productCategory);

    @Select("select * from product_category where category_type = #{categoryType}")
    @Results({
                     @Result(column = "category_type", property = "categoryType"),
                     @Result(column = "category_name", property = "categoryName"),
                     @Result(column = "category_id", property = "categoryId")
             })
    ProductCategory findBycategoryType(Integer categoryType);


    @Select("select * from product_category where category_name = #{categoryName}")
    @Results({
                     @Result(column = "category_type", property = "categoryType"),
                     @Result(column = "category_name", property = "categoryName"),
                     @Result(column = "category_id", property = "categoryId")
             })
    List<ProductCategory> findBycategoryName(String categoryName);

    @Update("update product_category set category_name = #{categoryName} where category_type = #{categoryType}")
    int updateByCategoryType(@Param("categoryName") String categoryName,
                             @Param("categoryType") Integer categoryType);

    @Update("update product_category set category_name = #{categoryName} where category_type = #{categoryType}")
    int updateByObject(ProductCategory productCategory);

    @Delete("delete from product_category where  category_type = #{categoryType}")
    int deleteByCategoryType(Integer categoryType);

    ProductCategory selectBycategoryType(String categoryType);
}

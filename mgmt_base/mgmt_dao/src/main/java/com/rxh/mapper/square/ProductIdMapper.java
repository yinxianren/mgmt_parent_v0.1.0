package com.rxh.mapper.square;

import com.rxh.square.pojo.ProductId;
import com.rxh.square.pojo.ProductIdExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface ProductIdMapper {
    int countByExample(ProductIdExample example);

    int deleteByExample(ProductIdExample example);

    int deleteByPrimaryKey(Integer productTypeId);

    int insert(ProductId record);

    int insertSelective(ProductId record);

    List<ProductId> selectByExample(ProductIdExample example);

    ProductId selectByPrimaryKey(Integer productTypeId);

    int updateByExampleSelective(@Param("record") ProductId record, @Param("example") ProductIdExample example);

    int updateByExample(@Param("record") ProductId record, @Param("example") ProductIdExample example);

    int updateByPrimaryKeySelective(ProductId record);

    int updateByPrimaryKey(ProductId record);
}
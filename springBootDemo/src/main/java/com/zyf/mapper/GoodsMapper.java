package com.zyf.mapper;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.zyf.entity.Goods;

public interface GoodsMapper {
	
	@Select("select * from goodmessage where goo_name = #{name}")
	Goods findByName(@Param("name") String name);
	
	@Insert("insert into goodmessage values(#{goodId}, #{goodName}, #{goodInPrice}, #{goodOutPrice}, #{goodStore})")
	int insertOne(@Param("goodId") String goodsId, @Param("goodName") String goodsName,
			@Param("goodInPrice") float goodInPrice, @Param("goodOutPrice") float goodOutPrice,
			@Param("goodStore") int goodStore);
}

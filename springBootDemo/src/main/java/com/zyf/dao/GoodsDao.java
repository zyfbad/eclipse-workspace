package com.zyf.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.zyf.entity.Goods;

public interface GoodsDao extends JpaRepository<Goods, String>{

}

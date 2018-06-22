package com.sell;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@MapperScan(basePackages = "com.sell.dataobject.mapper")////用到mybatis的时候用，这样他会扫描这个包下面的map文件
@EnableCaching//使用缓存
public class SellApplication
{

	public static void main(String[] args) {
		SpringApplication.run(SellApplication.class, args);
	}
}

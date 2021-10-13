package com.shop.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.query.Param;

import com.shop.entity.Item;
import java.lang.String;
import java.util.List;

public interface ItemRepository extends JpaRepository<Item, Long>, QuerydslPredicateExecutor<Item> { // <엔티티타입, 기본키타입>

	List<Item> findByItemNm(String itemnm); // 쿼리메서드

	List<Item> findByItemNmLike(String itemnm);

	List<Item> findByItemNmEndingWith(String name);

	List<Item> findByItemNmStartingWith(String name);

	List<Item> findByItemNmOrItemDetail(String itemNm, String itemDetail);

	List<Item> findByPriceLessThan(Integer price);

	List<Item> findByPriceLessThanOrderByPriceDesc(Integer price);

	@Query("select i from Item i where i.itemDetail like %:itemDetail% order by i.price desc")
	List<Item> findByItemDetail(@Param("itemDetail") String itemDetail);

	@Query(value = "select * from item i where i.item_detail like %:itemDetail% order by i.price desc", nativeQuery = true)
	List<Item> findByItemDetailByNative(@Param("itemDetail") String iotemDetail);
}

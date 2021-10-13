package com.shop.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.IntStream;

import javax.persistence.EntityManager;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.TestPropertySource;
import org.thymeleaf.util.StringUtils;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.QueryResults;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.shop.constant.ItemSellStatus;
import com.shop.entity.Item;
import com.shop.entity.QItem;

@SpringBootTest
@TestPropertySource(locations = "classpath:application-test.properties")
public class ItemRepositoryTest {

	@Autowired
	private ItemRepository itemRepository;

	@Autowired
	private EntityManager em;

	@Test
	@DisplayName("상품 저장 테스트")
	public void createItemTest() {

		Item item = new Item();
		item.setItemNm("테스트 상품");
		item.setPrice(10000);
		item.setItemDetail("테스트 상품 상세 설명");
		item.setItemSellStatus(ItemSellStatus.SELL);
		item.setStockNumber(100);
		item.setRegTime(LocalDateTime.now());
		item.setUpdateTime(LocalDateTime.now());
		Item savedItem = itemRepository.save(item);

		System.out.println(savedItem.toString());
	}

	public void createItemList() {
		IntStream.range(0, 10).forEach(i -> {
			Item item = new Item();
			item.setItemNm("테스트 상품" + i);
			item.setPrice(10000 + i);
			item.setItemDetail("테스트 상품 상세 설명" + i);
			item.setItemSellStatus(ItemSellStatus.SELL);
			item.setStockNumber(100);
			item.setRegTime(LocalDateTime.now());
			item.setUpdateTime(LocalDateTime.now());
			itemRepository.save(item);
		});
	}

	public void createItemList2() {
		IntStream.range(0, 5).forEach(i -> {
			Item item = new Item();
			item.setItemNm("테스트 상품" + i);
			item.setPrice(10000 + i);
			item.setItemDetail("테스트 상품 상세 설명" + i);
			item.setItemSellStatus(ItemSellStatus.SELL);
			item.setStockNumber(100);
			item.setRegTime(LocalDateTime.now());
			item.setUpdateTime(LocalDateTime.now());
			itemRepository.save(item);
		});

		IntStream.range(5, 10).forEach(i -> {
			Item item = new Item();
			item.setItemNm("테스트 상품" + i);
			item.setPrice(10000 + i);
			item.setItemDetail("테스트 상품 상세 설명" + i);
			item.setItemSellStatus(ItemSellStatus.SOLD_OUT);
			item.setStockNumber(0);
			item.setRegTime(LocalDateTime.now());
			item.setUpdateTime(LocalDateTime.now());
			itemRepository.save(item);
		});
	}

	@Test
	@DisplayName("상품명 조회 테스트")
	public void findByItemNmTest() {
		this.createItemList();
		List<Item> itemList = itemRepository.findByItemNmStartingWith("테스트 상품");

		itemList.forEach(item -> {
			System.out.println(item);
		});
	}

	@Test
	@DisplayName("상품명 혹은 삼품 디테일 조회 테스트")
	public void findByItemNmOrItemDetail() {
		this.createItemList();
		List<Item> itemList = itemRepository.findByItemNmOrItemDetail("테스트 상품3", "디테일");

		itemList.forEach(item -> {
			System.out.println(item);
		});
	}

	@Test
	@DisplayName("가격 LessThan 테스트")
	public void findByPriceLessThanTest() {
		this.createItemList();
		List<Item> itemList = itemRepository.findByPriceLessThan(10005);

		itemList.forEach(i -> {
			System.out.println(i);
		});
	}

	@Test
	@DisplayName("가격 내림차순 조회 테스트")
	public void findByPriceLessThanOrderByPriceDesc() {
		this.createItemList();
		List<Item> itemList = itemRepository.findByPriceLessThanOrderByPriceDesc(10005);

		itemList.forEach(i -> {
			System.out.println(i);
		});
	}

	@Test
	@DisplayName("@Query를 이용한 상품 조회 테스트")
	public void findByItemDetailTest() {
		this.createItemList();
		List<Item> itemList = itemRepository.findByItemDetail("테스트 상품 상세 설명");

		itemList.forEach(i -> {
			System.out.println(i);
		});
	}

	@Test
	@DisplayName("NativeQuery 속성을 이용한 상품 조회 테스트")
	public void findByItemDetailByNative() {
		this.createItemList();
		List<Item> itemList = itemRepository.findByItemDetailByNative("테스트 상품 상세 설명");

		itemList.forEach(i -> {
			System.out.println(i);
		});
	}

	@Test
	@DisplayName("Querydsl 조회 테스트1")
	public void queryDslTest() {
		this.createItemList();

		JPAQueryFactory qf = new JPAQueryFactory(em);
		QItem qItem = QItem.item;

		JPAQuery<Item> query = qf.selectFrom(qItem)//
				.where(qItem.itemSellStatus.eq(ItemSellStatus.SELL))//
				.where(qItem.itemDetail.like("%" + "테스트 상품 상세 설명" + "%"))//
				.orderBy(qItem.price.desc());

		List<Item> itemList = query.fetch();

		itemList.forEach(i -> {
			System.out.println(i);
		});

		QueryResults<Item> res = query.fetchResults();

		System.out.println("res.getTotal :: " + res.getTotal());
		res.getResults().forEach(i -> {
			System.out.println("res :: " + i);
		});
	}

	@Test
	@DisplayName("상품 Querydsl 조회 테스트2")
	public void queryDslTest2() {
		this.createItemList2();

		// BooleanBuilder는 쿼리에 들어갈 조건을 만들어주는 빌더 predicate를 구현한다.
		BooleanBuilder booleanBuilder = new BooleanBuilder();
		QItem item = QItem.item;

		String itemDetail = "테스트 상품 상세 설명";
		int price = 10003;
		String itemSellStat = "SELL";

		booleanBuilder.and(item.itemDetail.like("%" + itemDetail + "%"));
		booleanBuilder.and(item.price.gt(price));

		if (StringUtils.equals(itemSellStat, ItemSellStatus.SELL)) {
			booleanBuilder.and(item.itemSellStatus.eq(ItemSellStatus.SELL));
		}

		Pageable pageable = PageRequest.of(0, 5); // 0페이지를 조회 해달라 그리고 데이터는 5개를 보여 달라
		Page<Item> itemPagingResult = itemRepository.findAll(booleanBuilder, pageable);

		System.out.println("total elements: " + itemPagingResult.getTotalElements());

		List<Item> resultItemList = itemPagingResult.getContent();

		resultItemList.forEach(i -> {
			System.out.println(i);
		});

	}

}

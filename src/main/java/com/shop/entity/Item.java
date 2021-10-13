package com.shop.entity;

import com.shop.constant.ItemSellStatus;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;

@Entity
@Table(name = "item")
@Getter
@Setter
@ToString
public class Item {

	@Id
	@Column(name = "item_id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id; // 상품코드

	@Column(nullable = false, length = 50) // String 필드는 length가 default로 255이다.
	private String itemNm; // 상품명

	@Column(name = "price", nullable = false)
	private int price; // 가격

	@Column(nullable = false)
	private int stockNumber; // 재고수량

	@Lob
	@Column(nullable = false)
	private String itemDetail;// 상품 상세 설명

	@Enumerated(EnumType.STRING)
	private ItemSellStatus itemSellStatus; // 상품 판매 상태

	private LocalDateTime regTime; // 등록 시간

	private LocalDateTime updateTime; // 수정 시간

}
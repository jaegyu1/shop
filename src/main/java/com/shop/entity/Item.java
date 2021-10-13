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
	private Long id; // ��ǰ�ڵ�

	@Column(nullable = false, length = 50) // String �ʵ�� length�� default�� 255�̴�.
	private String itemNm; // ��ǰ��

	@Column(name = "price", nullable = false)
	private int price; // ����

	@Column(nullable = false)
	private int stockNumber; // ������

	@Lob
	@Column(nullable = false)
	private String itemDetail;// ��ǰ �� ����

	@Enumerated(EnumType.STRING)
	private ItemSellStatus itemSellStatus; // ��ǰ �Ǹ� ����

	private LocalDateTime regTime; // ��� �ð�

	private LocalDateTime updateTime; // ���� �ð�

}
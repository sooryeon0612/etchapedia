package com.etchapedia.pay;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.etchapedia.user.Users;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.SequenceGenerator;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class Orders {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "order_seq")
    @SequenceGenerator(name = "order_seq", sequenceName = "seq_order_idx", allocationSize = 1)
    private Integer orderIdx;

    @ManyToOne
    @JoinColumn(name = "user_idx")
    private Users user;               // 주문자 정보

    private long totalPrice;          // 주문 총액
    private LocalDateTime orderDate;  // 주문일

    private String summaryName;       // 주문 요약 이름

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderItem> items = new ArrayList<>();
}

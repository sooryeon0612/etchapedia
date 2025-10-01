package com.etchapedia.pay;

import com.etchapedia.book.Book;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.SequenceGenerator;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class OrderItem {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "order_item_seq")
    @SequenceGenerator(name = "order_item_seq", sequenceName = "seq_order_item_idx", allocationSize = 1)
    private Integer orderItemIdx;

    @ManyToOne
    @JoinColumn(name = "order_idx")
    private Orders order;

    @ManyToOne
    @JoinColumn(name = "book_idx")
    private Book book;

    private Integer quantity;
}

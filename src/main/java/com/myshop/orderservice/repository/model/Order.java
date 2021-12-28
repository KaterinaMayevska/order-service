package com.myshop.orderservice.repository.model;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import javax.persistence.*;
import java.util.Date;

@EnableAutoConfiguration
@Entity

@Table(name = "orders")
public final class Order {
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Long id;

    @Column(name="number")
    private int number;
    @Column(name = "price")
    private double price;
    @Column(name="order_date")
    private Date date;
    @Column(name="userId")
    private Long userId;
    @Column(name="itemId")
    private Long itemId;

    public Order() {
    }

    public Order(int number, double price, Date date, Long userId, Long itemId) {
        this.number = number;
        this.price = price;
        this.date = date;
        this.userId = userId;
        this.itemId = itemId;
    }

    public Long getId() {
        return id;
    }

    public int getNumber() {
        return number;
    }

    public double getPrice() {
        return price;
    }

    public Date getDate() {
        return date;
    }

    public Long getUserId() {
        return userId;
    }

    public Long getItemId() {
        return itemId;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public void setItemId(Long itemId) {
        this.itemId = itemId;
    }
}

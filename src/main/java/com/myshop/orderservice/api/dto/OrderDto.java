package com.myshop.orderservice.api.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
public class OrderDto {
    private Long userId;
    private Long itemId;
    private int number;
    private double price;
    private Date date;
}

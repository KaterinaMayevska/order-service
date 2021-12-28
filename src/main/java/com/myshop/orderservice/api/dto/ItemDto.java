package com.myshop.orderservice.api.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ItemDto {
    private String articul;
    private String name;
    private Double price;
}

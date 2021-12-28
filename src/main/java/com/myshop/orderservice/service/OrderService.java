package com.myshop.orderservice.service;
import com.myshop.orderservice.api.dto.ItemDto;
import com.myshop.orderservice.repository.model.Order;
import java.util.Date;
import java.util.List;



public interface OrderService {

    public List<Order> findAll();
    public Order create(Long itemId, Long userId,  int number, Date date) throws IllegalArgumentException;
    public void deleteById(Long id);
    public Order findById(Long id)throws IllegalArgumentException;
    public void update(Long id, Long itemId, Long userId, int number, Date date) throws IllegalArgumentException;
    public void delete(Long id) throws IllegalArgumentException;
    public ItemDto getItemByOrder (long id) throws IllegalArgumentException;


}

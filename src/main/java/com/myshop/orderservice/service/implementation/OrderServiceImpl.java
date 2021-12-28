package com.myshop.orderservice.service.implementation;

import com.myshop.orderservice.api.dto.ItemDto;
import com.myshop.orderservice.api.dto.UserDto;
import com.myshop.orderservice.repository.OrderRepository;
import com.myshop.orderservice.repository.model.Order;
import com.myshop.orderservice.repository.model.Role;
import com.myshop.orderservice.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;


import java.util.Date;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public final class OrderServiceImpl implements OrderService {
    private final String itemUrlAdress ="http://shop-service:8081/items";
    private final String userUrlAdress ="http://service-users:8083/users";
    @Autowired
    private OrderRepository orderRepo;

    public List<Order> findAll() {
        return orderRepo.findAll();
    }


    private boolean checkItem(Long itemId) {
        final RestTemplate restTemplate = new RestTemplate();
        final org.springframework.http.HttpEntity<Long> userRequest = new HttpEntity<>(itemId);

        final ResponseEntity<ItemDto> userResponse = restTemplate
                .exchange(itemUrlAdress + "/dto/" + itemId,
                        HttpMethod.GET, userRequest, ItemDto.class);

        return userResponse.getStatusCode() != HttpStatus.NOT_FOUND;
    }

    private boolean checkUser(Long userId) {
        final RestTemplate restTemplate = new RestTemplate();
        final HttpEntity<Long> userRequest = new HttpEntity<>(userId);

        final ResponseEntity<UserDto> userResponse = restTemplate
                .exchange(userUrlAdress + "/dto/" + userId,
                        HttpMethod.GET, userRequest, UserDto.class);

        return userResponse.getStatusCode() != HttpStatus.NOT_FOUND;
    }
    private boolean checkUserRole (Long userId){
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<UserDto> userDtoResponseEntity = restTemplate
                .exchange(userUrlAdress + "/dto/"+ userId.toString(),
                        HttpMethod.GET,
                        null,
                        new ParameterizedTypeReference<>() {
                        });
        UserDto userDto = userDtoResponseEntity.getBody();
        Role userRole = userDto.getRole();
        return (userRole == Role.CUSTOMER) ;

    }

    private double findPrice(int number, Long itemId){
        RestTemplate itemTemplate = new RestTemplate();
        ResponseEntity<ItemDto> itemDtoResponseEntity = itemTemplate
                .exchange(itemUrlAdress+ "/dto/" + itemId.toString(),
                        HttpMethod.GET,
                        null,
                        new ParameterizedTypeReference<>() {
                        });
        ItemDto itemDto = itemDtoResponseEntity.getBody();
        double orderPrice = itemDto.getPrice() * number;
        return orderPrice;
    }

    public Order create(Long itemId, Long userId,  int number, Date date) throws IllegalArgumentException{
        if(!checkItem(itemId)) throw new IllegalArgumentException("no such item");
        else if (!checkUser(userId)) throw new IllegalArgumentException("no such user");
        else{

            if(!checkUserRole(userId)) throw new IllegalArgumentException("this user is not a customer");
            else{
                double orderPrice = findPrice(number, itemId);
                Order order = new Order(number, orderPrice, date, userId, itemId);
                return orderRepo.save(order);}}
    }
    public void deleteById(Long id) {
        orderRepo.deleteById(id);

    }

    public Order findById(Long id)throws IllegalArgumentException{
        final Optional<Order> order = orderRepo.findById(id);
        if(order.isEmpty()) throw new RuntimeException("Impossible to do (Sorry)");
        else return order.get();
    }
    public void update(Long id, Long itemId, Long userId, int number, Date date) throws IllegalArgumentException{
        final Optional<Order> order = orderRepo.findById(id);
        if(order.isEmpty())throw new RuntimeException("Order not found (Sorry)");
        final Order orderReal = order.get();
        if (itemId != null && userId!=null) {
            if (!checkItem(itemId)) {
                throw new IllegalArgumentException("No such item");
            }
            if(!(checkUser(userId) && checkUserRole(userId))){
                throw new IllegalArgumentException("No such customer");
            }
            if(number > 0 ) orderReal.setNumber(number);
            orderReal.setDate(date);
            orderReal.setItemId(itemId);
            orderReal.setUserId(userId);
            orderReal.setPrice(findPrice(number, itemId));
            orderRepo.save(orderReal);
        }}

    public void delete(Long id) throws IllegalArgumentException{
        final Optional<Order> maybeOrder = orderRepo.findById(id);
        if(maybeOrder.isEmpty()) throw new RuntimeException("No such order (Sorry)");
        orderRepo.delete(maybeOrder.get());
    }


    public ItemDto getItemByOrder (long id) throws IllegalArgumentException{
        final Optional<Order> order = orderRepo.findById(id);
        if(order.isEmpty()) throw  new RuntimeException("No such order");
        else{
            Long itemId = order.get().getItemId();
            final RestTemplate restTemplate = new RestTemplate();
            final HttpEntity<Long> itemRequest = new HttpEntity<>(itemId);
            final ResponseEntity<ItemDto> itemResponse = restTemplate
                    .exchange(itemUrlAdress + "/dto/" + itemId,
                            HttpMethod.GET, itemRequest, ItemDto.class);

            if (itemResponse.getStatusCode() != HttpStatus.NOT_FOUND)
                return itemResponse.getBody();
            else
                throw new IllegalArgumentException("Item not found!");
        }
    }

}

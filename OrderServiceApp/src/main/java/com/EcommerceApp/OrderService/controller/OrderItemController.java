package com.EcommerceApp.OrderService.controller;

import com.EcommerceApp.OrderService.exception.*;
import com.EcommerceApp.OrderService.mapper.OrderItemMapper;
import com.EcommerceApp.OrderService.service.OrderItemService;
import org.dto.OrderItemDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/order/items")
public class OrderItemController {


    private final OrderItemService orderItemService;
    private final OrderItemMapper orderItemMapper;

    public OrderItemController(OrderItemService orderItemService, OrderItemMapper orderItemMapper) {
        this.orderItemService = orderItemService;
        this.orderItemMapper = orderItemMapper;
    }


    @GetMapping("/{orderId}")
    public ResponseEntity<List<OrderItemDTO>> getOrderItems(@PathVariable Integer orderId) {
        try {
            List<OrderItemDTO> orderItemDTOS = orderItemService.findByOrderId(orderId);
            return new ResponseEntity<>(orderItemDTOS, HttpStatus.OK);
        }catch (InvalidOrderIdException e){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        catch (Exception e){
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrderItemDTO> getOrderItem(@PathVariable Integer id) {
        try {
            OrderItemDTO orderItemDTO = orderItemService.getOrderItemById(id);
            return new ResponseEntity<>(orderItemDTO, HttpStatus.OK);
        }catch (InvalidOrderIdException | InvalidProductIdException e){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } catch (OrderItemNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }catch (Exception e){
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);

        }
    }

    // Update an order item
    @PutMapping("/{id}")
    public ResponseEntity<OrderItemDTO> updateOrderItem(@PathVariable Integer id, @RequestBody OrderItemDTO orderItemDTO) {
        try {
            OrderItemDTO updatedOrderItem = orderItemService.updateOrderItem(id, orderItemMapper.convertToEntity(orderItemDTO));
            return new ResponseEntity<>(updatedOrderItem, HttpStatus.OK);
        } catch (InvalidOrderIdException | InvalidProductIdException | OrderIdModificationException | ProductIdModificationException e){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }catch (OrderItemNotFoundException e){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Delete an order item by ID
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteOrderItem(@PathVariable Integer id) {
        try {
            orderItemService.deleteOrderItem(id);
            return new ResponseEntity<>("Order item deleted successfully", HttpStatus.NO_CONTENT);
        } catch (OrderItemNotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>("An error occurred while deleting the order item", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
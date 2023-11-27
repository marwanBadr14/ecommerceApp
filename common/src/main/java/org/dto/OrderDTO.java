package org.dto;

//import com.EcommerceApp.OrderService.Status;
import lombok.*;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderDTO {
    private Integer orderId;
    private Integer customerId;
    private String orderDate;
    private BigDecimal totalAmount;
    //private Status orderStatus;
    private List<OrderItemDTO> orderItems;
}

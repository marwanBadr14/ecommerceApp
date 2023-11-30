package org.dto;

//import com.EcommerceApp.OrderService.Status;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderDTO {
    private Integer orderId;
    private Integer customerId;
    private LocalDateTime orderDate;
    private BigDecimal totalAmount;
 //   private Status orderStatus;
    private List<OrderItemDTO> orderItems;

    @Override
    public String toString() {
        return "OrderDTO{" +
                "orderId=" + orderId +
                ", customerId=" + customerId +
                ", orderDate=" + orderDate +
                ", totalAmount=" + totalAmount +
                ", orderItems=" + orderItems +
                '}';
    }
}

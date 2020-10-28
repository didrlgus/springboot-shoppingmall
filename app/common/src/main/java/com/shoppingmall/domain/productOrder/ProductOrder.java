package com.shoppingmall.domain.productOrder;

import com.shoppingmall.common.BaseTimeEntity;
import com.shoppingmall.domain.cart.Cart;
import com.shoppingmall.domain.enums.OrderStatus;
import com.shoppingmall.domain.user.User;
import com.shoppingmall.dto.ProductOrderResponseDto;
import lombok.*;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@EntityListeners(value = {AuditingEntityListener.class})
public class ProductOrder extends BaseTimeEntity  {

    @Id     // primary key
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column
    private String orderNumber;
    @Column
    private String orderName;
    @Column
    private String deliveryMessage;
    @Column
    private OrderStatus orderStatus;
    @Column
    private String address;
    @Column
    private Character refundState;
    @Column
    private Integer amount;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;

    @OneToMany(mappedBy = "productOrder", cascade = CascadeType.ALL)
    private List<Cart> carts;

    public ProductOrderResponseDto toResponseDto() {
        LocalDateTime createdDate = this.getModifiedDate();

        return ProductOrderResponseDto.builder()
                .id(id)
                .orderNumber(orderNumber)
                .orderName(orderName)
                .deliveryMessage(deliveryMessage)
                .orderStatus(orderStatus.getValue())
                .address(address)
                .refundState(refundState)
                .amount(amount)
                .createdDate(createdDate.getYear() + "." + createdDate.getMonthValue() + "."
                        + createdDate.getDayOfMonth() + " " + createdDate.getHour() + ":" + createdDate.getMinute() + ":"
                        + createdDate.getSecond())
                .carts(carts)
                .build();
    }
}

package hr.abysalto.hiring.api.junior.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import org.springframework.data.annotation.AccessType;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.MappedCollection;
import org.springframework.data.relational.core.mapping.Table;

import lombok.Data; 

@Data
@AccessType(AccessType.Type.PROPERTY)
@Table("ORDER_TABLE")
public class Order {
    @Id
    private Long orderNr;
    private Long buyerId;
    private Buyer buyer;
    private Double totalAmount;

    @Transient
    private OrderStatus orderStatus;

    @Column("ORDER_STATUS")
    public String getStringOrderStatus() {
        if (this.orderStatus == null) {
            return null; 
        }
        return this.orderStatus.toString();
    }

    public void setStringOrderStatus(String orderStatusString) {
        if (orderStatusString == null || orderStatusString.trim().isEmpty() || orderStatusString.equals("string")) {
            this.orderStatus = null;
        } else {
            this.orderStatus = OrderStatus.fromString(orderStatusString);
        }
    }

    private LocalDateTime orderTime;
    
    private java.util.Set<OrderItem> orderItems;

    @MappedCollection(idColumn = "ORDER_NR")
    public java.util.Set<OrderItem> getOrderItems() {
        return this.orderItems;
    }

    public void setOrderItems(java.util.Set<OrderItem> orderItems) {
        this.orderItems = orderItems;
    }

    @Transient
    private PaymentOption paymentOption;

    @Column("PAYMENT_OPTION")
    public String getStringPaymentOption() {
        if (this.paymentOption == null) {
            return null; 
        }
        return this.paymentOption.toString();
    }

    public void setStringPaymentOption(String paymentOptionString) {
        if (paymentOptionString == null || paymentOptionString.trim().isEmpty() || paymentOptionString.equals("string")) {
            this.paymentOption = null;
        } else {
            this.paymentOption = PaymentOption.fromString(paymentOptionString);
        }
    }

    // ## method for calculating price
    public void calculateTotalAmount() {
        BigDecimal total = BigDecimal.ZERO; 
        
        if (this.orderItems != null) { 
            for (OrderItem item : this.orderItems) {
                if (item.getPrice() != null && item.getQuantity() != null) {
                    BigDecimal quantity = BigDecimal.valueOf(item.getQuantity());
                    BigDecimal itemTotal = item.getPrice().multiply(quantity);
                    total = total.add(itemTotal);
                }
            }
        }
        this.totalPrice = total; 
        this.totalAmount = total.doubleValue(); 
    }
    
    private String note;
    private Long deliveryAddressId;
    private BuyerAddress deliveryAddress;
    private String contactNumber;
    private String currency;
    private BigDecimal totalPrice;
}
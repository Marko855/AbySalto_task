package hr.abysalto.hiring.api.junior.model;

import java.math.BigDecimal;

import org.springframework.data.annotation.Id;

import lombok.Data;

@Data
public class OrderItem {
	@Id
	private Long orderItemId;
	private String name;
	private Short quantity;
	private BigDecimal price;
}

package hr.abysalto.hiring.api.junior.model;

import java.math.BigDecimal;

import lombok.Data;
import org.springframework.data.annotation.Id;

//automatski generira gettere i settere
@Data
public class OrderItem {
	@Id
	private Long orderItemId;
	private Long orderId;
	private Short itemNr;
	private String name;
	private Short quantity;
	private BigDecimal price;
}

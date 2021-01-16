package giga.koksy.app.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserOrderDto {
    private Long orderId;
    private boolean accepted;
}

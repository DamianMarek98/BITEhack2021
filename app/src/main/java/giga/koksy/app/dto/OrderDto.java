package giga.koksy.app.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderDto {
    private Long id;
    private String name;
    private String description;
    private String orderType;
}

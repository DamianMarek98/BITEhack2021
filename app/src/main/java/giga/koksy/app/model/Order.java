package giga.koksy.app.model;

import giga.koksy.app.enumerations.OrderType;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Entity(name = "bitehack_order")
@Setter
@Getter

public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 32)
    private String name;

    @Column(length = 512)
    private String description;

    @Column
    private OrderType orderType;

    @OneToMany(mappedBy = "order")
    private List<UserOrder> userOrders;
}
package giga.koksy.app.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity(name = "bitehack_user_order")
@Setter
@Getter

public class UserOrder {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private Boolean isAccepted;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;
}
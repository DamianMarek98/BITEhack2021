package giga.koksy.app.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Entity(name = "bitehack_user")
@Setter
@Getter
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 20)
    private String username;

    @Column(length = 20)
    private String password;

    @OneToMany(mappedBy = "user")
    List<UserOrder> userOrders;

    @OneToMany(mappedBy = "creator")
    List<Order> createdOrders;
}

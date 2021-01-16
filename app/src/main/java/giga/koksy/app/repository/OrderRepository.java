package giga.koksy.app.repository;

import giga.koksy.app.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

    @Query("SELECT o FROM bitehack_order o " +
            "LEFT JOIN bitehack_user_order uo on uo.id = o.id " +
            "WHERE uo.user.id=(:userId)")
    List<Order> findUserOrders(@Param(value = "userId") Long userId);
}
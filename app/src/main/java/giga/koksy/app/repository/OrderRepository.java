package giga.koksy.app.repository;

import giga.koksy.app.enumerations.OrderType;
import giga.koksy.app.model.Order;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

    @Query("SELECT o FROM bitehack_order o " +
            "LEFT JOIN bitehack_user_order uo on uo.order.id = o.id " +
            "WHERE uo.user.id=(:userId) ")
    List<Order> findUserOrders(@Param(value = "userId") Long userId);


    @Query("SELECT o FROM bitehack_order o " +
            "WHERE o.isAccepted = false " +
            "AND o.isFinished = false " +
            "AND o.creator.id <> (:userId) " +
            "AND o.id NOT IN (SELECT ou.order.id FROM bitehack_user_order ou WHERE ou.user.id=(:userId)) " +
            "ORDER BY o.creator.points DESC ")
    List<Order> findUnassignedOrders(@Param(value = "userId") Long userId, Pageable pageable);

    @Query("SELECT o FROM bitehack_order o " +
            "WHERE o.isAccepted = false " +
            "AND o.isFinished = false " +
            "AND o.creator.id <> (:userId) " +
            "AND o.orderType=(:orderType) " +
            "AND o.id NOT IN (SELECT ou.order.id FROM bitehack_user_order ou WHERE ou.user.id=(:userId)) " +
            "ORDER BY o.creator.points DESC ")
    List<Order> findUnassignedOrders(@Param(value = "userId") Long userId, @Param(value = "orderType") OrderType orderType, Pageable pageable);

    @Query("SELECT o FROM bitehack_order o WHERE o.creator.id=(:userId) AND o.isFinished = false ")
    List<Order> findCreatedUserOrders(@Param(value = "userId") Long userId);
}
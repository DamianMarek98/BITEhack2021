package giga.koksy.app.repository;

import giga.koksy.app.model.UserOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserOrderRepository extends JpaRepository<UserOrder, Long> {

    @Query("SELECT uo FROM bitehack_user_order uo WHERE uo.user.id=(:userId) AND uo.order.id=(:orderId)")
    Optional<UserOrder> findUserOrderByDetails(@Param("userId") Long userId, @Param("orderId") Long orderId);

}
package giga.koksy.app.repository;

import giga.koksy.app.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    @Query("SELECT u FROM bitehack_user u WHERE u.username=(:username) AND u.password=(:pwd)")
    Optional<User> findByDetails(@Param("username") String username, @Param("pwd") String password);

    Optional<User> findByUsername(String username);
}

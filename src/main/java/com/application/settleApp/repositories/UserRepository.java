package com.application.settleApp.repositories;

import com.application.settleApp.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {

  //  @Query(
  //      value =
  //          "SELECT * FROM user u INNER JOIN cost c ON u.user_id = c.user_id WHERE
  // c.product_id = :productId",
  //      nativeQuery = true)
  //  Optional<User> findUserByCostsId(@Param("productId") Long productId);

  User findByEmail(String email);
}

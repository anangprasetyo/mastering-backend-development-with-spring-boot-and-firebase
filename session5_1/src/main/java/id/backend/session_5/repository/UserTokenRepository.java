package id.backend.session_5.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import id.backend.session_5.model.UserToken;

@Repository
public interface UserTokenRepository extends JpaRepository<UserToken, Integer> {

    @Query("SELECT ut FROM UserToken ut WHERE ut.user.id = :user_id")
    Optional<UserToken> findByUserId(@Param("user_id") int user_id);

}

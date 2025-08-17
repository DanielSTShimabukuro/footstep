package footstep.footstep.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import footstep.footstep.models.User;

@Repository
public interface UserRepository extends JpaRepository<User, String> {
  Optional<User> findUserById(String id);
  Optional<User> findUserByUsername(String username);
  Optional<User> findUserByEmail(String email);
}

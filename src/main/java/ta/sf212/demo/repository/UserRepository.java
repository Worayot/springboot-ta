package ta.sf212.demo.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import ta.sf212.demo.model.User;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);
}

package chamapool.domain.member.repositories;

import chamapool.domain.member.models.PasswordResetToken;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PasswordResetTokenRepository extends JpaRepository<PasswordResetToken, Integer> {
  Optional<PasswordResetToken> findByToken(String token);
}

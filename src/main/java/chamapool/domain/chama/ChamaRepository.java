package chamapool.domain.chama;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ChamaRepository extends JpaRepository<Chama, Integer> {
  // There is only one chama in the system
  @Query("SELECT c FROM Chama c WHERE c.chamaId = 1")
  Optional<Chama> getChama();
}

package chamapool.domain.member.repositories;

import chamapool.domain.member.models.Occupation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OccupationRepository extends JpaRepository<Occupation, Integer> {}

package chamapool.domain.member.repositories;

import chamapool.domain.member.models.Address;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AddressRepository extends JpaRepository<Address, Integer> {}

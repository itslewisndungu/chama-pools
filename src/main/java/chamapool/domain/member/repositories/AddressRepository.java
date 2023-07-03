package chamapool.domain.member.repositories;

import chamapool.domain.member.models.Address;
import chamapool.domain.member.models.Member;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AddressRepository extends JpaRepository<Address, Integer> {
    Optional<Address> getAddressByMember(Member member);
}

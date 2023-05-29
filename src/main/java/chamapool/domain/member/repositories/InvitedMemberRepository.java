package chamapool.domain.member.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import chamapool.domain.member.models.InvitedMember;

public interface InvitedMemberRepository extends JpaRepository<InvitedMember, Integer> {
}

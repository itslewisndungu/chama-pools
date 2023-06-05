package chamapool.application.members;

import chamapool.application.members.requests.AcceptInvitationRequest;
import chamapool.application.members.requests.NewMemberRequest;
import chamapool.domain.member.VOs.InvitedMemberVO;
import chamapool.domain.member.VOs.MemberProfileVO;
import chamapool.domain.member.VOs.MemberVO;
import chamapool.domain.member.enums.Status;
import chamapool.domain.member.models.*;
import chamapool.domain.member.repositories.*;
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.NoSuchElementException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MembersService {
  private final InvitedMemberRepository invitedMemberRepository;
  private final MemberRepository memberRepository;
  private final PasswordEncoder passwordEncoder;
  private final NextOfKinRepository nextOfKinRepository;
  private final OccupationRepository occupationRepository;
  private final AddressRepository addressRepository;

  public InvitedMemberVO inviteMember(NewMemberRequest request) {
    InvitedMember member =
        new InvitedMember()
            // Personal info
            .firstName(request.firstName())
            .lastName(request.lastName())
            .nationalId(request.nationalId())
            .phoneNumber(request.phoneNumber())

            // Next of kin
            .nextOfKinFirstName(request.nextOfKinFirstName())
            .nextOfKinLastName(request.nextOfKinLastName())
            .nextOfKinMobileNumber(request.nextOfKinMobileNumber())
            .nextOfKinNationalId(request.nextOfKinNationalId())

            // Home location
            .constituency(request.constituency())
            .county(request.county())
            .subCounty(request.subCounty())

            // Occupation
            .organization(request.organization())
            .salary(request.salary())
            .position(request.position());

    var savedMember = invitedMemberRepository.save(member);

    return new InvitedMemberVO(savedMember);
  }

  @Transactional
  public MemberProfileVO acceptInvitation(Integer inviteId, AcceptInvitationRequest request) {
    var invitedMember = this.invitedMemberRepository.getReferenceById(inviteId);
    invitedMember.updateCredentials(request.username(), passwordEncoder.encode(request.password()));
    return new MemberProfileVO(this.registerMemberFromInvitation(invitedMember));
  }

  private Member registerMemberFromInvitation(InvitedMember invitedMember) {
    Member member =
        new Member()
            .status(Status.ACTIVE)
            .username(invitedMember.username())
            .password(invitedMember.password())
            .firstName(invitedMember.firstName())
            .lastName(invitedMember.lastName())
            .nationalId(invitedMember.nationalId())
            .phoneNumber(invitedMember.phoneNumber());

    NextOfKin kin =
        new NextOfKin()
            .firstName(invitedMember.nextOfKinFirstName())
            .lastName(invitedMember.nextOfKinLastName())
            .mobileNumber(invitedMember.nextOfKinMobileNumber())
            .nationalId(invitedMember.nextOfKinNationalId());
    this.nextOfKinRepository.save(kin);

    Address homeAddress =
        new Address()
            .constituency(invitedMember.constituency())
            .county(invitedMember.county())
            .subCounty(invitedMember.subCounty());
    this.addressRepository.save(homeAddress);

    Occupation occupation =
        new Occupation()
            .organization(invitedMember.organization())
            .salary(invitedMember.salary())
            .position(invitedMember.position());
    this.occupationRepository.save(occupation);

    member.nextOfKin(kin).homeAddress(homeAddress).occupation(occupation);

    return this.memberRepository.save(member);
  }

  public MemberProfileVO retrieveMemberProfile(String username) {
    return this.memberRepository
        .getMemberByUsername(username)
        .map(MemberProfileVO::new)
        .orElseThrow(
            () ->
                new NoSuchElementException(
                    "Member with username %s not found".formatted(username)));
  }

  public MemberVO retrieveMember(String username) {
    return this.memberRepository
        .getMemberByUsername(username)
        .map(MemberVO::new)
        .orElseThrow(
            () ->
                new NoSuchElementException(
                    "Member with username %s not found".formatted(username)));
  }

  public List<MemberVO> retrieveMembers() {
    return this.memberRepository.findAll().stream().map(MemberVO::new).toList();
  }

  public List<MemberProfileVO> retrieveMemberProfiles() {
    return this.memberRepository.findAll().stream().map(MemberProfileVO::new).toList();
  }

  public InvitedMemberVO getInvitation(Integer inviteId) {
    var invitedMember = this.invitedMemberRepository.getReferenceById(inviteId);
    return new InvitedMemberVO(invitedMember);
  }

  public List<InvitedMemberVO> getAllInvitations() {
    return this.invitedMemberRepository.findAll().stream().map(InvitedMemberVO::new).toList();
  }
}

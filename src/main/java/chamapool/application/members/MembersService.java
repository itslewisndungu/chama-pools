package chamapool.application.members;

import chamapool.application.members.requests.AcceptInvitationRequest;
import chamapool.application.members.requests.NewMemberRequest;
import chamapool.application.members.requests.PayMembershipFeeRequest;
import chamapool.application.transactions.TransactionsService;
import chamapool.domain.member.VOs.InvitedMemberVO;
import chamapool.domain.member.VOs.MemberProfileVO;
import chamapool.domain.member.VOs.MemberVO;
import chamapool.domain.member.VOs.MembershipFeeVO;
import chamapool.domain.member.enums.Status;
import chamapool.domain.member.models.*;
import chamapool.domain.member.repositories.*;
import chamapool.domain.transaction.TransactionType;
import jakarta.transaction.Transactional;
import java.time.LocalDate;
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
  private final MembershipFeeRepository membershipFeeRepository;
  private final TransactionsService transactionsService;

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
    var member = this.registerMemberFromInvitation(invitedMember);

    var fee = new MembershipFee().amount(this.calculateMembershipFee()).member(member);
    this.membershipFeeRepository.save(fee);
    this.memberRepository.save(member);

    return new MemberProfileVO(member);
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

  @Transactional
  public MembershipFeeVO payMembershipFee(Integer memberId, PayMembershipFeeRequest request) {
    var membershipFee = this.memberRepository.getReferenceById(memberId).membershipFee();
    membershipFee.amountPaid(membershipFee.amountPaid() + request.amount());
    this.membershipFeeRepository.save(membershipFee);

    if (membershipFee.balance() == 0) membershipFee.paymentDate(LocalDate.now());
    this.membershipFeeRepository.save(membershipFee);

    this.transactionsService.createTransaction(TransactionType.MEMBERSHIP_FEE, request.amount());

    return new MembershipFeeVO(membershipFee);
  }

  public MembershipFeeVO retrieveMemberMembershipFee(Integer memberId) {
    var member = this.memberRepository.getReferenceById(memberId);
    return this.retrieveMemberMembershipFee(member);
  }

  public MembershipFeeVO retrieveMemberMembershipFee(Member member) {
    var membershipFee = member.membershipFee();
    return new MembershipFeeVO(membershipFee);
  }

  private Double calculateMembershipFee() {
    return 15000.0;
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

    member = this.memberRepository.save(member);

    NextOfKin kin =
        new NextOfKin()
            .member(member)
            .firstName(invitedMember.nextOfKinFirstName())
            .lastName(invitedMember.nextOfKinLastName())
            .mobileNumber(invitedMember.nextOfKinMobileNumber())
            .nationalId(invitedMember.nextOfKinNationalId());
    this.nextOfKinRepository.save(kin);

    Address homeAddress =
        new Address()
            .member(member)
            .constituency(invitedMember.constituency())
            .county(invitedMember.county())
            .subCounty(invitedMember.subCounty());
    this.addressRepository.save(homeAddress);

    Occupation occupation =
        new Occupation()
            .member(member)
            .organization(invitedMember.organization())
            .salary(invitedMember.salary())
            .position(invitedMember.position());
    this.occupationRepository.save(occupation);

    return member;
  }
}

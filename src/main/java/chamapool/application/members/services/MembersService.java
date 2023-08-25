package chamapool.application.members.services;

import chamapool.application.members.requests.AcceptInvitationRequest;
import chamapool.application.members.requests.NewMemberRequest;
import chamapool.application.members.requests.PayBulkMembershipFeesRequest;
import chamapool.application.members.requests.PayMembershipFeeRequest;
import chamapool.application.notifications.NotificationsService;
import chamapool.application.sms.MessageService;
import chamapool.application.transactions.TransactionsService;
import chamapool.domain.member.VOs.*;
import chamapool.domain.member.enums.MembershipFeeStatus;
import chamapool.domain.member.enums.Status;
import chamapool.domain.member.models.*;
import chamapool.domain.member.repositories.*;
import chamapool.domain.notifications.models.Notification;
import chamapool.domain.notifications.models.NotificationType;
import chamapool.domain.transaction.TransactionType;
import jakarta.transaction.Transactional;
import java.time.LocalDate;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;
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

  private final MessageService messageService;
  private final TransactionsService transactionsService;
  private final NotificationsService notificationsService;

  public InvitedMemberVO inviteMember(NewMemberRequest request) {
    var username = request.firstName() + "." + request.lastName();

    InvitedMember member =
        new InvitedMember()
            .firstName(request.firstName())
            .lastName(request.lastName())
            .nationalId(request.nationalId())
            .phoneNumber(request.phoneNumber())
            .username(username);
    var savedMember = invitedMemberRepository.save(member);

    var notification =
        new Notification()
            .message(
                "%s %s has successfully invited  to join Vision Ahead."
                    .formatted(request.firstName(), request.lastName()))
            .title("Invitation to join Vision Ahead")
            .relatedId(savedMember.id())
            .type(NotificationType.MEMBER_INVITATION);

    this.notificationsService.sendAdminNotification(notification);
    this.messageService.sendSmsMessage(
        ("You have been invited to join Vision Ahead."
                + " Please visit http://localhost:3000/accept-invitation/%d to accept the invitation")
            .formatted(savedMember.id()),
        new String[] {savedMember.phoneNumber()});

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

    var memberNotification =
        new Notification()
            .message(
                "Welcome to the Vision Ahead,%s. You have an outstanding membership fee of %.2f that you need to pay to start receiving loans. More info will be presented during the next meeting"
                    .formatted(member.fullName(), fee.amount()))
            .title("Welcome to Vision Ahead")
            .relatedId(member.id())
            .type(NotificationType.MEMBER_INVITATION);

    var adminNotification =
        new Notification()
            .message(
                "%s %s has successfully accepted the invitation to join Vision Ahead."
                    .formatted(member.firstName(), member.lastName()))
            .title("Invitation to join Vision Ahead")
            .relatedId(member.id())
            .type(NotificationType.MEMBER_INVITATION);

    this.notificationsService.sendAdminNotification(adminNotification);
    this.notificationsService.sendMemberNotification(member, memberNotification);

    return new MemberProfileVO(member);
  }

  public MemberProfileVO retrieveMemberProfile(String username) {
    var member = this.getMemberByUsername(username);
    return new MemberProfileVO(member);
  }

  public List<MemberVO> retrieveMembers() {
    return this.memberRepository.findAll().stream().map(MemberVO::new).toList();
  }

  public InvitedMemberVO getInvitation(Integer inviteId) {
    var invitedMember = this.invitedMemberRepository.getReferenceById(inviteId);
    return new InvitedMemberVO(invitedMember);
  }

  public List<InvitedMemberVO> getAllInvitations() {
    return this.invitedMemberRepository.findAll().stream().map(InvitedMemberVO::new).toList();
  }

  public MembershipFeeVO retrieveMemberMembershipFee(Integer memberId) {
    var member = this.memberRepository.getReferenceById(memberId);
    return this.retrieveMemberMembershipFee(member);
  }

  public MembershipFeeVO retrieveMemberMembershipFee(Member member) {
    var membershipFee = member.membershipFee();
    return new MembershipFeeVO(membershipFee);
  }

  public NextOfKinVO retrieveMemberNextOfKin(String username) {
    var member = this.getMemberByUsername(username);

    return this.nextOfKinRepository
        .getNextOfKinByMember(member)
        .map(NextOfKinVO::new)
        .orElseThrow(
            () -> new NoSuchElementException("Kins for member %s not found".formatted(username)));
  }

  public OccupationVO retrieveMemberOccupation(String username) {
    var member = this.getMemberByUsername(username);

    return this.occupationRepository
        .getOccupationByMember(member)
        .map(OccupationVO::new)
        .orElseThrow(
            () ->
                new NoSuchElementException(
                    "Occupation for member %s not found".formatted(username)));
  }

  public AddressVO retrieveMemberAddress(String username) {
    var member = this.getMemberByUsername(username);

    return this.addressRepository
        .getAddressByMember(member)
        .map(AddressVO::new)
        .orElseThrow(
            () ->
                new NoSuchElementException("Address for member %s not found".formatted(username)));
  }

  public List<MembershipFeeVO> retrieveMembersWithOutstandingMembershipFees() {
    return this.membershipFeeRepository.getOutstandingMembershipFees().stream()
        .map(MembershipFeeVO::new)
        .collect(Collectors.toList());
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

  private Member getMemberByUsername(String username) {
    return this.memberRepository
        .getMemberByUsername(username)
        .orElseThrow(
            () ->
                new NoSuchElementException(
                    "Member with username %s not found".formatted(username)));
  }

  @Transactional
  public MembershipFeeVO payMembershipFee(Integer memberId, PayMembershipFeeRequest request) {
    var membershipFee = this.payMemberMembershipFee(memberId, request.amount());
    return new MembershipFeeVO(membershipFee);
  }

  @Transactional
  public void payBulkMembershipFees(PayBulkMembershipFeesRequest request) {
    var repayments = request.feePayments().stream().filter(fee -> fee.amount() > 0).toList();

    for (var repayment : repayments) {
      this.payMemberMembershipFee(repayment.memberId(), repayment.amount());
    }
  }

  private MembershipFee payMemberMembershipFee(Integer memberId, Double amount) {
    var membershipFee = this.memberRepository.getReferenceById(memberId).membershipFee();
    membershipFee.amountPaid(membershipFee.amountPaid() + amount);
    this.membershipFeeRepository.save(membershipFee);

    if (membershipFee.balance() <= 0) membershipFee.paymentDate(LocalDate.now());
    this.membershipFeeRepository.save(membershipFee);

    this.transactionsService.createTransaction(
        TransactionType.MEMBERSHIP_FEE,
        amount,
        "Record membership fees for member %s".formatted(membershipFee.member().fullName()));

    var notification =
        new Notification()
            .title("Membership Fee Payment")
            .relatedId(memberId)
            .type(NotificationType.MEMBERSHIP_FEE)
            .message(
                "Your membership fee of KES %.2f has been received.".formatted(amount)
                    + (membershipFee.status() == MembershipFeeStatus.PAID
                        ? "You have fully paid your dues"
                        : "You have a balance of KES %.2f".formatted(membershipFee.balance())));

    this.notificationsService.sendMemberNotification(membershipFee.member(), notification);

    return membershipFee;
  }
}

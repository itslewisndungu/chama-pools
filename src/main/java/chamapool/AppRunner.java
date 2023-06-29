package chamapool;

import chamapool.domain.chama.Chama;
import chamapool.domain.chama.ChamaRepository;
import chamapool.domain.loans.Loan;
import chamapool.domain.loans.enums.LoanStatus;
import chamapool.domain.loans.repositories.LoanRepository;
import chamapool.domain.meeting.enums.MeetingCategory;
import chamapool.domain.meeting.models.Meeting;
import chamapool.domain.meeting.models.MeetingAttendance;
import chamapool.domain.meeting.models.MeetingContribution;
import chamapool.domain.meeting.repositories.MeetingAttendanceRepository;
import chamapool.domain.meeting.repositories.MeetingContributionRepository;
import chamapool.domain.meeting.repositories.MeetingRepository;
import chamapool.domain.member.enums.MemberRole;
import chamapool.domain.member.enums.Status;
import chamapool.domain.member.models.*;
import chamapool.domain.member.repositories.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class AppRunner implements CommandLineRunner {
  private final MeetingRepository meetingRepository;
  private final MemberRepository memberRepository;
  private final PasswordEncoder passwordEncoder;
  private final NextOfKinRepository nextOfKinRepository;
  private final AddressRepository addressRepository;
  private final OccupationRepository occupationRepository;
  private final RoleRepository roleRepository;
  private final ChamaRepository chamaRepository;
  private final LoanRepository loanRepository;
  private final MeetingAttendanceRepository attendanceRepository;
  private final MeetingContributionRepository contributionRepository;

  Random random = new Random();

  @Override
  public void run(String... args) {
    this.generateMembers();
    this.generateChamaDetails();
  }

  private void generateChamaDetails() {
    Chama visionAhead =
        new Chama().name("Vision Ahead").accountBalance(BigDecimal.valueOf(200_000));

    this.chamaRepository.save(visionAhead);
  }

  private void generateMembers() {
    log.info("Generating a test chairman, treasurer, secretary and member");

    log.info("Generating boilerplate roles...");
    Role chairmanRole = new Role().name(MemberRole.CHAIRMAN);
    Role memberRole = new Role().name(MemberRole.MEMBER);
    Role treasurerRole = new Role().name(MemberRole.TREASURER);
    Role secretaryRole = new Role().name(MemberRole.SECRETARY);

    roleRepository.saveAll(List.of(chairmanRole, memberRole, treasurerRole, secretaryRole));

    log.info("Generating boilerplate next of kin, home address and occupation");

    NextOfKin kin =
        new NextOfKin().firstName("Kimani").lastName("Wanjiku").mobileNumber("075478963214");
    this.nextOfKinRepository.save(kin);

    Address homeAddress =
        new Address().constituency("Kiambaa").county("Kiambu").subCounty("Kiambaa");
    this.addressRepository.save(homeAddress);

    Occupation occupation =
        new Occupation().organization("Equity Bank").salary(40000.0).position("Operations Manager");
    this.occupationRepository.save(occupation);

    log.info("Generating a new Chairman...");

    Member chairman =
        new Member()
            .status(Status.ACTIVE)
            .username("chairman")
            .password(passwordEncoder.encode("9326Kalewi"))
            .firstName("Bwana")
            .lastName("Chairman")
            .nationalId("32454323")
            .phoneNumber("8430570482")
            .addRoles(chairmanRole, memberRole);

    chairman.nextOfKin(kin).homeAddress(homeAddress).occupation(occupation);

    log.info("Generating a new member...");
    Member member =
        new Member()
            .status(Status.ACTIVE)
            .username("member")
            .password(passwordEncoder.encode("9326Kalewi"))
            .firstName("SR")
            .lastName("Lewis")
            .nationalId("38259057")
            .phoneNumber("047896358974")
            .addRoles(memberRole);

    Occupation position =
        new Occupation().organization("Equity Bank").salary(40000.0).position("Operations Manager");
    this.occupationRepository.save(position);

    member.nextOfKin(kin).homeAddress(homeAddress).occupation(position);

    log.info("Generating a new treasurer...");
    var treasurer =
        new Member()
            .status(Status.ACTIVE)
            .username("treasurer")
            .password(passwordEncoder.encode("9326Kalewi"))
            .firstName("Treasurer")
            .lastName("Lewis")
            .nationalId("34259057")
            .phoneNumber("147896358974")
            .addRoles(treasurerRole);

    treasurer.nextOfKin(kin).homeAddress(homeAddress).occupation(position);

    log.info("Generating a new secretary...");
    var secretary =
        new Member()
            .status(Status.ACTIVE)
            .username("secretary")
            .password(passwordEncoder.encode("9326Kalewi"))
            .firstName("Secretary")
            .lastName("Lewis")
            .nationalId("37259057")
            .phoneNumber("347896358974")
            .addRoles(secretaryRole);

    secretary.nextOfKin(kin).homeAddress(homeAddress).occupation(position);

    var members = List.of(chairman, member, treasurer, secretary);
    memberRepository.saveAll(members);

    log.info("Generating a new loans ..");
    Loan loan =
        new Loan()
            .reasonForLoan("To buy a car")
            .amount(100000.0)
            .member(chairman)
            .status(LoanStatus.AWAITING_DISBURSEMENT);

    Loan loan2 =
        new Loan()
            .reasonForLoan("To buy a car")
            .amount(100000.0)
            .member(member)
            .startDate(LocalDate.now())
            .status(LoanStatus.ACTIVE);

    loanRepository.saveAll(List.of(loan, loan2));

    log.info("Generating a test meeting");

    var meeting1 =
        new Meeting()
            .meetingDate(LocalDate.now().minusMonths(2))
            .agenda("What is going on")
            .title("July meeting")
            .category(MeetingCategory.MONTHLY_MEETING);

    var meeting2 =
        new Meeting()
            .meetingDate(LocalDate.now().minusMonths(3))
            .agenda("What is going on")
            .title("May meeting")
            .category(MeetingCategory.MONTHLY_MEETING);

    var meeting3 =
        new Meeting()
            .meetingDate(LocalDate.now().minusMonths(1))
            .agenda("What is going on")
            .title("June meeting")
            .category(MeetingCategory.MONTHLY_MEETING);

    var emergencyMeeting =
        new Meeting()
            .meetingDate(LocalDate.now().minusDays(1))
            .agenda("Mama nani is sick af, we want to contribute at least 5000 each")
            .title("Emergency meeting")
            .category(MeetingCategory.EMERGENCY);

    var meetings = List.of(meeting1, meeting2, meeting3, emergencyMeeting);
    meetingRepository.saveAll(meetings);

    for (Meeting meeting : meetings) {
      var meetingAttendances = new ArrayList<MeetingAttendance>();
      var meetingContributions = new ArrayList<MeetingContribution>();

      members.forEach(
          m -> {
            var contribution = new MeetingContribution().member(m).meeting(meeting);
            var attendance = new MeetingAttendance().member(m).meeting(meeting);

            var rand = random.nextInt(3);
            switch (rand) {
              case 0 -> {
                contribution.amount(1500.0);
                attendance.isPresent(true);
              }
              case 1 -> {
                attendance.isPresent(false);
                contribution.amount(0.0);
              }
              case 2 -> {
                attendance.isPresent(false).apology("Feeling sick");
                contribution.amount(1500.0);
              }
            }

            meetingContributions.add(contribution);
            meetingAttendances.add(attendance);
          });

      contributionRepository.saveAll(meetingContributions);
      attendanceRepository.saveAll(meetingAttendances);
    }
  }
}

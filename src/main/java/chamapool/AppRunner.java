package chamapool;

import chamapool.domain.chama.Chama;
import chamapool.domain.chama.ChamaRepository;
import chamapool.domain.member.enums.MemberRole;
import chamapool.domain.member.enums.Status;
import chamapool.domain.member.models.*;
import chamapool.domain.member.repositories.*;
import java.math.BigDecimal;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class AppRunner implements CommandLineRunner {
  private final MemberRepository memberRepository;
  private final PasswordEncoder passwordEncoder;
  private final NextOfKinRepository nextOfKinRepository;
  private final AddressRepository addressRepository;
  private final OccupationRepository occupationRepository;
  private final RoleRepository roleRepository;
  private final ChamaRepository chamaRepository;

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
    memberRepository.save(chairman);

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
    memberRepository.save(member);

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
    memberRepository.save(treasurer);

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
    memberRepository.save(secretary);

    log.info("Done");
  }
}

package chamapool.domain.member.models;

import chamapool.domain.member.enums.MemberRole;
import chamapool.domain.member.enums.Status;
import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@Setter
@Getter
@EntityListeners(AuditingEntityListener.class)
@Accessors(chain = true, fluent = true)
public class Member {
  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE)
  private Integer id;

  private String firstName;

  private String lastName;

  @Column(unique = true)
  private String username;

  @Column(name = "national_id", unique = true)
  private String nationalId;

  @Column(name = "phone_number", unique = true)
  private String phoneNumber;

  @CreatedDate private LocalDate joinedOn;

  @Enumerated(EnumType.STRING)
  private Status status;

  private String password;
  private LocalDate dateOfBirth;

  @Transient private String token;

  @ManyToMany(fetch = FetchType.EAGER)
  @JoinTable(
      name = "member_roles",
      joinColumns = @JoinColumn(name = "member_id"),
      inverseJoinColumns = @JoinColumn(name = "role_id"))
  private Set<Role> roles = new HashSet<>();

  @OneToOne
  @JoinColumn(name = "membership_fee_id")
  private MembershipFee membershipFee;

  public Set<MemberRole> getRoles() {
    return this.roles().stream().map(Role::name).collect(Collectors.toSet());
  }

  public Member addRoles(Role... roles) {
    this.roles.addAll(Arrays.asList(roles));
    return this;
  }

  public String fullName() {
    return this.firstName() + " " + this.lastName();
  }
}

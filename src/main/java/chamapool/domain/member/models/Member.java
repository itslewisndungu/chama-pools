package chamapool.domain.member.models;

import chamapool.domain.member.enums.Status;
import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
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
  @GeneratedValue(strategy = GenerationType.IDENTITY)
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

  @Transient private String token;

  @ManyToMany(fetch = FetchType.EAGER)
  @JoinTable(
      name = "member_roles",
      joinColumns = @JoinColumn(name = "member_id"),
      inverseJoinColumns = @JoinColumn(name = "role_id"))
  private Set<Role> roles = new HashSet<>();

  @OneToOne
  @JoinColumn(name = "next_of_kin_id")
  private NextOfKin nextOfKin;

  @OneToOne
  @JoinColumn(name = "occupation_id")
  private Occupation occupation;

  @OneToOne
  @JoinColumn(name = "address_id")
  private Address homeAddress;

  public Set<String> getRoles() {
    return this.roles().stream().map(Role::name).collect(Collectors.toSet());
  }

  public Member addRoles(Role... roles) {
    this.roles.addAll(Arrays.asList(roles));
    return this;
  }
}

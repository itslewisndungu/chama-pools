package chamapool.domain.member.models;

import jakarta.persistence.*;
import java.time.LocalDate;
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
public class InvitedMember {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

  private String firstName;
  private String lastName;
  private String username;

  @Column(name = "national_id", unique = true)
  private String nationalId;

  @Column(name = "phone_umber", unique = true)
  private String phoneNumber;

  @CreatedDate private LocalDate invitedOn;

  private String position;

  private String organization;

  private Double salary;

  @Column(name = "kin_national_id", unique = true)
  private String nextOfKinNationalId;

  @Column(name = "kin_first_name", unique = true)
  private String nextOfKinFirstName;

  @Column(name = "kin_last_name", unique = true)
  private String nextOfKinLastName;

  @Column(name = "kin_mobile_number", unique = true)
  private String nextOfKinMobileNumber;

  private String county;

  private String subCounty;

  private String constituency;

  @Transient private String password;

  public String generatedUsername() {
    return this.firstName().strip().toLowerCase() + "." + this.lastName().strip().toLowerCase();
  }

  public InvitedMember updateCredentials(String username, String password) {
    return this.username(username).password(password);
  }
}

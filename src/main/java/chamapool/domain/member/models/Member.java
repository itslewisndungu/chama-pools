package chamapool.domain.member.models;

import chamapool.domain.member.enums.Role;
import chamapool.domain.member.enums.Status;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDate;

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

    @CreatedDate
    private LocalDate joinedOn;

    @Enumerated(EnumType.STRING)
    private Status status;

    private String password;

    @Enumerated(EnumType.STRING)
    @Column()
    private Role role;

    @Transient
    private String token;

    @OneToOne
    @JoinColumn(name = "next_of_kin_id")
    private NextOfKin nextOfKin;

    @OneToOne
    @JoinColumn(name = "occupation_id")
    private Occupation occupation;

    @OneToOne
    @JoinColumn(name = "address_id")
    private Address homeAddress;
}


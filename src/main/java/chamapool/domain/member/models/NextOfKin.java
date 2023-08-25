package chamapool.domain.member.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Entity
@Setter
@Getter
@Accessors(chain = true, fluent = true)
public class NextOfKin {
    @Id
    @GeneratedValue
    private Integer id;

    private String firstName;

    private String lastName;

    @Column(name = "national_id", unique = true)
    private String nationalId;

    @Column(name = "mobile_number", unique = true)
    private String mobileNumber;

    @OneToOne
    @JoinColumn(name = "member_id")
    private Member member;
}

package chamapool.domain.member.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Entity
@Setter
@Getter
@Accessors(chain = true, fluent = true)
public class Address {
  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE)
  private Integer id;

  private String county;
  private String subCounty;
  private String constituency;
  private String locationDescription;

  @OneToOne
  @JoinColumn(name = "member_id")
  private Member member;
}

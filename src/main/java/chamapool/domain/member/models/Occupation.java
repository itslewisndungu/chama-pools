package chamapool.domain.member.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Entity
@Setter
@Getter
@Accessors(chain = true, fluent = true)
public class Occupation {
  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE)
  private Integer id;

  private String organization;
  private String position;
  private Double salary;

  @OneToOne
  @JoinColumn(name = "member_id")
  private Member member;
}

package chamapool.domain.member.models;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Entity
@Setter
@Getter
@Accessors(chain = true, fluent = true)
public class Role {
  @Id @GeneratedValue private Integer id;

  private String name;

  @Override
  public String toString() {
    return this.name;
  }
}

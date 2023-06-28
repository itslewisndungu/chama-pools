package chamapool.domain.meeting.models;

import chamapool.domain.member.models.Member;
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
@Accessors(chain = true, fluent = true)
@EntityListeners(AuditingEntityListener.class)
public class MeetingContribution {
    @EmbeddedId
    private MeetingContributionId id = new MeetingContributionId();

    @MapsId("meetingId")
    @ManyToOne
    @JoinColumn(name = "meeting_id")
    private Meeting meeting;

    @MapsId("memberId")
    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;

    private Double amount;

    @CreatedDate
    private LocalDate contributionDate;
}

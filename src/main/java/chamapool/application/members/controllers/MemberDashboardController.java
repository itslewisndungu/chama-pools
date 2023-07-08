package chamapool.application.members.controllers;

import chamapool.application.members.services.MemberDashboardService;
import chamapool.domain.member.models.Member;
import java.util.HashMap;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/member")
@RequiredArgsConstructor
public class MemberDashboardController {
    private final MemberDashboardService service;

    @GetMapping("/meetings-summary")
    public HashMap<String, Object> getMemberMeetingsSummary(Member member) {
        return this.service.getMemberMeetingsSummary(member);
    }

    @GetMapping("/loans-summary")
    public HashMap<String, Object> getMemberLoansSummary(Member member) {
        return this.service.getMemberLoansSummary(member);
    }
}

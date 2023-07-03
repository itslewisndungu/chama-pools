package chamapool.application.members.requests;

public record NewMemberRequest(
        String firstName,
        String lastName,
        String nationalId,
        String phoneNumber
) {
}

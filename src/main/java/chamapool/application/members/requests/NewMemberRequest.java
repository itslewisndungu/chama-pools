package chamapool.application.members.requests;

public record NewMemberRequest(
        String firstName,
        String lastName,
        String nationalId,
        String phoneNumber,
        String position,
        String organization,
        Double salary,
        String nextOfKinNationalId,
        String nextOfKinFirstName,
        String nextOfKinLastName,
        String nextOfKinMobileNumber,
        String county,
        String subCounty,
        String constituency
) {
}

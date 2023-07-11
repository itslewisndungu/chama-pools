package chamapool.application.auth.requests;

public record ChangePasswordRequest(String token, String password) {}

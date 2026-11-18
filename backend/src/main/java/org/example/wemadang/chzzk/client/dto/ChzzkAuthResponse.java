package org.example.wemadang.chzzk.client.dto;

public record ChzzkAuthResponse<T>(int code, String message, T content) {

    public record TokenRequest(String accessToken, String refreshToken,
                               String tokenType, String expiresIn) {}

    public record UsersMe(String channelId, String channelName){}
}



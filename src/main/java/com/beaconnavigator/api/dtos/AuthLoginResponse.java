package com.beaconnavigator.api.dtos;

public class AuthLoginResponse {
    private String token;

    public AuthLoginResponse() {}

    public AuthLoginResponse(String token) {
        this.token = token;
    }

    public String getToken() { return token; }
    public void setToken(String token) { this.token = token; }
}

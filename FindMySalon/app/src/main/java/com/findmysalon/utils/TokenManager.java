package com.findmysalon.utils;

public interface TokenManager {
    String getToken();
    boolean hasToken();
    void clearToken();
    String refreshToken();
}

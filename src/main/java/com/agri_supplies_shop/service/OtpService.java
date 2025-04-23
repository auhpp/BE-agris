package com.agri_supplies_shop.service;

import java.util.concurrent.ExecutionException;

public interface OtpService {
    Integer generateOTP(String key);

    Integer getOPTByKey(String key) throws ExecutionException;

    void clearOTPFromCache(String key);
}

package com.agri_supplies_shop.service.impl;

import com.agri_supplies_shop.service.OtpService;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import org.springframework.stereotype.Service;

import java.util.Random;
import java.util.concurrent.TimeUnit;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class OtpServiceImpl implements OtpService {
    Integer EXPIRE_MIN = 43200;
    @NonFinal
    LoadingCache<String, Integer> otpCache;

    public OtpServiceImpl() {
        super();
        otpCache = CacheBuilder.newBuilder()
                .expireAfterWrite(EXPIRE_MIN, TimeUnit.MINUTES)
                .build(new CacheLoader<String, Integer>() {
                    @Override
                    public Integer load(String key) throws Exception {
                        return 0;
                    }
                });
    }

    @Override
    public Integer generateOTP(String key) {
        Random random = new Random();
        int otp = 100000 + random.nextInt(900000);
        otpCache.put(key, otp);
        return otp;
    }

    @Override
    public Integer getOPTByKey(String key) {
        return otpCache.getIfPresent(key);
    }

    @Override
    public void clearOTPFromCache(String key) {
        otpCache.invalidate(key);
    }
}

package com.agri_supplies_shop.service.impl;

import com.agri_supplies_shop.dto.request.AuthenticationRequest;
import com.agri_supplies_shop.dto.request.IntrospectRequest;
import com.agri_supplies_shop.dto.request.LogoutRequest;
import com.agri_supplies_shop.dto.request.RefreshRequest;
import com.agri_supplies_shop.dto.response.AuthenticationResponse;
import com.agri_supplies_shop.dto.response.IntrospectResponse;
import com.agri_supplies_shop.entity.Account;
import com.agri_supplies_shop.entity.InvalidatedToken;
import com.agri_supplies_shop.exception.AppException;
import com.agri_supplies_shop.exception.ErrorCode;
import com.agri_supplies_shop.repository.AccountRepository;
import com.agri_supplies_shop.repository.CustomerRepository;
import com.agri_supplies_shop.repository.InvalidatedTokenRepository;
import com.agri_supplies_shop.service.AuthenticationService;
import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuthenticationServiceImpl implements AuthenticationService {
    InvalidatedTokenRepository invalidatedTokenRepository;

    PasswordEncoder passwordEncoder;

    AccountRepository accountRepository;

    @NonFinal
    @Value("${jwt.signer-key}")
    private String SIGNER_KEY;

    @NonFinal
    @Value("${jwt.valid-duration}")
    private long VALID_DURATION;

    @NonFinal
    @Value("${jwt.refreshable-duration}")
    private long REFRESHABLE_DURATION;

    @Override
    public AuthenticationResponse authenticate(AuthenticationRequest request) throws JOSEException {
        Account account = accountRepository.findByUserName(request.getUserName()).orElseThrow(
                () -> new AppException(ErrorCode.USER_NOT_EXISTED)
        );
        AuthenticationResponse response = new AuthenticationResponse();
        boolean valid = passwordEncoder.matches(request.getPassword(), account.getPassword());
        if (valid) {
            String token = generateToken(account);
            response.setToken(token);
        } else {
            throw new AppException(ErrorCode.WRONG_PASSWORD);
        }
        response.setAuthenticated(true);
        return response;
    }

    @Override
    public IntrospectResponse introspect(IntrospectRequest request) throws ParseException, JOSEException {
        String token = request.getToken();
        boolean isValid = true;
        try {
            verifyToken(token, false);
        } catch (AppException e) {
            isValid = false;
        }
        return IntrospectResponse.builder()
                .valid(isValid)
                .build();
    }

    @Override
    public void logout(LogoutRequest request) throws ParseException, JOSEException {
        SignedJWT signedJWT = verifyToken(request.getToken(), true);
        String tokenId = signedJWT.getJWTClaimsSet().getJWTID();
        Date expiryTime = signedJWT.getJWTClaimsSet().getExpirationTime();
        InvalidatedToken invalidatedToken = InvalidatedToken.builder()
                .id(tokenId)
                .expiryTime(expiryTime)
                .build();
        invalidatedTokenRepository.save(invalidatedToken);
    }

    @Override
    public AuthenticationResponse refreshToken(RefreshRequest request) throws ParseException, JOSEException {
        SignedJWT signedJWT = verifyToken(request.getToken(), true);

        String tokenId = signedJWT.getJWTClaimsSet().getJWTID();
        Date expiryTime = signedJWT.getJWTClaimsSet().getExpirationTime();

        InvalidatedToken invalidatedToken = InvalidatedToken.builder()
                .id(tokenId)
                .expiryTime(expiryTime)
                .build();
        invalidatedTokenRepository.save(invalidatedToken);

        String userName = signedJWT.getJWTClaimsSet().getSubject();
        Account account = accountRepository.findByUserName(userName).orElseThrow(
                () -> new AppException(ErrorCode.USER_NOT_EXISTED)
        );
        String token = generateToken(account);
        return AuthenticationResponse.builder()
                .authenticated(true)
                .token(token)
                .build();
    }

    private SignedJWT verifyToken(String token, boolean isRefresh) throws ParseException, JOSEException {
        //create JWSVerifier pass SIGNER_KEY
        JWSVerifier verifier = new MACVerifier(SIGNER_KEY.getBytes());
        //Get SIGNER_KEY from token request
        SignedJWT signedJWT = SignedJWT.parse(token);
        //Get expiry time
        Date expiryTime = (isRefresh) ?
                new Date(signedJWT.getJWTClaimsSet().getIssueTime()
                        .toInstant().plus(REFRESHABLE_DURATION, ChronoUnit.SECONDS).toEpochMilli())
                : signedJWT.getJWTClaimsSet().getExpirationTime();
        //verify
        boolean verified = signedJWT.verify(verifier);

        if (!(verified && expiryTime.after(new Date()))) {
            throw new AppException(ErrorCode.UNAUTHENTICATED);
        }
        if (invalidatedTokenRepository.existsById(signedJWT.getJWTClaimsSet().getJWTID())) {
            throw new AppException(ErrorCode.UNAUTHENTICATED);
        }

        return signedJWT;
    }

    private String generateToken(Account account) throws JOSEException {
        //Header
        JWSHeader header = new JWSHeader(JWSAlgorithm.HS512);
        //Create claims
        JWTClaimsSet claimsSet = new JWTClaimsSet.Builder()
                .subject(account.getUserName())
                .issuer("agris.com")
                .issueTime(new Date())
                .expirationTime(new Date(
                        Instant.now().plus(VALID_DURATION, ChronoUnit.SECONDS).toEpochMilli()
                ))
                .jwtID(UUID.randomUUID().toString()) //Use for refresh token task
                .claim("scope", account.getRole().getName()) //Use for phan quyen
                .build();
        //Payload
        Payload payload = new Payload(claimsSet.toJSONObject());

        //create jwsObject
        JWSObject jwsObject = new JWSObject(header, payload);

        //sign token
        jwsObject.sign(new MACSigner(SIGNER_KEY.getBytes()));
        return jwsObject.serialize();
    }
}

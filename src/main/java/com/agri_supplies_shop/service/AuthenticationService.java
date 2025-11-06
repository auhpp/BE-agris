package com.agri_supplies_shop.service;

import com.agri_supplies_shop.dto.request.AuthenticationRequest;
import com.agri_supplies_shop.dto.request.IntrospectRequest;
import com.agri_supplies_shop.dto.request.LogoutRequest;
import com.agri_supplies_shop.dto.request.RefreshRequest;
import com.agri_supplies_shop.dto.response.AuthenticationResponse;
import com.agri_supplies_shop.dto.response.IntrospectResponse;
import com.agri_supplies_shop.entity.Account;
import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.KeyLengthException;

import java.text.ParseException;

public interface AuthenticationService {

    AuthenticationResponse authenticate(AuthenticationRequest request) throws JOSEException;

    IntrospectResponse introspect(IntrospectRequest request) throws ParseException, JOSEException;

    void logout(LogoutRequest request) throws ParseException, JOSEException;

    AuthenticationResponse refreshToken(RefreshRequest request) throws ParseException, JOSEException;

    String generateToken(Account account) throws JOSEException;
}

package com.coffeewa.coffeewebapp.service;





import org.springframework.stereotype.Service;

import com.coffeewa.coffeewebapp.accounts.accountservice;
import com.warrenstrange.googleauth.GoogleAuthenticator;
import com.warrenstrange.googleauth.GoogleAuthenticatorKey;
import com.warrenstrange.googleauth.GoogleAuthenticatorQRGenerator;

@Service
public class TotpService {


    private final GoogleAuthenticator googleAuthenticator;


    public TotpService(GoogleAuthenticator googleAuthenticator){
        this.googleAuthenticator = new GoogleAuthenticator();
    }

    public String generateSecretKey(){
        GoogleAuthenticatorKey key = googleAuthenticator.createCredentials();
        return key.getKey();
    }

    public String getQRCodeUrl(String username, String secret, String appName) {
        return GoogleAuthenticatorQRGenerator.getOtpAuthURL(appName,username, new GoogleAuthenticatorKey.Builder(secret).build() );


    }

    public boolean verifyCode(String secret, int code){
        return googleAuthenticator.authorize(secret, code);
    }




}

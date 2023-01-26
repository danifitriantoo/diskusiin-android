package com.danifitrianto.diskussin.setups.services;

import android.util.Base64;

import java.io.UnsupportedEncodingException;

public class DecodeToken {
    public static void decoded(String JWTEncoded) throws Exception {
        try {
            String[] split = JWTEncoded.split("\\.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static String getJson(String strEncoded) throws UnsupportedEncodingException{
        byte[] decodedJwt = Base64.decode(strEncoded, Base64.URL_SAFE);
        return new String(decodedJwt, "UTF-8");
    }
}

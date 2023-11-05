package de.marshal.bankapp.util;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Encoders;

public class GenerateJwtSecretKey {
    public static void main(String[] args) {
        System.out.println(generateKey());
    }

    private static String generateKey() {
        return Encoders.BASE64.encode(Jwts.SIG.HS512.key().build().getEncoded());
    }
}

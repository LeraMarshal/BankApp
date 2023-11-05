package de.marshal.bankapp.util;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.Scanner;

public class GenerateJwtAccessToken {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        String secretKeyBase64 = scanner.nextLine();
        SecretKey secretKey = Keys.hmacShaKeyFor(Decoders.BASE64.decode(secretKeyBase64));

        System.out.println(Jwts.builder()
                .subject("Bank subsystem")
                .expiration(new Date(Long.MAX_VALUE))
                .signWith(secretKey)
                .compact());
    }
}

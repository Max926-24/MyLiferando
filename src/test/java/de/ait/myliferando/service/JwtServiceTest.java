package de.ait.myliferando.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;


public class JwtServiceTest {

    private JwtService jwtService;

    @BeforeEach
    void setUp() {
        jwtService = new JwtService("1234-ahfhb-656453-ythrge-jfgff-234-324", 60);
    }

    @Test
    void generateToken_thenExtractUsername_returnsSameUsername() {
        String token = jwtService.generateToken("dispatcher2", "DISPATCHER");

        assertThat(jwtService.extractUsername(token)).isEqualTo("dispatcher2");
    }

    @Test
    void generateToken_thenExtractRole_returnsSameRole() {
        String token = jwtService.generateToken("dispatcher2", "DISPATCHER");

        assertThat(jwtService.extractRole(token)).isEqualTo("DISPATCHER");
    }

    @Test
    void isValid_whenTokenIsGood_returnsTrue() {
        String token = jwtService.generateToken("dispatcher2", "DISPATCHER");
        assertThat(jwtService.isValid(token)).isTrue();
    }

    @Test
    void isValid_whenTokenIsBroken_returnsFalse() {
        assertThat(jwtService.isValid("This is a not token")).isFalse();
    }

    @Test
    void isValid_whenTokenIsExpired_returnsFalse() {
        JwtService expiredService = new JwtService(
                "1234-ahfhb-656453-ythrge-jfgff-234-324", -1);
        String token = expiredService.generateToken("dispatcher2", "DISPATCHER");
        assertThat(expiredService.isValid(token)).isFalse();
    }

    @Test
    void isValid_whenTokenSignedWithOtherKey_returnsFalse() {
        JwtService otherService = new JwtService(
                "1234-ahfhb-65983-ythrge-jfgff-234-324", 60);
        String token = otherService.generateToken("dispatcher2", "DISPATCHER");
        assertThat(jwtService.isValid(token)).isFalse();
    }
}
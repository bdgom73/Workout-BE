package com.exerciseApp.exercise.Service;

import org.apache.tomcat.util.codec.binary.Base64;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;

import javax.annotation.PostConstruct;
import javax.transaction.Transactional;

import java.nio.charset.StandardCharsets;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
@Rollback(false)
class JWTServiceTest {

    @Autowired JWTService jwtService;
    @Autowired CreateIDService createIDService;

    @Test
    public void test() {
        UUID uuid = UUID.randomUUID();
        System.out.println("jwtService = " + UUID.randomUUID());
    }
}
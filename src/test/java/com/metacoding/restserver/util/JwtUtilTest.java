package com.metacoding.restserver.util;

import com.metacoding.restserver._core.util.JwtUtil;
import com.metacoding.restserver.user.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

// JwtUtil, application-dev.properties 2개만 메모리에 올리고 써도 되지만 모르면 통합테스트로
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
public class JwtUtilTest {

    @Autowired
    private JwtUtil jwtUtil;

    @Test
    public void create_Test(){
        // given
        User user = User.builder()
                .id(1)
                .username("ssar")
                .build();

        // when
        String jwt = jwtUtil.create(user);

        // eye
        System.out.println("mma: " + jwt);
    }
}
package com.metacoding.restserver.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@AutoConfigureMockMvc // MockMvc를 IoC 컨테이너에 등록해준다.
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK) // 디스패쳐 서블릿 + 컨트롤러 + 서비스 + 리포지토리 + persist context
public class UserControllerTest { // mock -> 포트 8080과 충돌날 수 있어서 가상으로

    @Autowired
    private MockMvc mvc; // @Autowired 써보면 빨간줄 -> IoC 컨테이너에 없음

    private ObjectMapper om = new ObjectMapper();

    @Test
    public void join_test() throws Exception {
        // given -> join할 때 필요한 body 데이터, when의 content에 넣을 데이터
        UserRequest.JoinDTO dto = new UserRequest.JoinDTO();
        dto.setUsername("haha");
        dto.setPassword("1234");
        dto.setEmail("haha@nate.com");
        // 스프링이 제공하는 jackson.databind -> ObjectMapper를 제공 (없으면 라이브러리 추가해야지)
        String reqBody = om.writeValueAsString(dto);

        // when
        // java는 await, asnyc가 없다. -> 메서드 자체가 데이터 받을 때 까지 대기하도록 되어 있다.
        ResultActions actions = mvc.perform(post("/join").content(reqBody).contentType(MediaType.APPLICATION_JSON));
//        ResultActions actions = mvc.perform(post("/join").content("하하").contentType(MediaType.APPLICATION_JSON));
        // 일부러 틀린 문자열넣을 경우 GeneralExceptionHandler에서 처리되서 무슨 예외인지 모름 -> 주석 처리해보고 할 것

        // eye -> 응답되는 body 데이터 확인
        String resBody = actions.andReturn().getResponse().getContentAsString();
        System.out.println("mma: " + resBody); // mma는 아무거나 적으면 된다. 컨트롤 f로 확인하기 편하게 쓰는 컨벤션

        // then -> 예외가 터저도 테스트는 통과로 뜸. 여기서 예외 터질 시 실패하도록 검증
        // 배열찾기 팁 : $.data[0].id -> data의 0번째 id
        actions.andExpect(jsonPath("$.success").value(true));
        actions.andExpect(jsonPath("$.message").value("성공"));
        actions.andExpect(jsonPath("$.data.id").value(4));
        actions.andExpect(jsonPath("$.data.username").value("haha"));
        actions.andExpect(jsonPath("$.data.email").value("haha@nate.com"));
        // 날짜는 매칭 힘드니 null 아닌 정도만 확인
        actions.andExpect(jsonPath("$.data.createdAt").isNotEmpty());
        // 더미를 바꾸지 않는 이상 테스트가 터질 일이 없다.
    }

    @Test
    public void join_fail_test() throws Exception {
        // 보통 성공과 실패 테스트 코드는 같이 짠다.
        // -> 예외가 터저도 테스트는 통과로 뜸. 여기서 예외 터질 시 실패하도록 검증

        UserRequest.JoinDTO dto = new UserRequest.JoinDTO();
        dto.setUsername("ssar");
        dto.setPassword("1234");
        dto.setEmail("ssar@nate.com");
        String reqBody = om.writeValueAsString(dto);

        // when
        ResultActions actions = mvc.perform(post("/join").content(reqBody).contentType(MediaType.APPLICATION_JSON));

        // eye -> 응답되는 body 데이터 확인
        String resBody = actions.andReturn().getResponse().getContentAsString();
        System.out.println("mma: " + resBody); // mma는 아무거나 적으면 된다. 컨트롤 f로 확인하기 편하게 쓰는 컨벤션

        // then -> 예외 터질 시 실패하도록 검증
        actions.andExpect(jsonPath("$.success").value(false));
        actions.andExpect(jsonPath("$.message").value("유저네임 중복"));
        actions.andExpect(jsonPath("$.data").isEmpty());
    }

    @Test
    public void login_test() throws Exception {
        // given -> join할 때 필요한 body 데이터, when의 content에 넣을 데이터
        UserRequest.LoginDTO dto = new UserRequest.LoginDTO();
        dto.setUsername("ssar");
        dto.setPassword("1234");

        String reqBody = om.writeValueAsString(dto);

        // when
        ResultActions actions = mvc.perform(post("/login").content(reqBody).contentType(MediaType.APPLICATION_JSON));

        // eye
        String resBody = actions.andReturn().getResponse().getContentAsString();
        // 다 실행하지 말고 mma로 뭐가 나오는 지 눈(eye)로 보고 then을 주석 해제
        System.out.println("mma: " + resBody);

        String jwt = actions.andReturn().getResponse().getHeader("Authorization");
        System.out.println("mma: " + jwt);
        // 토큰에 문제 있고 없고는 JwtUtilTest에서 하면 된다.

        // then
        actions.andExpect(header().string("Authorization", Matchers.startsWith("Bearer")));
        actions.andExpect(jsonPath("$.success").value(true));
        actions.andExpect(jsonPath("$.message").value("성공"));
        actions.andExpect(jsonPath("$.data.id").value(1));
        actions.andExpect(jsonPath("$.data.username").value("ssar"));
    }
}

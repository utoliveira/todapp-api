package com.atlas.todappapi.unit.controller;

import com.atlas.todappapi.bean.config.SecurityConfig;
import com.atlas.todappapi.bean.controller.RegisterController;
import com.atlas.todappapi.bean.service.RegisterService;
import com.atlas.todappapi.consts.TodappEndpoint;
import com.atlas.todappapi.exception.EmailAlreadyRegisteredException;
import com.atlas.todappapi.model.request.RegisterRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static com.atlas.todappapi.consts.code.OperationCode.EMAIL_ALREADY_USED;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(controllers = {RegisterController.class, SecurityConfig.class})
public class RegisterControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private RegisterService registerService;

    private ObjectMapper objectMapper;

    @Before
    public void init(){
        objectMapper = new ObjectMapper();
    }

    @Test
    public void register_shouldRegister() throws Exception{
        doNothing().when(registerService).register(BDDMockito.any(RegisterRequest.class));

        RegisterRequest request = RegisterRequest.builder()
                .password("123").name("Higor").email("higor@email.com").build();

        mockMvc.perform(post(TodappEndpoint.REGISTER + "/")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());
    }

    @Test
    public void register_invalidEmail_null() throws Exception{
        RegisterRequest request = RegisterRequest.builder()
                .password("123").name("Higor").email(null).build();

        invalidRegisterRequestTest(request);
    }

    @Test
    public void register_invalidEmail_empty() throws Exception{
        RegisterRequest request = RegisterRequest.builder()
                .password("123").name("Higor").email(null).build();

        invalidRegisterRequestTest(request);
    }


    @Test
    public void register_invalidEmail_blank() throws Exception{
        RegisterRequest request = RegisterRequest.builder()
                .password("123").name("Higor").email(null).build();

        invalidRegisterRequestTest(request);
    }

    @Test
    public void register_invalidEmail_blowLengthLimit() throws Exception{
        RegisterRequest request = RegisterRequest.builder()
                .password("123").name("Higor")
                .email(getStringEmailLongerThan100()).build();

        invalidRegisterRequestTest(request);
    }

    @Test
    public void register_invalidEmail_outOfFormat_case1() throws Exception{
        RegisterRequest request = RegisterRequest.builder()
                .password("123").name("Higor").email("@gmail.com").build();

        invalidRegisterRequestTest(request);
    }

    @Test
    public void register_invalidEmail_outOfFormat_case2() throws Exception{
        RegisterRequest request = RegisterRequest.builder()
                .password("123").name("Higor").email("gmail.com").build();

        invalidRegisterRequestTest(request);
    }

    @Test
    public void register_invalidEmail_outOfFormat_case3() throws Exception{
        RegisterRequest request = RegisterRequest.builder()
                .password("123").name("Higor").email("higor@gmail").build();

        invalidRegisterRequestTest(request);
    }

    @Test
    public void register_invalidEmail_outOfFormat_case4() throws Exception{
        RegisterRequest request = RegisterRequest.builder()
                .password("123").name("Higor").email("higor@.com").build();

        invalidRegisterRequestTest(request);
    }

    @Test
    public void register_invalidEmail_outOfFormat_case5() throws Exception{
        RegisterRequest request = RegisterRequest.builder()
                .password("123").name("Higor").email("higor@teste.").build();

        invalidRegisterRequestTest(request);
    }

    @Test
    public void register_invalidEmail_outOfFormat_case6() throws Exception{
        RegisterRequest request = RegisterRequest.builder()
                .password("123").name("Higor").email("higor@teste").build();

        invalidRegisterRequestTest(request);
    }


    @Test
    public void register_invalidPassword_null() throws Exception{
        RegisterRequest request = RegisterRequest.builder()
                .password(null).name("Higor").email("higor@email.com").build();

    }

    @Test
    public void register_invalidPassword_empty() throws Exception{
        RegisterRequest request = RegisterRequest.builder()
                .password("").name("Higor").email("higor@email.com").build();

        invalidRegisterRequestTest(request);
    }

    @Test
    public void register_invalidPassword_blank() throws Exception{
        RegisterRequest request = RegisterRequest.builder()
                .password("").name("Higor").email("higor@email.com").build();

        invalidRegisterRequestTest(request);
    }

    @Test
    public void register_invalidPassword_blowLengthLimit() throws Exception{
        RegisterRequest request = RegisterRequest.builder()
                .password(getStringLongerThan100())
                .name("Higor").email("higor@email.com").build();

        invalidRegisterRequestTest(request);
    }

    @Test
    public void register_invalidName_null() throws Exception{
        RegisterRequest request = RegisterRequest.builder()
                .password("1234")
                .name(null).email("higor@email.com").build();

        invalidRegisterRequestTest(request);
    }

    @Test
    public void register_invalidName_empty() throws Exception{
        RegisterRequest request = RegisterRequest.builder()
                .password("1234")
                .name("").email("higor@email.com").build();

        invalidRegisterRequestTest(request);
    }

    @Test
    public void register_invalidName_blank() throws Exception{
        RegisterRequest request = RegisterRequest.builder()
                .password("1234")
                .name("  ").email("higor@email.com").build();

        invalidRegisterRequestTest(request);
    }

    @Test
    public void register_invalidName_blowLengthLimit() throws Exception{
        RegisterRequest request = RegisterRequest.builder()
                .password("1234")
                .name(getStringLongerThan100()).email("higor@email.com").build();

        invalidRegisterRequestTest(request);
    }

    @Test
    public void register_emailAlreadyUsed() throws Exception{
        doThrow(EmailAlreadyRegisteredException.class)
                .when(registerService)
                .register(BDDMockito.any(RegisterRequest.class));

        RegisterRequest request = RegisterRequest.builder()
                .password("1234")
                .name("Higor Oliveira").email("higor@email.com").build();

        mockMvc.perform(post(TodappEndpoint.REGISTER + "/")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isPreconditionFailed())
                .andExpect(jsonPath("code").value(EMAIL_ALREADY_USED.getCode()));
    }

    private void invalidRegisterRequestTest(RegisterRequest request) throws Exception{
        mockMvc.perform(post(TodappEndpoint.REGISTER+"/")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }
    private String getStringLongerThan100(){
        return "-----------------------------------------------------------------------------------------------------";
    }

    private String getStringEmailLongerThan100() {
        return "-------------------------------------------------------------------------------------------@gmail.com";
    }

}

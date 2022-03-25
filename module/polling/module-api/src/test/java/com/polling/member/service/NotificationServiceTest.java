package com.polling.member.service;

import com.polling.member.dto.request.VerifySMSCodeRequestDto;
import com.polling.notification.SendSMSRequestDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import static org.assertj.core.api.Assertions.assertThat;


@SpringBootTest
public class NotificationServiceTest {
    @Autowired
    private NotificationService smsService;
    @Autowired
    private NotificationRedisService notificationRedisService;

    private static String TO = "01096121458";

    @Test
    public void 휴대폰인증문자보내기_restTemplate() throws Exception{
        //given
        SendSMSRequestDto requestDto = new SendSMSRequestDto("01065752938", "함수에서 보낸거");
        //when

        smsService.sendSms(requestDto);
        //then
    }

//    @Test
//    public void 휴대폰인증문자보내기_webClient() throws Exception{
//        //given
//        SendSMSRequestDto requestDto = new SendSMSRequestDto("01065752938", "테스트");
//        //when
//        smsService.sendSms_webClient(requestDto);
//        //then
//    }

    @Test
    void 인증번호보냈다가인증_Success() throws Exception{
        //given
        SendSMSRequestDto requestDto = new SendSMSRequestDto(TO, "테스트");
        //when
        smsService.sendSms(requestDto);
        String code = notificationRedisService.getSmsCertification(requestDto.getTo());
        VerifySMSCodeRequestDto dto = new VerifySMSCodeRequestDto(TO, code);
        //then
        boolean isSuccess = smsService.isVerified(dto);
        assertThat(isSuccess).isTrue();
    }

    @Test
    void 인증번호보냈다가인증_Failed() throws Exception{
        //given
        SendSMSRequestDto requestDto = new SendSMSRequestDto(TO, "테스트");
        //when
        smsService.sendSms(requestDto);
        String code = notificationRedisService.getSmsCertification(requestDto.getTo());
        VerifySMSCodeRequestDto dto = new VerifySMSCodeRequestDto(TO, "notacode");
        //then
        boolean isSuccess = smsService.isVerified(dto);
        assertThat(isSuccess).isFalse();
    }


}

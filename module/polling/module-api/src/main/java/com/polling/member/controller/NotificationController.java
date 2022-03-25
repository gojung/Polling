package com.polling.member.controller;

import com.polling.member.dto.request.VerifySMSCodeRequestDto;
import com.polling.member.dto.response.SMSCodeResponseDto;
import com.polling.notification.SendSMSRequestDto;
import com.polling.member.service.NotificationService;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/notify")
public class NotificationController {
    private final NotificationService notificationService;

    @PostMapping("/sms")
    @ApiOperation(value = "휴대폰 인증")
    public ResponseEntity<SMSCodeResponseDto> save(@RequestBody SendSMSRequestDto requestDto) {
        notificationService.sendSms(requestDto);
        return ResponseEntity.status(200).build();
    }

    @PostMapping("/sms/confirms")
    public ResponseEntity<Void> SmsVerification(@RequestBody VerifySMSCodeRequestDto requestDto) {
        notificationService.verify(requestDto);
        return ResponseEntity.status(200).build();
    }
}

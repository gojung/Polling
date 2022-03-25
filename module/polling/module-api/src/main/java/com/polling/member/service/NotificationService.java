package com.polling.member.service;

import com.google.gson.Gson;
import com.polling.exception.CustomErrorResult;
import com.polling.exception.CustomException;
import com.polling.member.dto.request.VerifySMSCodeRequestDto;
import com.polling.notification.SendSMSApiRequestDto;
import com.polling.notification.SendSMSRequestDto;
import com.polling.utils.RandomUtils;
import lombok.RequiredArgsConstructor;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class NotificationService {
//    private final NotificationClient notificationClient;
    private final Gson gson;
    private final NotificationRedisService notificationRedisService;

    @Value("${sms.serviceid}")
    private String serviceId;
    @Value("${sms.accesskey}")
    private String accessKey;
    @Value("${sms.secretkey}")
    private String secretKey;

    private final String FROM = "01065752938";

    /*WebClient*/
//    public void sendSms_webClient(SendSMSRequestDto requestDto) {
//        notificationClient.sendSMS(requestDto);
//    }

    /*RestTemplate*/
    public void sendSms(SendSMSRequestDto requestDto) {
        try {
            String verifyCode = RandomUtils.generateAuthNo4();

            List<SendSMSRequestDto> messages = new ArrayList<>();
            messages.add(requestDto);

            Long time = System.currentTimeMillis();
            SendSMSApiRequestDto smsRequest = new SendSMSApiRequestDto("SMS", "COMM", "82", FROM, verifyCode, messages);
            String jsonBody = gson.toJson(smsRequest);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.set("x-ncp-apigw-timestamp", time.toString());
            headers.set("x-ncp-iam-access-key", this.accessKey);
            String sig = makeSignature(time);
            headers.set("x-ncp-apigw-signature-v2", sig);

            HttpEntity<String> body = new HttpEntity<>(jsonBody,headers);

            /* 실제 요청 날리는 부분 주석 처리 */
            System.out.println("send SMS!!!");
            RestTemplate restTemplate = new RestTemplate();
            restTemplate.setRequestFactory(new HttpComponentsClientHttpRequestFactory());
//            restTemplate.postForObject(new URI("https://sens.apigw.ntruss.com/sms/v2/services/"+this.serviceId+"/messages"), body, SendSMSApiRequestDto.class);

            //redis 저장
            notificationRedisService.createSmsCertification(requestDto.getTo(), verifyCode);
        }
//        catch (Exception e) { throw new RuntimeException(e.getCause());}
        catch (Exception e) { throw new CustomException(CustomErrorResult.FAIL_SEND_SMS);}
    }

    private HttpEntity<String> makeParam(SendSMSRequestDto requestDto, String verifyCode) throws UnsupportedEncodingException, NoSuchAlgorithmException, InvalidKeyException{
        List<SendSMSRequestDto> messages = new ArrayList<>();
        messages.add(requestDto);


        Long time = System.currentTimeMillis();
        SendSMSApiRequestDto smsRequest = new SendSMSApiRequestDto("SMS", "COMM", "82", FROM, verifyCode, messages);
        String jsonBody = gson.toJson(smsRequest);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("x-ncp-apigw-timestamp", time.toString());
        headers.set("x-ncp-iam-access-key", this.accessKey);
        String sig = makeSignature(time);
        headers.set("x-ncp-apigw-signature-v2", sig);

        HttpEntity<String> body = new HttpEntity<>(jsonBody,headers);
        return body;
    }

    private String makeSignature(Long time) throws UnsupportedEncodingException, NoSuchAlgorithmException, InvalidKeyException {
        String space = " ";
        String newLine = "\n";
        String method = "POST";
        String url = "/sms/v2/services/"+ this.serviceId+"/messages";
        String timestamp = time.toString();
        String accessKey = this.accessKey;
        String secretKey = this.secretKey;

        String message = method +
                space +
                url +
                newLine +
                timestamp +
                newLine +
                accessKey;

        SecretKeySpec signingKey = new SecretKeySpec(secretKey.getBytes(StandardCharsets.UTF_8), "HmacSHA256");
        Mac mac = Mac.getInstance("HmacSHA256");
        mac.init(signingKey);

        byte[] rawHmac = mac.doFinal(message.getBytes(StandardCharsets.UTF_8));

        return Base64.encodeBase64String(rawHmac);
    }

    public void verify(VerifySMSCodeRequestDto requestDto){
        if (!isVerified(requestDto)) {
            throw new CustomException(CustomErrorResult.WRONG_VERIFY_CODE);
        }
        notificationRedisService.removeSmsCertification(requestDto.getTo());
    }

    public boolean isVerified(VerifySMSCodeRequestDto requestDto){
        return (notificationRedisService.hasKey(requestDto.getTo()) &&
                notificationRedisService.getSmsCertification(requestDto.getTo())
                        .equals(requestDto.getCode()));
    }
}

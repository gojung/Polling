package com.polling.grpc.notification.service;

import com.polling.aop.annotation.Trace;
import com.polling.grpc.EventServiceGrpc;
import com.polling.grpc.GiftType;
import com.polling.grpc.ResultStatus;
import com.polling.grpc.WinningRequest;
import com.polling.grpc.WinningResponse;
import io.grpc.Status;
import io.grpc.stub.StreamObserver;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class EventService extends EventServiceGrpc.EventServiceImplBase {

  private final JavaMailSender mailSender;

  @Value("${spring.mail.username}")
  private String FROM_ADDRESS;

  @Trace
  @Override
  public void winning(WinningRequest request,
      StreamObserver<WinningResponse> responseObserver) {
    try {
      sendMail(request.getEmail(), request.getGiftType());
      responseObserver.onNext(
          WinningResponse.newBuilder()
              .setStatus(ResultStatus.newBuilder()
                  .setCode(Status.OK.getCode().value())
                  .setMessage("Winning SUCCESS!!!")
                  .build())
              .setResult("Success Event")
              .build()
      );
      responseObserver.onCompleted();

    } catch (Exception e) {
      responseObserver.onError(e);
    }
  }


  private void sendMail(String userEmail, GiftType giftType) {
    SimpleMailMessage message = new SimpleMailMessage();
    message.setTo(userEmail);
    message.setFrom(FROM_ADDRESS);
    message.setSubject("경품당첨");
    message.setText(giftType + "에 당첨되셨습니다");

    mailSender.send(message);
  }


}

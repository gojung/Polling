package com.polling.grpc.client;

import com.google.common.util.concurrent.ListenableFuture;
import com.polling.grpc.EventServiceGrpc;
import com.polling.grpc.MailRequest;
import com.polling.grpc.MailResponse;
import com.polling.grpc.client.dto.request.SendMailRequestDto;
import io.grpc.Deadline;
import java.util.concurrent.TimeUnit;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@Slf4j
@RestController
@RequestMapping("/api/notify/mail")
public class NotificationMailStub extends AbstractStub {

  private EventServiceGrpc.EventServiceFutureStub stub() {
    return EventServiceGrpc.newFutureStub(channel())
        .withDeadline(Deadline.after(3000, TimeUnit.MILLISECONDS));
  }

  @PostMapping
  public String winning(@RequestBody SendMailRequestDto requestDto) {

    EventServiceGrpc.EventServiceFutureStub stub = stub();

    MailRequest request = MailRequest.newBuilder()
        .setEmail(requestDto.getUserEmail())
        .setGiftType(requestDto.getGiftType())
        .build();

    ListenableFuture<MailResponse> responseFuture = stub.sendReward(request);

    try {
      MailResponse response = responseFuture.get(300000, TimeUnit.MILLISECONDS); //300 sec

    } catch (Exception e) {
      log.error(e.getMessage(), e);
    }

    return "OK";
  }
}

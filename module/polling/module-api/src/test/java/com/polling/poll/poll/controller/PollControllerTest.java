package com.polling.poll.poll.controller;

import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.polling.exception.CustomExceptionHandler;
import com.polling.poll.poll.dto.response.FindPollPageResponseDto;
import com.polling.poll.poll.entity.status.PollStatus;
import com.polling.poll.poll.repository.PollQueryRepository;
import com.polling.poll.poll.service.PollService;
import java.util.ArrayList;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@EnableWebSecurity
public class PollControllerTest {

  @InjectMocks
  private PollController target;

  @Mock
  private PollService pollService;

  @Mock
  private PollQueryRepository pollQueryRepository;

  private MockMvc mockMvc;

  @BeforeEach
  public void init() {
    mockMvc = MockMvcBuilders.standaloneSetup(target)
        .setControllerAdvice(new CustomExceptionHandler())
        .build();
  }

  @Test
  public void pollControllerIsNotNull() throws Exception {
    Assertions.assertThat(target).isNotNull();
  }

  @Test
  public void 투표조회_진행중() throws Exception {
    //given
    final String url = "/api/polls/{status}/{page}/{limit}";
    doReturn(new ArrayList<FindPollPageResponseDto>()).when(pollQueryRepository)
        .findPollPageByStatus(PollStatus.IN_PROGRESS, 0, 10);

    //when
    ResultActions resultActions = mockMvc.perform(get(url, "progress", 0, 10));

    //then
    resultActions.andExpect(status().isOk());
    verify(pollQueryRepository, times(1)).findPollPageByStatus(PollStatus.IN_PROGRESS, 0, 10);
  }

}

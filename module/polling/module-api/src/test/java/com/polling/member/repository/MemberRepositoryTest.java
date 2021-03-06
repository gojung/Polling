package com.polling.member.repository;

import static org.assertj.core.api.Assertions.assertThat;

import com.polling.config.jpa.JpaConfig;
import com.polling.member.entity.Member;
import com.polling.member.entity.status.MemberRole;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

@Import(JpaConfig.class)
@DataJpaTest
class MemberRepositoryTest {

  private final String email = "test@email.com";
  @Autowired
  private MemberRepository memberRepository;
  @Autowired
  private EntityManager em;

  @Test
  public void memberRepositoryIsNotNull() {
    assertThat(memberRepository).isNotNull();
  }

  @Test
  public void 멤버등록() throws Exception {
    //given
    final Member member = createMember();

    //when
    final Member result = memberRepository.save(member);

    //then
    assertThat(result.getEmail()).isEqualTo(email);
    assertThat(result.getMemberRole()).contains(MemberRole.ROLE_USER);
    assertThat(result.getNickname()).isEqualTo("testNickname");
    assertThat(result.getPassword()).isEqualTo("test");
    assertThat(result.getPhoneNumber()).isEqualTo("01012345678");
    assertThat(result.getId()).isNotNull();
    assertThat(result.getCreatedDate()).isNotNull();
    assertThat(result.getModifiedDate()).isNotNull();
  }

  @Test
  public void 멤버존재확인_이메일() throws Exception {
    //given
    final Member member = createMember();

    //when
    memberRepository.save(member);
    final Boolean result = memberRepository.existsByEmail(email);

    //then
    assertThat(result).isTrue();
  }

  @Test
  public void 멤버조회_이메일() throws Exception {
    //given
    final Member member = createMember();

    //when
    memberRepository.save(member);
    final Member findMember = memberRepository.findByEmail(email).orElseThrow();

    //then
    assertThat(findMember.getEmail()).isEqualTo(email);
    assertThat(findMember.getMemberRole()).contains(MemberRole.ROLE_USER);
    assertThat(findMember.getNickname()).isEqualTo("testNickname");
    assertThat(findMember.getPassword()).isEqualTo("test");
    assertThat(findMember.getPhoneNumber()).isEqualTo("01012345678");
    assertThat(findMember.getId()).isNotNull();
    assertThat(findMember.getCreatedDate()).isNotNull();
    assertThat(findMember.getModifiedDate()).isNotNull();
  }

  @Test
  public void 멤버삭제() throws Exception {
    //given
    final Member member = createMember();

    //when
    memberRepository.save(member);
    memberRepository.delete(member);
    final Boolean result = memberRepository.existsByEmail("test@test.com");

    //then
    assertThat(result).isFalse();
  }

  @Test
  public void 맴버닉네임변경() throws Exception {
    //given
    final Member member = createMember();

    //when
    Member savedMember = memberRepository.save(member);
    savedMember.changeNickname("changeNickname");
    em.flush();
    em.clear();
    Member findMember = memberRepository.findByEmail(email).orElseThrow();

    //then
    assertThat(findMember.getNickname()).isEqualTo("changeNickname");

  }

  private Member createMember() {
    return Member.builder()
        .nickname("testNickname")
        .password("test")
        .email(email)
        .phoneNumber("01012345678")
        .build();
  }
}
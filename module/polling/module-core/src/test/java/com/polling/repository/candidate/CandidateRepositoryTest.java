package com.polling.repository.candidate;


import com.polling.config.JpaConfig;
import com.polling.entity.candidate.Candidate;
import com.polling.entity.candidate.CandidateGallery;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@Import(JpaConfig.class)
@DataJpaTest
public class CandidateRepositoryTest {

    @Autowired
    private CandidateRepository candidateRepository;

    @Test
    public void candidateRepositoryIsNotNull() throws Exception{
        assertThat(candidateRepository).isNotNull();
    }
    
    @Test
    public void 후보자투표수증가() throws Exception{
        //given
        Candidate candidate = createCandidate("suzy");
        candidateRepository.save(candidate);

        //when
        candidate.addVoteTotal(1);
        Candidate findCandidate = candidateRepository.findById(candidate.getId()).orElseThrow();

        //then
        assertThat(findCandidate.getVoteTotalCount()).isEqualTo(1);
    }

    @Test
    public void 후보자삭제() throws Exception{
        //given
        Candidate candidate = createCandidate("suzy");
        candidateRepository.save(candidate);

        //when
        candidateRepository.delete(candidate);

        //then
        assertThat(candidateRepository.existsById(candidate.getId())).isFalse();

    }

    private Candidate createCandidate(String name){
        Candidate candidate = Candidate.builder()
                .name(name)
                .profile("profile")
                .poll(null)
                .thumbnail("thumbnail")
                .build();

        return candidate;
    }
}

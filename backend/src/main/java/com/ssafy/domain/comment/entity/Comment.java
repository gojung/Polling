package com.ssafy.domain.comment.entity;

import com.ssafy.domain.candidate.entity.Candidate;
import com.ssafy.domain.common.BaseTimeEntity;
import com.ssafy.domain.user.entity.User;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@NoArgsConstructor
@Table(name = "TB_COMMENT")
@Entity
public class Comment extends BaseTimeEntity {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "comment_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "candidate_id")
    private Candidate candidate;

    @Column
    private String content;

    @Builder
    public Comment(User user, Candidate candidate, String content){
        this.user = user;
        this.candidate = candidate;
        this.content = content;
    }

}

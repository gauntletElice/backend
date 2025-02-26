package com.example.elice.member.domain;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Setter
@Getter
@Builder
@Table(name = "users")
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String email;

    @Enumerated(EnumType.STRING)
    private Role role;

    @Column
    private String nickname;

    @Column
    private String profile;

    public static Member createFirstLoginMember(final String email, String nickname, String profile) {
        return Member.builder()
                .email(email)
                .nickname(nickname)
                .profile(profile)
                .role(Role.USER)
                .build();
    }

}

package com.example.elice.member.domain;

import com.example.elice.common.global.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Setter
@Getter
@Builder
@Table(name = "users")
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String email;

    @Enumerated(EnumType.STRING)
    private Role role;

    public static Member createFirstLoginMember(final String email) {
        return Member.builder()
                .email(email)
                .role(Role.USER)
                .build();
    }

}

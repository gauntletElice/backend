package com.example.elice.member;

import com.example.elice.member.domain.Member;
import com.example.elice.member.domain.Role;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class MemberTest {
    private Member member;

    @BeforeEach
    void init() {
        member = Member.createFirstLoginMember("test@example.com", "nickname", "profile");
    }

    @DisplayName("최초 로그인 기본 정보 세팅")
    @Test
    void testCreateFirstLoginMember() {
        assertNotNull(member);
        assertEquals("test@example.com", member.getEmail());
        assertEquals(Role.USER, member.getRole());
    }

}

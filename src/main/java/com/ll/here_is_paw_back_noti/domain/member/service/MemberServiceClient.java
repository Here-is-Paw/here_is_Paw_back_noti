package com.ll.here_is_paw_back_noti.domain.member.service;

import com.ll.here_is_paw_back_noti.domain.member.entity.Member;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Slf4j
@Service
@RequiredArgsConstructor
public class MemberServiceClient {

  private final RestTemplate restTemplate;

  @Value("${services.member.url}")
  private String memberServiceUrl;

  public Member getMemberById(Long id) {
    try {
      String url = UriComponentsBuilder.fromHttpUrl(memberServiceUrl)
          .path("/api/v1/members/{id}")
          .buildAndExpand(id)
          .toUriString();

      return restTemplate.getForObject(url, Member.class);
    } catch (Exception e) {
      log.error("Error fetching member with id {}: {}", id, e.getMessage());
      // 기본 Member 객체 반환 (ID만 포함)
      Member member = new Member();
      member.setId(id);
      return member;
    }
  }
}
package com.ll.here_is_paw_back_noti.domain.member.service;

import com.ll.here_is_paw_back_noti.domain.member.dto.MemberDto;
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

  @Value("${services.auth.url}")
  private String memberServiceUrl;

  public MemberDto getMemberById(Long id) {
    try {
      String url = UriComponentsBuilder.fromHttpUrl(memberServiceUrl)
          .path("/api/v1/members/{id}")
          .buildAndExpand(id)
          .toUriString();

      return restTemplate.getForObject(url, MemberDto.class);
    } catch (Exception e) {
      log.error("Error fetching member with id {}: {}", id, e.getMessage());
      // 기본 Member 객체 반환 (ID만 포함)
      MemberDto memberDto = new MemberDto();
      memberDto.setId(id);
      return memberDto;
    }
  }
}
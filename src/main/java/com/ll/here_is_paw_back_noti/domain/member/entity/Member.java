package com.ll.here_is_paw_back_noti.domain.member.entity;

import com.ll.here_is_paw_back_noti.global.base.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Member extends BaseEntity {
  private String username;
  private String nickname;

  // 필요한 최소 정보만 포함
  // 필요에 따라 추가 필드 확장 가능
}
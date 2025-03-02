package com.ll.here_is_paw_back_noti.domain.noti.entity;

import com.ll.here_is_paw_back_noti.domain.member.dto.MemberDto;
import com.ll.here_is_paw_back_noti.global.base.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.Transient;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Entity
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class Noti extends BaseEntity {

  private Long senderId;

  private Long receiverId;

  // @Transient 필드는 데이터베이스에 저장되지 않음
  @Transient
  private transient MemberDto sender;

  @Transient
  private transient MemberDto receiver;

  private String eventName;

  private String message;

  private Long postId;

  private boolean read;

  public void markAsRead() {
    this.read = true;
  }


}

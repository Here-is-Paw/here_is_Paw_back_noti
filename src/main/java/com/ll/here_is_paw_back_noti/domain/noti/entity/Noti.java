package com.ll.here_is_paw_back_noti.domain.noti.entity;

import com.ll.here_is_paw_back_noti.domain.member.entity.Member;
import com.ll.here_is_paw_back_noti.global.base.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
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

  @JoinColumn(name = "sender_id")
  private Member sender;

  @JoinColumn(name = "receiver_id")
  private Member receiver;

  private String eventName;

  private String message;

  private Long postId;

  private boolean read;

  public void markAsRead() {
    this.read = true;
  }


}

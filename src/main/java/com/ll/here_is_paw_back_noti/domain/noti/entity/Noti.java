package com.ll.here_is_paw_back_noti.domain.noti.entity;

import com.ll.here_is_paw_back_noti.global.jpa.BaseEntity;
import jakarta.persistence.Entity;
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
  private String senderNickname;
  private String senderAvatar;

  private Long receiverId;
  private String receiverNickname;
  private String receiverAvatar;

  private String eventName;

  private String message;

  private Long postId;

  private boolean read;

  public void markAsRead() {
    this.read = true;
  }


}

package com.ll.here_is_paw_back_noti.domain.noti.entity;

import static jakarta.persistence.GenerationType.IDENTITY;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class NotiRequest{

  @Id
  @GeneratedValue(strategy = IDENTITY)
  private Long id;
  private String title;
  private String content;
}

package com.ll.here_is_paw_back_noti.domain.noti.controller;


import com.ll.here_is_paw_back_noti.domain.member.dto.MemberDto;
import com.ll.here_is_paw_back_noti.domain.noti.entity.Noti;
import com.ll.here_is_paw_back_noti.domain.noti.service.NotiService;
import com.ll.here_is_paw_back_noti.domain.noti.service.SseService;
import com.ll.here_is_paw_back_noti.global.globalDto.GlobalResponse;
import com.ll.here_is_paw_back_noti.global.webMvc.LoginUser;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/noti")
@RequiredArgsConstructor
@Slf4j
public class NotiController {
  private final NotiService notiService;
  private final SseService sseService;
  private final List<Noti> notiList = new ArrayList<>();

//   알림 목록 조회
  @GetMapping
  public GlobalResponse<List<Noti>> getNotifications(
      @LoginUser MemberDto memberDto) {
    Long memberId = memberDto.getId();
    log.debug("memberId: {}", memberId);
    List<Noti> notifications = notiService.getAllNotifications(memberId);
    log.debug("notifications: {}", notifications.size());
    return GlobalResponse.success(notifications);
  }

  @PostMapping
  public GlobalResponse<String> testSendNotification(
      @LoginUser MemberDto memberDto) {
    Long senderId = memberDto.getId();
    Long receiverId = 4L;
    String eventName = "imageMatch";
    String message = "test message";
    Long postId = 1L;
    notiService.sendNotification(senderId, receiverId, eventName, message, postId);
    return GlobalResponse.success("noti보냄");
  }

  // 알림 읽음 처리
  @GetMapping("/{id}/read")
  public GlobalResponse markAsRead(@PathVariable("id") Long notiId) {
    notiService.markAsRead(notiId);
    return GlobalResponse.success("읽음 처리 완료");
  }


  // 모든 알림 읽음 처리
  @PostMapping("/read-all")
  public ResponseEntity<Void> markAllAsRead(@LoginUser MemberDto member) {
    notiService.markAllAsRead(member.getId());
    return ResponseEntity.ok().build();
  }
}

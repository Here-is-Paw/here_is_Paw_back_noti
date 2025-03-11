package com.ll.here_is_paw_back_noti.domain.noti.service;


import com.ll.here_is_paw_back_noti.domain.member.dto.MemberDto;
import com.ll.here_is_paw_back_noti.domain.member.service.MemberServiceClient;
import com.ll.here_is_paw_back_noti.domain.noti.entity.Noti;
import com.ll.here_is_paw_back_noti.domain.noti.kafka.dto.ImageMatchDto;
import com.ll.here_is_paw_back_noti.domain.noti.repository.NotiRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class NotiService {

  private static final String FINDING_TO_MISSING = "finding_to_missing";
  private static final String MISSING_TO_FINDING = "missing_to_finding";

  private final NotiRepository notiRepository;
  private final SseService sseService;

  private final MemberServiceClient memberServiceClient;

  public void sendToFindingNoti(Long receiverId, List<ImageMatchDto> matches) {
    matches.forEach(match -> {
      Long senderId = match.getTargetMemberId();
      MemberDto sender = memberServiceClient.getMemberById(senderId);

      String message = String.format(
          "%s님이 유사도 %.1f%% 인 강아지를 발견했습니다.",
          sender.getNickname(),
          match.getSimilarity() * 100);

      sendNotification(senderId, receiverId, "imageMatch", message, match.getPostId());
    });
  }

  public void sendToMissingNoti(Long senderId, Long postId, List<ImageMatchDto> matches) {
    MemberDto sender = memberServiceClient.getMemberById(senderId);

    matches.forEach(match -> {
      String message = String.format(
          "%s님이 유사도 %.1f%% 인 강아지를 발견했습니다.",
          sender.getNickname(),
          match.getSimilarity() * 100);

      sendNotification(senderId, match.getTargetMemberId(), "imageMatch", message, postId);
    });
  }

  @Transactional
  public void sendNotification(Long senderId, Long receiverId, String eventName, String message, Long postId) {
    MemberDto sender = memberServiceClient.getMemberById(senderId);
    MemberDto receiver = memberServiceClient.getMemberById(receiverId);

    Noti noti = Noti.builder()
        .senderId(senderId)
        .senderNickname(sender.getNickname())
        .senderAvatar(sender.getAvatar())
        .receiverId(receiverId)
        .receiverNickname(receiver.getNickname())
        .receiverAvatar(receiver.getAvatar())
        .eventName(eventName)
        .message(message)
        .postId(postId)
        .read(false)
        .build();

    notiRepository.save(noti);
    sseService.sendNoti(receiverId, "noti", noti);
  }


  // 특정 알림 읽음 처리
  @Transactional
  public void markAsRead(Long notificationId) {
    notiRepository.findById(notificationId).ifPresent(Noti::markAsRead);
  }

  public List<Noti> getAllNotifications(Long memberId) {
    return notiRepository.findByReceiverIdOrderByCreatedAtDesc(memberId);
  }

  @Transactional
  public void markAllAsRead(Long userId) {
    List<Noti> notifications = notiRepository.findByReceiverId(userId);
    for (Noti notification : notifications) {
      notification.setRead(true);
    }
    notiRepository.saveAll(notifications);
  }
}

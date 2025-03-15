package com.ll.here_is_paw_back_noti.domain.noti.service;


import com.ll.here_is_paw_back_noti.domain.noti.entity.Noti;
import com.ll.here_is_paw_back_noti.domain.noti.repository.NotiRepository;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@Service
@RequiredArgsConstructor
@Slf4j
public class SseService {
  private final Map<String, SseEmitter> emitters = new ConcurrentHashMap<>();
  private final NotiRepository notiRepository;

  public SseEmitter add(String userId, String connectionId) {
    // 복합 키 사용: userId + connectionId
    String connectionKey = userId + ":" + connectionId;
    log.debug("connectionKey: {}", connectionKey);

//    if (emitters.containsKey(connectionKey)) {
//      log.info("Found existing connection, replacing it: {}", connectionKey);
//      SseEmitter oldEmitter = emitters.remove(connectionKey);
//      if (oldEmitter != null) {
//        oldEmitter.complete();
//      }
//    }

    // 기존 emitter 삭제
//    removeUserConnections(userId);

    SseEmitter emitter = new SseEmitter(3600000L);

    // 연결 완료 콜백
    emitter.onCompletion(() -> {
      log.info("SSE connection completed for user: {}", userId);
      this.emitters.remove(connectionKey);  // connectionKey로 삭제
    });

    // 타임아웃 콜백
    emitter.onTimeout(() -> {
      log.info("SSE connection timeout for user: {}", userId);
      emitter.complete();
      this.emitters.remove(connectionKey);  // connectionKey로 삭제
    });

    // 에러 콜백
    emitter.onError((e) -> {
      log.error("SSE connection error for user: {}", userId, e);
      emitter.complete();
      this.emitters.remove(connectionKey);  // connectionKey로 삭제
    });

    // 초기 연결 이벤트 전송
    try {
      emitter.send(SseEmitter.event()
          .name("connect")
          .data("Connected to notification service"));
    } catch (IOException e) {
      log.error("Error sending initial SSE event to user: {}", userId, e);
      emitter.complete();
      return emitter;
    }

    // 복합 키로 emitter 저장
    this.emitters.put(connectionKey, emitter);
    log.info("SSE connection established for user: {} with connection ID: {}", userId, connectionId);

    return emitter;
  }

  private void removeUserConnections(String userId) {
    List<String> keysToRemove = emitters.keySet().stream()
        .filter(key -> key.startsWith(userId + ":"))
        .collect(Collectors.toList());

    for (String key : keysToRemove) {
      SseEmitter emitter = emitters.get(key);
      if (emitter != null) {
        emitter.complete();
        emitters.remove(key);
      }
    }
  }

//  public SseEmitter add(String userId){
//    if (emitters.containsKey(userId)) {
//      return emitters.get(userId);
//    }
//    // 타임아웃 설정 (1시간)
//    SseEmitter emitter = new SseEmitter(3600000L);
//
//    // 연결 완료 콜백
//    emitter.onCompletion(() -> {
//      log.info("SSE connection completed for user: {}", userId);
//      this.emitters.remove(userId);
//    });
//
//    // 타임아웃 콜백
//    emitter.onTimeout(() -> {
//      log.info("SSE connection timeout for user: {}", userId);
//      emitter.complete();
//      this.emitters.remove(userId);
//    });
//
//    // 에러 콜백
//    emitter.onError((e) -> {
//      log.error("SSE connection error for user: {}", userId, e);
//      emitter.complete();
//      this.emitters.remove(userId);
//    });
//
//    // 초기 연결 이벤트 전송
//    try {
//      emitter.send(SseEmitter.event()
//          .name("connect")
//          .data("Connected to notification service"));
//    } catch (IOException e) {
//      log.error("Error sending initial SSE event to user: {}", userId, e);
//      emitter.complete();
//      return emitter;
//    }
//
//    // 사용자 ID로 emitter 저장
//    this.emitters.put(userId, emitter);
//    log.info("SSE connection established for user: {}", userId);
//
//    return emitter;
//  }

//  public void sendNoti(Long userId, String eventName, Noti noti){
//    String userIdStr = String.valueOf(userId);
//    SseEmitter emitter = emitters.get(userIdStr);
//    log.debug("emitter start : {}", emitter);
//    if (emitter != null) {
//      try {
//        log.debug("Sending noti to user: {}", userIdStr);
//        emitter.send(SseEmitter.event()
//            .name(eventName)
//            .data(noti));
//
//      } catch (IOException e) {
//        log.debug("error noti to user: {}", userIdStr);
//        emitters.remove(userIdStr);
//        emitter.completeWithError(e);
//      }
//    }
//  }
//
//  public void sendNoti(Long userId, String eventName, Noti noti){
//    String userIdStr = String.valueOf(userId);
//    SseEmitter emitter = emitters.get(userIdStr);
//    log.debug("emitter start : {}", emitter);
//    if (emitter != null) {
//      try {
//        log.debug("Sending noti to user: {}", userIdStr);
//        emitter.send(SseEmitter.event()
//            .name(eventName)
//            .data(noti));
//      } catch (IOException e) {
//        log.debug("Error sending notification to user: {}", userIdStr, e);
//        // Remove the emitter on error and close it properly
//        emitters.remove(userIdStr);
//        emitter.complete();  // Complete instead of completeWithError to avoid exception propagation
//
//        // Don't rethrow exception - just log it and continue
//        log.info("Client connection closed, notification stored in repository for later delivery");
//      }
//    } else {
//      log.debug("No active emitter found for user: {}", userIdStr);
//    }
//  }

  public void sendNoti(Long userId, String eventName, Noti noti) {
    String userIdStr = String.valueOf(userId);

    emitters.entrySet().stream().forEach(entry -> log.debug("Key: {}, Value: {}", entry.getKey(), entry.getValue()));
    // Find all emitters for this user (regardless of connection ID)
    List<Map.Entry<String, SseEmitter>> userEmitters = emitters.entrySet().stream()
        .filter(entry -> entry.getKey().startsWith(userIdStr + ":"))
        .collect(Collectors.toList());

    if (userEmitters.isEmpty()) {
      log.debug("No active emitters found for user: {}", userIdStr);
      return;
    }

    // Send to all connections for this user
    for (Map.Entry<String, SseEmitter> entry : userEmitters) {
      try {
        log.debug("Sending noti to user: {} with connection: {}", userIdStr, entry.getKey());
        entry.getValue().send(SseEmitter.event()
            .name(eventName)
            .data(noti));
      } catch (IOException e) {
        log.debug("Error sending notification to connection: {}", entry.getKey(), e);
        emitters.remove(entry.getKey());
        entry.getValue().complete();
        log.info("Client connection closed, notification stored in repository for later delivery");
      }
    }
  }

//  private void sendUnreadNoti(String userId, SseEmitter emitter) throws IOException {
//    List<Noti> unreadNotifications = notiRepository.findByUserIdAndReadOrderByCreatedAtDesc(userId, false);
//
//    for (Noti noti : unreadNotifications) {
//      emitter.send(SseEmitter.event()
//          .name(noti.getEventName())
//          .data(noti.getNotiRequest())
//          .id(String.valueOf(noti.getId())));
//      log.debug("Unread noti sent: {}", noti.getId());
//      // 전송 후 읽음 처리
//      noti.markAsRead();
//    }
//
//    // 일괄 저장
//    if (!unreadNotifications.isEmpty()) {
//      notiRepository.saveAll(unreadNotifications);
//    }
//  }
//
//  public void sendNotificationToAll(String eventName, NotiRequest data) {
//    emitters.forEach((userId, emitter) -> {
//      sendNoti(userId, eventName, data);
//    });
//  }
//
//  public void removeEmitter(String userId) {
//    emitters.remove(userId);
//  }
}

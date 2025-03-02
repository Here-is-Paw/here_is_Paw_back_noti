package com.ll.here_is_paw_back_noti.domain.noti.controller;


import com.ll.here_is_paw_back_noti.domain.noti.service.SseService;
import com.ll.hereispaw.global.webMvc.LoginUser;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/sse")
@Tag(name = "SSE API", description = "SSE API")
public class SseController {
  private final SseService sseService;

  @GetMapping(value = "/connect", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
  public ResponseEntity<SseEmitter> add(@LoginUser Member member) {
    Long memberId = member.getId();
    return ResponseEntity.ok(sseService.add(memberId.toString()));
  }

//  @GetMapping(value = "/disconnect/{userId}", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
//  public ResponseEntity<Void> remove(@PathVariable String userId) {
//    sseService.removeEmitter(userId);
//    return ResponseEntity.ok().build();
//  }

}

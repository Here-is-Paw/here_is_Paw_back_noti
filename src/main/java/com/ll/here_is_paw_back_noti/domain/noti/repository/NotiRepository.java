package com.ll.here_is_paw_back_noti.domain.noti.repository;


import com.ll.here_is_paw_back_noti.domain.noti.entity.Noti;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NotiRepository extends JpaRepository<Noti, Long> {
  List<Noti> findByReceiverId(Long receiverId);
  List<Noti> findByReceiverIdOrderByCreatedAtDesc(Long receiverId);
}

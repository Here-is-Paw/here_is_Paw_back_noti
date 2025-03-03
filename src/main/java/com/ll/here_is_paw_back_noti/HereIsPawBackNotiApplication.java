package com.ll.here_is_paw_back_noti;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class HereIsPawBackNotiApplication {

  public static void main(String[] args) {
    SpringApplication.run(HereIsPawBackNotiApplication.class, args);
  }

}

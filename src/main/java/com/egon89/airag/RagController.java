package com.egon89.airag;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RagController {

  @PostMapping("/ask")
  public String ask(@RequestBody AskRequest question) {
    return "post Hello, " + question;
  }

  public record AskRequest(String question) {
  }
}

package com.vita.vitapickBack.config;

import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.extern.log4j.Log4j2;

@Log4j2
@RestController
public class CheckController {

	@GetMapping("/check-server")
	public ResponseEntity<?> checkLogin() {

		log.info(" * React SpringBoot Connection Test");

		return ResponseEntity.ok().body(
				Map.of(
						"checkData", "* Port:8080",
						"checkLogin", "* 로그인 확인 안됨"));
	}

}
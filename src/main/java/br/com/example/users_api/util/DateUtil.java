package br.com.example.users_api.util;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.springframework.stereotype.Component;

@Component
public class DateUtil {
	public String formatLocalDateBaseStyle(LocalDateTime localDateTime) {
		return DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss").format(localDateTime);
	}
}

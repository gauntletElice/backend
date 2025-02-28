package com.example.elice.common.scheduled;

import com.example.elice.fortuneCookie.service.FortuneCookieService;
import okhttp3.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.concurrent.CompletableFuture;

@Component
public class FortuneCookieScheduler {

    private final OkHttpClient client = new OkHttpClient();
    private final String API_URL = "https://api-cloud-function.elice.io/6b6ac971-f63f-4e75-b363-f8d40e9e08a1/v1/chat/completions";
    private final FortuneCookieService fortuneCookieService;
    private static final Logger logger = LoggerFactory.getLogger(FortuneCookieScheduler.class);

    public FortuneCookieScheduler(FortuneCookieService fortuneCookieService) {
        this.fortuneCookieService = fortuneCookieService;
    }

    @Value("${elice.key}")
    private String AUTH_TOKEN;

    // 비동기적으로 포춘쿠키 데이터를 가져오는 메서드
    public CompletableFuture<Void> fetchFortuneCookie() {
        return CompletableFuture.runAsync(() -> {
            String jsonPayload = "{\"model\":\"helpy-v-large\",\"messages\":[{\"role\":\"user\",\"content\":[{\"type\":\"text\",\"text\":\"포춘쿠키처럼 오늘의 운세는?(추임새 넣지 말고 오늘의 운세만 응답해주길 바래. 무조건 좋은걸로 할 필요 없이 랜덤으로 하나 보내줘.)\"}]}],\"max_tokens\":512}";

            if (AUTH_TOKEN == null || AUTH_TOKEN.isEmpty()) {
                logger.error("❌ AUTH_TOKEN 값이 비어 있습니다. YML 파일을 다시 확인하세요.");
                return;
            }

            logger.info("AUTH_TOKEN: " + AUTH_TOKEN);

            MediaType mediaType = MediaType.parse("application/json");
            RequestBody body = RequestBody.create(mediaType, jsonPayload);
            Request request = new Request.Builder()
                    .url(API_URL)
                    .post(body)
                    .addHeader("accept", "application/json")
                    .addHeader("content-type", "application/json")
                    .addHeader("authorization", "Bearer " + AUTH_TOKEN)
                    .build();

            try (Response response = client.newCall(request).execute()) {
                if (response.isSuccessful() && response.body() != null) {
                    String responseBody = response.body().string();
                    logger.info("✅ API 응답: " + responseBody);

                    // JSON 응답 파싱
                    ObjectMapper objectMapper = new ObjectMapper();
                    JsonNode rootNode = objectMapper.readTree(responseBody);
                    JsonNode choicesNode = rootNode.path("choices");
                    if (choicesNode.isArray() && choicesNode.size() > 0) {
                        JsonNode messageNode = choicesNode.get(0).path("message");
                        String content = messageNode.path("content").asText();  // content 값을 추출
                        logger.info("운세 내용: " + content);
                        fortuneCookieService.saveFortune(content);
                    } else {
                        logger.error("❌ 'choices' 배열이 비어있거나 예상하지 못한 구조입니다.");
                    }
                } else {
                    logger.error("❌ API 호출 실패: HTTP 코드 " + response.code());
                    String errorResponse = response.body() != null ? response.body().string() : "없음";
                    logger.error("응답 본문: " + errorResponse);
                }
            } catch (IOException e) {
                logger.error("❌ API 요청 중 오류 발생: " + e.getMessage(), e);
            }
        });
    }

    // 주기적으로 실행될 메서드 (비동기 호출)
    @Scheduled(cron = "0 0 9 * * *") // 매일 오전 9시 실행
    public void scheduleFetchFortuneCookie() {
        fetchFortuneCookie().thenRun(() -> {
            logger.info("✅ 비동기 작업 완료");
        }).exceptionally(ex -> {
            logger.error("❌ 비동기 작업 실패: " + ex.getMessage(), ex);
            return null;
        });
    }
}

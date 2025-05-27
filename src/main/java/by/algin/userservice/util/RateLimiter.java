package by.algin.userservice.util;

import by.algin.userservice.config.AppProperties;
import by.algin.userservice.exception.RateLimitExceededException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.Deque;
import java.util.LinkedList;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
@RequiredArgsConstructor
public class RateLimiter {

    private final AppProperties appProperties;
    private final Map<String, Deque<Long>> requestTimestamps = new ConcurrentHashMap<>();

    public void checkRateLimit(String key) {
        int maxRequests = appProperties.getRateLimit().getResendConfirmation().getMaxRequests();
        long windowSeconds = appProperties.getRateLimit().getResendConfirmation().getWindowSeconds();
        long currentTime = Instant.now().getEpochSecond();

        requestTimestamps.compute(key, (k, timestamps) -> {
            if (timestamps == null) {
                timestamps = new LinkedList<>();
            }

            while (!timestamps.isEmpty() && timestamps.peekFirst() < currentTime - windowSeconds) {
                timestamps.pollFirst();
            }

            if (timestamps.size() >= maxRequests) {
                throw new RateLimitExceededException(
                        String.format("Rate limit exceeded for %s. Maximum %d requests allowed in %d seconds.",
                                key, maxRequests, windowSeconds));
            }

            timestamps.addLast(currentTime);
            return timestamps;
        });
    }
}
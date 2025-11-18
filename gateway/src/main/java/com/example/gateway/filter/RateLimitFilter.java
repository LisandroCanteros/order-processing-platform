package com.example.gateway.filter;

import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.Refill;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class RateLimitFilter implements GlobalFilter, Ordered {
    private static final Logger log = LoggerFactory.getLogger(RateLimitFilter.class);
    private final Map<String, Bucket> buckets =  new ConcurrentHashMap<>();

    // 100 requests/min per client
    private Bucket createNewBucket() {
        Refill refill = Refill.greedy(100, Duration.ofMinutes(1));
        Bandwidth bandwidthLimit = Bandwidth.classic(100, refill);
        return Bucket.builder().addLimit(bandwidthLimit).build();
    }

    private String keyFor(ServerWebExchange exchange) {
        // can be changed to API key, header, etc
        String ip = exchange.getRequest().getHeaders().getFirst("X-Forwarded-For");
        if (ip == null) {
            ip = exchange.getRequest().getRemoteAddress() != null
                    ? exchange.getRequest().getRemoteAddress().getAddress().getHostAddress()
                    : "unknown";
        }
        return ip;
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        String key = keyFor(exchange);
        Bucket bucket = buckets.computeIfAbsent(key, k -> createNewBucket());

        if (bucket.tryConsume(1)) {
            return chain.filter(exchange);
        } else {
            log.warn("Rate limit exceeded for key {}", key);
            exchange.getResponse().setStatusCode(HttpStatus.TOO_MANY_REQUESTS);
            return exchange.getResponse().setComplete();
        }
    }

    @Override
    public int getOrder() {
        return 0;
    }
}

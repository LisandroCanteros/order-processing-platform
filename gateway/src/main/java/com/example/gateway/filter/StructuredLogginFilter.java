package com.example.gateway.filter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpMethod;
import org.springframework.http.server.RequestPath;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Component
public class StructuredLogginFilter implements GlobalFilter, Ordered {
    private static final Logger logger = LoggerFactory.getLogger(StructuredLogginFilter.class);
    private static final String REQUEST_ID_HEADER = "X-Request-Id";

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        String requestId = exchange.getRequest().getHeaders().getFirst(REQUEST_ID_HEADER);
        if (requestId == null || requestId.isBlank()) {
            String newRequestId = UUID.randomUUID().toString();
            exchange = exchange.mutate()
                    .request(r -> r.headers(h -> h.add(REQUEST_ID_HEADER, newRequestId)))
                    .build();

        }

        long start = System.currentTimeMillis();
        HttpMethod method = exchange.getRequest().getMethod();
        RequestPath path = exchange.getRequest().getPath();

        logger.info("gateway.request.start {} {} {}", method, path, requestId);

        ServerWebExchange finalExchange = exchange;
        return chain.filter(exchange).doOnSuccess(done -> {
            long duration = System.currentTimeMillis() - start;
            int status = finalExchange.getResponse().getStatusCode() != null ?  finalExchange.getResponse().getStatusCode().value() : 200;
            logger.info("gateway.request.end {} {} {} status={} durationMs={}", method, path, requestId, status, duration);
        });
    }

    @Override
    public int getOrder() {
        return -1;
    }
}

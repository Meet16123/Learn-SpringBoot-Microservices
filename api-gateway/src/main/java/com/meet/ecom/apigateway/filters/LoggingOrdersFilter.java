package com.meet.ecom.apigateway.filters;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class LoggingOrdersFilter  extends AbstractGatewayFilterFactory<LoggingOrdersFilter.Config> {
    public LoggingOrdersFilter() {
        super(Config.class);
    }

    @Override
    public GatewayFilter apply(Config config) {
        return (exchange, chain) -> {
            log.info("Orders Filter: {}", exchange.getRequest().getURI());
            return chain.filter(exchange);
        };
    }


    public static class Config {
        // Put configuration properties here
    }
}

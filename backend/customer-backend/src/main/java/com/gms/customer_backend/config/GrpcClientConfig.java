package com.gms.customer_backend.config;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GrpcClientConfig {

    @Bean(destroyMethod = "shutdown")
    public ManagedChannel garageManagedChannel(
            @Value("${grpc.client.garage.host}") String host,
            @Value("${grpc.client.garage.port}") int port
    ) {

        return ManagedChannelBuilder
                .forAddress(host, port)
                .usePlaintext()
                .build();
    }
}
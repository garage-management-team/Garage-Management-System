package com.gms.customer_backend.config;

import com.gms.customer_backend.booking.grpc.CustomerInformationGrpcService;

import io.grpc.Server;
import io.grpc.ServerBuilder;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GrpcServerConfig {

    @Bean(initMethod = "start", destroyMethod = "shutdown")
    public Server customerGrpcServer(
            CustomerInformationGrpcService customerInformationGrpcService,
            @Value("${grpc.server.port:9091}") int port) {

        return ServerBuilder.forPort(port)
                .addService(customerInformationGrpcService)
                .build();
    }
}
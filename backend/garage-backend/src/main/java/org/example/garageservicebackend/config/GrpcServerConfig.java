package org.example.garageservicebackend.config;

import io.grpc.Server;
import io.grpc.ServerBuilder;

import org.example.garageservicebackend.grpc.GarageAvailabilityGrpcService;
import org.example.garageservicebackend.grpc.GarageServicesGrpcService;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;

@Configuration
public class GrpcServerConfig {

    @Value("${grpc.server.port:9090}")
    private int grpcPort;

    @Bean(destroyMethod = "shutdown")
    public Server grpcServer(
            GarageAvailabilityGrpcService availabilityService,
            GarageServicesGrpcService servicesService
    ) throws IOException {

        return ServerBuilder
                .forPort(grpcPort)
                .addService(availabilityService)
                .addService(servicesService)
                .build()
                .start();
    }
}
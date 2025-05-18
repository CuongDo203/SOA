package com.microservice.quiz_creation_service.clients;

import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(name = "validation-service")
public interface ValidationServiceClient {
}

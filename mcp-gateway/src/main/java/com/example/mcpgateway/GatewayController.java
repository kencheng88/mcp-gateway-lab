package com.example.mcpgateway;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/gateway")
public class GatewayController {

    private final SidecarAggregationService aggregationService;

    public GatewayController(SidecarAggregationService aggregationService) {
        this.aggregationService = aggregationService;
    }

    @GetMapping("/status")
    public Map<String, Object> getStatus() {
        return Map.of(
                "status", "Gateway is running",
                "connected_sidecars", aggregationService.getActiveSidecars());
    }
}

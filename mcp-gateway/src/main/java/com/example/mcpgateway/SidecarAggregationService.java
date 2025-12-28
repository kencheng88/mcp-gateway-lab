package com.example.mcpgateway;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.mcp.client.McpClient;
import org.springframework.ai.mcp.client.stdio.StdioClientClientTransport;
import org.springframework.ai.mcp.client.webmvc.WebMvcMcpClientTransport;
import org.springframework.ai.mcp.server.McpServer;
import org.springframework.ai.mcp.server.McpServerContainer;
import org.springframework.ai.mcp.spec.McpSchema;
import org.springframework.ai.mcp.spring.McpServerConnectionAdapter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import jakarta.annotation.PostConstruct;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class SidecarAggregationService {

    private static final Logger log = LoggerFactory.getLogger(SidecarAggregationService.class);

    @Value("${registry.url:http://localhost:8082/api/registry/sidecars}")
    private String registryUrl;

    private final RestTemplate restTemplate;

    // 儲存對各個 Sidecar 的 MCP Client
    // 實際上在 Gateway 模式中，我們可能需要一個更複雜的 Proxy 機制，
    // 這裡先實作「向 Registry 拿清單並列出」的基礎結構。
    private final Map<String, String> activeSidecars = new ConcurrentHashMap<>();

    public SidecarAggregationService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @PostConstruct
    public void syncWithRegistry() {
        log.info("正在從 Registry 同步 Sidecar 清單: {}", registryUrl);
        try {
            ResponseEntity<Map<String, String>> response = restTemplate.exchange(
                    registryUrl,
                    HttpMethod.GET,
                    null,
                    new ParameterizedTypeReference<Map<String, String>>() {
                    });

            if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
                activeSidecars.clear();
                activeSidecars.putAll(response.getBody());
                log.info("同步成功，目前發現 {} 個 Sidecar", activeSidecars.size());
            }
        } catch (Exception e) {
            log.error("無法連線至 Registry: {}", e.getMessage());
        }
    }

    public Map<String, String> getActiveSidecars() {
        return activeSidecars;
    }
}

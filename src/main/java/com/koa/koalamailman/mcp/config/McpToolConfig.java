package com.koa.koalamailman.mcp.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.koa.koalamailman.mcp.tool.MandalartMcpTools;
import io.modelcontextprotocol.server.transport.WebMvcSseServerTransportProvider;
import org.springframework.ai.mcp.server.autoconfigure.McpServerProperties;
import org.springframework.ai.tool.ToolCallbackProvider;
import org.springframework.ai.tool.method.MethodToolCallbackProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.function.RouterFunction;
import org.springframework.web.servlet.function.ServerResponse;

@Configuration
public class McpToolConfig {

    @Bean
    public WebMvcSseServerTransportProvider webMvcSseServerTransportProvider(
            ObjectMapper objectMapper,
            McpServerProperties serverProperties
    ) {
        return new WebMvcSseServerTransportProvider(
                objectMapper,
                serverProperties.getBaseUrl(),
                serverProperties.getSseMessageEndpoint(),
                serverProperties.getSseEndpoint()
        );
    }

    @Bean
    public RouterFunction<ServerResponse> mcpRouterFunction(WebMvcSseServerTransportProvider transportProvider) {
        return transportProvider.getRouterFunction();
    }

    @Bean
    public ToolCallbackProvider mandalartToolCallbackProvider(MandalartMcpTools mandalartMcpTools) {
        return MethodToolCallbackProvider.builder()
                .toolObjects(mandalartMcpTools)
                .build();
    }
}

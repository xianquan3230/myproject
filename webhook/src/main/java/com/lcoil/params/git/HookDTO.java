package com.lcoil.params.git;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@Data
public class HookDTO {
    @JsonProperty("type")
    private String type;
    @JsonProperty("id")
    private Integer id;
    @JsonProperty("name")
    private String name;
    @JsonProperty("active")
    private Boolean active;
    @JsonProperty("events")
    private List<String> events;
    @JsonProperty("config")
    private ConfigDTO config;
    @JsonProperty("updated_at")
    private String updatedAt;
    @JsonProperty("created_at")
    private String createdAt;
    @JsonProperty("url")
    private String url;
    @JsonProperty("test_url")
    private String testUrl;
    @JsonProperty("ping_url")
    private String pingUrl;
    @JsonProperty("deliveries_url")
    private String deliveriesUrl;
    @JsonProperty("last_response")
    private LastResponseDTO lastResponse;

    @NoArgsConstructor
    @Data
    public static class ConfigDTO {
        @JsonProperty("content_type")
        private String contentType;
        @JsonProperty("insecure_ssl")
        private String insecureSsl;
        @JsonProperty("url")
        private String url;
    }

    @NoArgsConstructor
    @Data
    public static class LastResponseDTO {
        @JsonProperty("code")
        private Object code;
        @JsonProperty("status")
        private String status;
        @JsonProperty("message")
        private Object message;
    }
}
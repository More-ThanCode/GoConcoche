package com.more_than_code.go_con_coche.cloudinary;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
@ConfigurationProperties(prefix = "cloudinary")
@Data
public class CloudinaryProperties {
    private String cloudName;
    private String apiKey;
    private String apiSecret;
    private Map<DefaultImageType, String> defaultImages = new HashMap<>();
}

package com.example.coursesuggestionapp.Configuration;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "py-recommender")
@Data
public class PyRecommenderConfig {
    private String url;
    private String port;
}

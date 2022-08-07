package com.onlyabhinav.cowinslots.configs;



import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import javax.annotation.PostConstruct;

@Data
@Configuration
public class AppConfig {

    private static Logger logger = LoggerFactory.getLogger(AppConfig.class);

    @Value("${min.availability}")
    private int minAvailability;

    @Value("${notification.sound.low}")
    private String soundFileLow;

    @Value("${notification.sound.med}")
    private String soundFileMed;

    @Value("${notification.sound.hi}")
    private String soundFileHi;

    @PostConstruct
    public void propsLoaded() {
        logger.info("Properties Loaded {}", this);
    }

}

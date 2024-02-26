package com.azki.wallet;

import com.azki.wallet.CacheConfig.CacheConfigBookReader;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Import;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;

import static org.springframework.data.redis.core.RedisKeyValueAdapter.EnableKeyspaceEvents.ON_STARTUP;

@SpringBootApplication
@Import({CacheConfigBookReader.class})
@EnableCaching
@EnableConfigurationProperties
@EnableRedisRepositories(enableKeyspaceEvents = ON_STARTUP, keyspaceNotificationsConfigParameter = "")
public class WalletApplication {

	public static void main(String[] args) {
		SpringApplication.run(WalletApplication.class, args);
	}

}

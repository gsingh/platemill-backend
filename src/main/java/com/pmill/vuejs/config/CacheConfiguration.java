package com.pmill.vuejs.config;

import java.time.Duration;

import org.ehcache.config.builders.*;
import org.ehcache.jsr107.Eh107Configuration;

import org.hibernate.cache.jcache.ConfigSettings;
import io.github.jhipster.config.JHipsterProperties;

import org.springframework.boot.autoconfigure.cache.JCacheManagerCustomizer;
import org.springframework.boot.autoconfigure.orm.jpa.HibernatePropertiesCustomizer;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.*;

@Configuration
@EnableCaching
public class CacheConfiguration {

    private final javax.cache.configuration.Configuration<Object, Object> jcacheConfiguration;

    public CacheConfiguration(JHipsterProperties jHipsterProperties) {
        JHipsterProperties.Cache.Ehcache ehcache =
            jHipsterProperties.getCache().getEhcache();

        jcacheConfiguration = Eh107Configuration.fromEhcacheCacheConfiguration(
            CacheConfigurationBuilder.newCacheConfigurationBuilder(Object.class, Object.class,
                ResourcePoolsBuilder.heap(ehcache.getMaxEntries()))
                .withExpiry(ExpiryPolicyBuilder.timeToLiveExpiration(Duration.ofSeconds(ehcache.getTimeToLiveSeconds())))
                .build());
    }

    @Bean
    public HibernatePropertiesCustomizer hibernatePropertiesCustomizer(javax.cache.CacheManager cacheManager) {
        return hibernateProperties -> hibernateProperties.put(ConfigSettings.CACHE_MANAGER, cacheManager);
    }

    @Bean
    public JCacheManagerCustomizer cacheManagerCustomizer() {
        return cm -> {
            createCache(cm, com.pmill.vuejs.repository.UserRepository.USERS_BY_LOGIN_CACHE);
            createCache(cm, com.pmill.vuejs.repository.UserRepository.USERS_BY_EMAIL_CACHE);
            createCache(cm, com.pmill.vuejs.domain.User.class.getName());
            createCache(cm, com.pmill.vuejs.domain.Authority.class.getName());
            createCache(cm, com.pmill.vuejs.domain.User.class.getName() + ".authorities");
            createCache(cm, com.pmill.vuejs.domain.Production.class.getName());
            createCache(cm, com.pmill.vuejs.domain.EventOfPlateMill.class.getName());
            createCache(cm, com.pmill.vuejs.domain.EventOfPlateMill.class.getName() + ".pictureOfEvents");
            createCache(cm, com.pmill.vuejs.domain.EventOfPlateMill.class.getName() + ".videoOfEvents");
            createCache(cm, com.pmill.vuejs.domain.PictureOfEvent.class.getName());
            createCache(cm, com.pmill.vuejs.domain.VideoOfEvent.class.getName());
            createCache(cm, com.pmill.vuejs.domain.ShiftManager.class.getName());
            createCache(cm, com.pmill.vuejs.domain.ShiftManager.class.getName() + ".prods");
            createCache(cm, com.pmill.vuejs.domain.ShiftManager.class.getName() + ".hpFinishes");
            createCache(cm, com.pmill.vuejs.domain.ShiftManager.class.getName() + ".normaliseds");
            createCache(cm, com.pmill.vuejs.domain.ShiftManager.class.getName() + ".shippings");
            createCache(cm, com.pmill.vuejs.domain.HeavyPlateFinished.class.getName());
            createCache(cm, com.pmill.vuejs.domain.Normalising.class.getName());
            createCache(cm, com.pmill.vuejs.domain.Shipping.class.getName());
            // jhipster-needle-ehcache-add-entry
        };
    }

    private void createCache(javax.cache.CacheManager cm, String cacheName) {
        javax.cache.Cache<Object, Object> cache = cm.getCache(cacheName);
        if (cache != null) {
            cm.destroyCache(cacheName);
        }
        cm.createCache(cacheName, jcacheConfiguration);
    }
}

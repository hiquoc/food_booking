package com.huy.food.configs;

import javax.sql.DataSource;

import org.flywaydb.core.Flyway;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.jpa.autoconfigure.EntityManagerFactoryDependsOnPostProcessor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

/**
 * Runs Flyway database migrations before the JPA {@code EntityManagerFactory} is
 * initialized.
 * <p>
 * Spring Boot 4 no longer ships Flyway auto-configuration, so the Flyway instance is
 * built and launched manually here. Ordering with Hibernate is guaranteed through
 * {@link EntityManagerFactoryDependsOnPostProcessor}, which declares that the JPA
 * entity manager factory depends on the {@code flywayInitializer} bean, forcing the
 * migration to complete before {@code spring.jpa.hibernate.ddl-auto} is applied.
 * <p>
 * Standard {@code spring.flyway.*} properties are honoured for the common switches
 * ({@code enabled}, {@code locations}, {@code baseline-on-migrate}, {@code baseline-version}).
 */
@Configuration(proxyBeanMethods = false)
public class FlywayConfig {

    /** Bean name of the Flyway migration initializer registered by this configuration. */
    public static final String FLYWAY_INITIALIZER_BEAN_NAME = "flywayInitializer";

    @Bean(name = FLYWAY_INITIALIZER_BEAN_NAME, initMethod = "migrate", destroyMethod = "")
    @ConditionalOnProperty(prefix = "spring.flyway", name = "enabled", havingValue = "true", matchIfMissing = true)
    Flyway flyway(DataSource dataSource, Environment environment) {
        String locations = environment.getProperty("spring.flyway.locations", "classpath:db/migration");
        boolean baselineOnMigrate =
                environment.getProperty("spring.flyway.baseline-on-migrate", Boolean.class, true);
        String baselineVersion = environment.getProperty("spring.flyway.baseline-version", "1");
        return Flyway.configure()
                .dataSource(dataSource)
                .locations(locations)
                .baselineOnMigrate(baselineOnMigrate)
                .baselineVersion(baselineVersion)
                .load();
    }

    @Bean
    @ConditionalOnProperty(prefix = "spring.flyway", name = "enabled", havingValue = "true", matchIfMissing = true)
    static EntityManagerFactoryDependsOnPostProcessor entityManagerFactoryDependsOnFlyway() {
        return new EntityManagerFactoryDependsOnPostProcessor(FLYWAY_INITIALIZER_BEAN_NAME);
    }
}
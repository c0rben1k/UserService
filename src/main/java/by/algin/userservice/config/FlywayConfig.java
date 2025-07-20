package by.algin.userservice.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.flywaydb.core.Flyway;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;

import javax.sql.DataSource;

@Slf4j
@RequiredArgsConstructor
public class FlywayConfig {

    private final DataSource dataSource;
    private final AppProperties appProperties;

    @EventListener(ApplicationReadyEvent.class)
    public void runMigration() {
        log.info("Starting FORCED Flyway migration...");
        try {
            String migrationLocation = appProperties.getDatabase().getMigration().getLocation();
            log.info("Using migration location: {}", migrationLocation);

            Flyway flyway = Flyway.configure()
                    .dataSource(dataSource)
                    .locations(migrationLocation)
                    .baselineOnMigrate(true)
                    .validateOnMigrate(false)
                    .cleanDisabled(false)
                    .load();

            log.info("Cleaning Flyway schema history to force re-execution...");
            flyway.clean();

            log.info("Executing migration...");
            var result = flyway.migrate();

            log.info("FORCED Flyway migration completed successfully. Migrations executed: {}", result.migrationsExecuted);
        } catch (Exception e) {
            log.error("FORCED Flyway migration failed: {}", e.getMessage(), e);
            throw new RuntimeException("Database migration failed", e);
        }
    }
}

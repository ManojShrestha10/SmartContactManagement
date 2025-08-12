package com.scm.component;

import java.util.concurrent.ThreadLocalRandom;

import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;

public class CustomServiceHealthIndicator implements HealthIndicator {

    @Override
    public Health health() {
        double chance = ThreadLocalRandom.current().nextDouble();
        // Simulate a random failure with a 10% chance
        if (chance > 0.9) {
            return Health.down()
            .withDetail("error", "Random failure occurred")
            .withDetail("chance", chance)
            .build();

        }
        return Health.up()
        .withDetail("chance", chance)
        .build();
    }

}

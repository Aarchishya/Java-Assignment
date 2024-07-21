package com.example.reportgenerator.config;

import org.junit.jupiter.api.Test;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;



import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

import static org.junit.jupiter.api.Assertions.*;

class SchedulerConfigTest {

    @Test
    void testThreadPoolTaskSchedulerProperties() {
        // Arrange
        SchedulerConfig schedulerConfig = new SchedulerConfig();
        ScheduledTaskRegistrar taskRegistrar = new ScheduledTaskRegistrar();

        // Act
        schedulerConfig.configureTasks(taskRegistrar);

        // Assert
        ThreadPoolTaskScheduler scheduler = (ThreadPoolTaskScheduler) taskRegistrar.getScheduler();

        // Test that the scheduler is initialized
        assertDoesNotThrow(() -> scheduler.getScheduledExecutor());

        // Test thread naming indirectly
        CountDownLatch latch = new CountDownLatch(1);
        AtomicReference<String> threadName = new AtomicReference<>();

        scheduler.execute(() -> {
            threadName.set(Thread.currentThread().getName());
            latch.countDown();
        });

        try {
            latch.await(5, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            fail("Timed out waiting for task execution");
        }

        assertNotNull(threadName.get());
        assertTrue(threadName.get().startsWith("scheduled-task-pool-"));
    }
}
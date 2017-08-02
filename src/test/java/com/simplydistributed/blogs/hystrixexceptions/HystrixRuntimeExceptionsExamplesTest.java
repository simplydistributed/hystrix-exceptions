package com.simplydistributed.blogs.hystrixexceptions;

import com.netflix.hystrix.exception.HystrixRuntimeException;
import org.junit.Test;

import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.TimeoutException;

import static org.assertj.core.api.Assertions.assertThat;

public class HystrixRuntimeExceptionsExamplesTest {
    @Test
    public void FailedWithUserException_failsWithRuntimeException() throws Exception {
        try {
            new HystrixRuntimeExceptionsExamples.FailedWithUserException().execute();
            assertThat(false).describedAs("Should have failed").isTrue();
        } catch (HystrixRuntimeException ex) {
            assertThat(ex.getCause()).isNotNull();
            assertThat(ex.getCause().getMessage()).isEqualTo("My user exception");
        }
    }

    @Test
    public void FailedWithTimeoutException() throws Exception {
        try {
            new HystrixRuntimeExceptionsExamples.FailedWithTimeoutException().execute();
            assertThat(false).describedAs("Should have failed").isTrue();
        } catch (HystrixRuntimeException ex) {
            assertThat(ex.getCause()).isInstanceOf(TimeoutException.class);
        }
    }

    @Test
    public void FailedBecauseShortCircuited_failsWithRuntimeException() throws Exception {
        try {
            new HystrixRuntimeExceptionsExamples.FailedBecauseShortCircuited().execute();
            assertThat(false).describedAs("Should have failed").isTrue();
        } catch (HystrixRuntimeException ex) {
            assertThat(ex.getCause()).isInstanceOf(RuntimeException.class);
            assertThat(ex.getCause().getMessage()).isEqualTo("Hystrix circuit short-circuited and is OPEN");
        }
    }

    @Test
    public void FailedBecauseThreadPoolExhausted_failsWithRejectedException() throws Exception {

        try {
            new HystrixRuntimeExceptionsExamples.FailedBecauseThreadPoolExhausted().queue();
            new HystrixRuntimeExceptionsExamples.FailedBecauseThreadPoolExhausted().execute();
        } catch (HystrixRuntimeException ex) {
            assertThat(ex.getCause()).isInstanceOf(RejectedExecutionException.class);
            assertThat(ex.getMessage()).contains("could not be queued for execution");
        }
    }

    @Test
    public void FailedBecauseSemaphoreExhausted_failsWithRejectedException() throws Exception {

        try {
            new HystrixRuntimeExceptionsExamples.FailedBecauseSemaphoreExhausted().queue();
            new HystrixRuntimeExceptionsExamples.FailedBecauseSemaphoreExhausted().execute();
        } catch (HystrixRuntimeException ex) {
            assertThat(ex.getCause()).isInstanceOf(RuntimeException.class);
            assertThat(ex.getMessage()).contains("could not acquire a semaphore for execution");
        }
    }
}
package com.simplydistributed.blogs.hystrixexceptions;

import com.netflix.hystrix.HystrixCommand;
import com.netflix.hystrix.HystrixCommandGroupKey;
import com.netflix.hystrix.HystrixCommandProperties;
import com.netflix.hystrix.HystrixThreadPoolProperties;
import com.netflix.hystrix.exception.HystrixBadRequestException;

public class HystrixRuntimeExceptionsExamples {
    public static class FailedWithUserException extends HystrixCommand<String> {

        protected FailedWithUserException() {
            super(HystrixCommandGroupKey.Factory.asKey("FailedWithUserException"));
        }

        @Override
        protected String run() throws Exception {
            throw new RuntimeException("My user exception");
        }
    }

    public static class FailedWithTimeoutException extends HystrixCommand<String> {

        protected FailedWithTimeoutException() {
            super(Setter.withGroupKey(HystrixCommandGroupKey.Factory.asKey("FailedWithTimeoutException"))
                    .andCommandPropertiesDefaults(
                            HystrixCommandProperties.Setter()
                                    .withExecutionTimeoutInMilliseconds(500)));
        }

        @Override
        protected String run() throws Exception {
            Thread.sleep(1000);
            return null;
        }
    }

    public static class FailedBecauseShortCircuited extends HystrixCommand<String> {

        protected FailedBecauseShortCircuited() {
            super(Setter.withGroupKey(HystrixCommandGroupKey.Factory.asKey("FailedBecauseShortCircuited"))
                    .andCommandPropertiesDefaults(HystrixCommandProperties.Setter()
                            .withCircuitBreakerForceOpen(true))
            );
        }

        @Override
        protected String run() throws Exception {
            return null;
        }
    }

    public static class FailedBecauseThreadPoolExhausted extends HystrixCommand<String> {

        protected FailedBecauseThreadPoolExhausted() {
            super(Setter.withGroupKey(HystrixCommandGroupKey.Factory.asKey("FailedBecauseThreadPoolExhausted"))
                    .andThreadPoolPropertiesDefaults(HystrixThreadPoolProperties.Setter()
                            .withCoreSize(1)
                            .withMaxQueueSize(0)
                    )
            );
        }

        @Override
        protected String run() throws Exception {
            Thread.sleep(1000);
            return "My message";
        }
    }

    public static class FailedBecauseSemaphoreExhausted extends HystrixCommand<String> {

        protected FailedBecauseSemaphoreExhausted() {
            super(Setter.withGroupKey(HystrixCommandGroupKey.Factory.asKey("FailedBecauseSemaphoreExhausted"))
                    .andCommandPropertiesDefaults(HystrixCommandProperties.Setter()
                        .withExecutionIsolationStrategy(HystrixCommandProperties.ExecutionIsolationStrategy.SEMAPHORE)
                            .withExecutionIsolationSemaphoreMaxConcurrentRequests(0)

                    )
            );
        }

        @Override
        protected String run() throws Exception {
            Thread.sleep(1000);
            return "My message";
        }
    }

    public static class FailedBecauseHystrixBadRequestException extends HystrixCommand<String> {
        protected FailedBecauseHystrixBadRequestException() {
            super(Setter.withGroupKey(HystrixCommandGroupKey.Factory.asKey("FailedBecauseHystrixBadRequestException")));
        }

        @Override
        protected String run() throws Exception {
            throw new HystrixBadRequestException("Error running exception", new RuntimeException("Bad request exception"));
        }
    }
}

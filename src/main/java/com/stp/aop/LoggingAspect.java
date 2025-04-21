package com.stp.aop;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class LoggingAspect {

	private static final Logger logger = LoggerFactory.getLogger(LoggingAspect.class);

	// Before method execution
	@Before("execution(* com.stp.controller.NativeQueryController.*(..))")
	public void logMethodStartNative(JoinPoint joinPoint) {
		String methodName = joinPoint.getSignature().getName();
		Object[] args = joinPoint.getArgs();
		logger.info("NativeQueryController Method {} called with arguments: {}", methodName, args);
	}

	// After method execution (successful completion)
	@After("execution(* com.stp.controller.NativeQueryController.*(..))")
	public void logMethodEndNative(JoinPoint joinPoint) {
		String methodName = joinPoint.getSignature().getName();
		logger.info("NativeQueryController NativeQueryControllerMethod {} executed successfully.", methodName);
	}

	// After method execution (exception thrown)
	@AfterThrowing(pointcut = "execution(* com.stp.controller.NativeQueryController.*(..))", throwing = "exception")
	public void logMethodExceptionNative(JoinPoint joinPoint, Throwable exception) {
		String methodName = joinPoint.getSignature().getName();
		logger.error("NativeQueryController Method {} failed with exception: {}", methodName, exception.getMessage());
	}

	@Around("execution(* com.stp.controller.NativeQueryController.*(..))")
	public Object logExecutionTimeNative(ProceedingJoinPoint joinPoint) throws Throwable {
		long start = System.currentTimeMillis();
		Object proceed = joinPoint.proceed(); // This will execute the method
		long executionTime = System.currentTimeMillis() - start;
		String methodName = joinPoint.getSignature().getName();
		logger.info("NativeQueryController Method {} executed in {} ms", methodName, executionTime);
		return proceed;
	}

}

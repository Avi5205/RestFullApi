package in.kodder.todoapispring;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class TimeMonitorAspect {

    @Around("@annotation(TimeMonitor)")
    public void logTime(ProceedingJoinPoint joinPoint) throws Throwable {
        long startTime = System.currentTimeMillis();
        // Proceed with the method execution
        try {
            joinPoint.proceed();
        } catch (Throwable throwable) {
            // Handle any exceptions thrown by the method
            System.out.println("Exception in method: " + joinPoint.getSignature().getName() + " - " + throwable.getMessage());
        } finally {
            long endTime = System.currentTimeMillis();
            long duration = endTime - startTime;
            System.out.println("Method execution time: " + duration + " ms");
        }

    }
}

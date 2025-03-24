package com.stp.caller;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

public class ApiRequestExecutor {
    private static final int MAX_THREADS = 500; // Adjust based on your system's capability
    private static final String API_URL = "http://localhost:8000/api_ttum/fetchDetail";
    private static final String JSON_PAYLOAD = "{ \"type\": \"UPI\", \"reqdate\": \"20/06/2023\", \"queryid\":\"1\", \"queryDetail\":\"hello\"}";
    private static final int TOTAL_REQUESTS = 1000; // 1 crore
    private static final int BATCH_SIZE = 100; // Number of requests per batch

    public static void main(String[] args) {
        long startTime = System.currentTimeMillis();

        // Calculate number of batches needed
        int numBatches = TOTAL_REQUESTS / BATCH_SIZE;

        for (int batch = 0; batch < numBatches; batch++) {
            System.out.println("Starting batch " + (batch + 1) + " of " + numBatches);
            executeBatchRequests(BATCH_SIZE);
        }

        long endTime = System.currentTimeMillis();
        System.out.println("Completed " + TOTAL_REQUESTS + " requests in " + (endTime - startTime) / 1000.0 + " seconds");
    }

    public static void executeBatchRequests(int numRequests) {
        ExecutorService executorService = Executors.newFixedThreadPool(MAX_THREADS);
        List<Future<String>> futures = new ArrayList<>();

        // Submit tasks to the executor
        for (int i = 0; i < numRequests; i++) {
            futures.add(executorService.submit(() -> ApiClient.makePostRequest(API_URL, JSON_PAYLOAD)));
        }

        // Collect results
        int successCount = 0;
        for (Future<String> future : futures) {
            try {
                if (future.get() != null) {
                    successCount++;
                }
            } catch (InterruptedException | ExecutionException e) {
                System.err.println("Error in future execution: " + e.getMessage());
            }
        }

        executorService.shutdown();
        System.out.println("Batch completed with " + successCount + " successful requests");
    }
}

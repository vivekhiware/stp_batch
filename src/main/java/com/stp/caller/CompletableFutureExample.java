package com.stp.caller;

import java.util.concurrent.CompletableFuture;

public class CompletableFutureExample {

	public static void main(String[] args) {
		CompletableFuture<Integer> future = CompletableFuture.supplyAsync(() -> {
			System.out.println("Task 1: Starting...");
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			return 10; // Return value
		}).thenApplyAsync(result -> {
			System.out.println("Task 2: Processing " + result);
			return result * 2; // Modify the result
		}).thenApplyAsync(result -> {
			System.out.println("Task 3: Final result " + result);
			return result + 5; // Modify the result
		});

		// Wait for the final result
		Integer finalResult = future.join();
		System.out.println("Final Result: " + finalResult);
	}
}

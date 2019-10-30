package de.leonhard.storage.utils;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.concurrent.TimeUnit;

/*
Utility Class to benchmark
 */
@SuppressWarnings("WeakerAccess")
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class LagCatcher {
	private static final HashMap<String, Long> startTimes = new HashMap<>();
	private static final HashMap<String, Long> stopTimes = new HashMap<>();

	public static void start(String name) {
		if (LagCatcher.startTimes.containsKey(name)) {
			throw new IllegalStateException(("Test is already running for '" + name + '\''));
		}
		long nanoTime = System.nanoTime();
		LagCatcher.startTimes.put(name, nanoTime);
	}

	public static void stop(String name) {
		if (LagCatcher.stopTimes.containsKey(name)) {
			throw new IllegalStateException(("No test running for '" + name + '\''));
		}
		LagCatcher.stopTimes.put(name, System.nanoTime());
	}

	private static void show(String name) {
		if (!LagCatcher.startTimes.containsKey(name) || !LagCatcher.stopTimes.containsKey(name)) {
			throw new IllegalStateException(("No results found for '" + name + '\''));
		}
		Long value = LagCatcher.startTimes.get(name);
		if (value == null) {
			return;
		}
		long start = value;
		Long value2 = LagCatcher.stopTimes.get(name);
		if (value2 != null) {
			long end = value2;
			long took = end - start;
			System.out.println((Object) ("Test '" + name + "' took " + TimeUnit.NANOSECONDS.toMicros(took) +
					" micro-seconds. That's " + TimeUnit.NANOSECONDS.toMillis(took) + " ms."));
			LagCatcher.stopTimes.remove(name);
			LagCatcher.startTimes.remove(name);
		}
	}

	public static void stopAndShow(String name) {
		stop(name);
		show(name);
	}

	public static void runMultipleTimes(int cycles, Runnable runnable) {
		long nanosTook = 0L;
		for (int i = 0; i < cycles; ++i) {
			long nanoTime = System.nanoTime();
			runnable.run();
			nanosTook += System.nanoTime() - nanoTime;
		}
		System.out.println((Object) ("Average time: " + TimeUnit.NANOSECONDS.toMicros(nanosTook / cycles)
				+ " micros - " + TimeUnit.NANOSECONDS.toMillis(nanosTook / cycles) + " ms."));
		System.out.println((Object) ("Test took: " + TimeUnit.NANOSECONDS.toMicros(nanosTook) + " micros " +
				"- " + TimeUnit.NANOSECONDS.toMillis(nanosTook) + " ms"));
	}
}
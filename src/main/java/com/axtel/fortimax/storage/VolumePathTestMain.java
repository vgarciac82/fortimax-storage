package com.axtel.fortimax.storage;

import com.axtel.fortimax.storage.dto.VolumePathInfo;
import com.axtel.fortimax.storage.service.VolumePathService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

import java.io.FileWriter;
import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

//@SpringBootApplication
public class VolumePathTestMain {

	private static final Logger log = LoggerFactory.getLogger(VolumePathTestMain.class);

	public static void main(String[] args) throws InterruptedException {
		ConfigurableApplicationContext context = SpringApplication.run(VolumePathTestMain.class, args);
		VolumePathService service = context.getBean(VolumePathService.class);

		int threads = 10;
		int filesPerThread = 3000;

		ExecutorService executor = Executors.newFixedThreadPool(threads);

		for (int i = 0; i < threads; i++) {
			executor.submit(() -> {
				for (int j = 0; j < filesPerThread; j++) {
					try {
						VolumePathInfo info = service.getNextAvailablePath();
						try (FileWriter fw = new FileWriter(info.getFullPath())) {
							fw.write("Simulated content for " + info.getFileName());
						}
					} catch (IOException e) {
						log.error("[ERROR] IOException: " + e.getMessage(), e);
					} catch (Exception e) {
						log.error("[ERROR] Unexpected: " + e.getMessage(), e);
					}
				}
			});
		}

		executor.shutdown();
		executor.awaitTermination(30, TimeUnit.MINUTES);
		context.close();
	}
}

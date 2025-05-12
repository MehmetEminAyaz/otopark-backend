package com.otopark.backend;

import com.otopark.backend.entity.ParkingSlot;
import com.otopark.backend.repository.ParkingSlotRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class OtoparkManagementSystemApplication {

	public static void main(String[] args) {
		SpringApplication.run(OtoparkManagementSystemApplication.class, args);
	}
	@Bean
	CommandLineRunner initSlots(ParkingSlotRepository slotRepo) {
		return args -> {
			String[] rows = {"A","B"};
			int cols = 10;
			for (String row : rows) {
				for (int i = 1; i <= cols; i++) {
					String id = row + i;
					if (!slotRepo.existsById(id)) {
						slotRepo.save(ParkingSlot.builder()
								.slotId(id)
								.occupied(false)
								.build());
					}
				}
			}
		};
	}
}

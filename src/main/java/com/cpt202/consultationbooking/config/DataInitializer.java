package com.cpt202.consultationbooking.config;

import com.cpt202.consultationbooking.entity.*;
import com.cpt202.consultationbooking.enums.*;
import com.cpt202.consultationbooking.repository.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;

@Component
@Profile("!test")
public class DataInitializer implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(DataInitializer.class);

    private final UserRepository userRepository;
    private final CustomerRepository customerRepository;
    private final OperationManagerRepository operationManagerRepository;
    private final ExpertiseCategoryRepository categoryRepository;
    private final LevelRepository levelRepository;
    private final SpecialistRepository specialistRepository;
    private final AvailabilitySlotRepository slotRepository;

    public DataInitializer(UserRepository userRepository,
                          CustomerRepository customerRepository,
                          OperationManagerRepository operationManagerRepository,
                          ExpertiseCategoryRepository categoryRepository,
                          LevelRepository levelRepository,
                          SpecialistRepository specialistRepository,
                          AvailabilitySlotRepository slotRepository) {
        this.userRepository = userRepository;
        this.customerRepository = customerRepository;
        this.operationManagerRepository = operationManagerRepository;
        this.categoryRepository = categoryRepository;
        this.levelRepository = levelRepository;
        this.specialistRepository = specialistRepository;
        this.slotRepository = slotRepository;
    }

    @Override
    public void run(String... args) {
        if (userRepository.count() > 0) {
            log.info("Database already has data, skipping initialization");
            return;
        }

        log.info("Initializing sample data...");

        User managerUser = userRepository.save(User.builder()
                .username("manager1")
                .password("password123")
                .email("manager1@example.com")
                .address("123 Admin St")
                .role(UserRole.MANAGER)
                .status(UserStatus.ACTIVE)
                .build());
        operationManagerRepository.save(OperationManager.builder().user(managerUser).build());

        User customer1 = userRepository.save(User.builder()
                .username("customer1")
                .password("password123")
                .email("customer1@example.com")
                .address("456 Customer Ave")
                .role(UserRole.CUSTOMER)
                .status(UserStatus.ACTIVE)
                .build());
        customerRepository.save(Customer.builder().user(customer1).build());

        User customer2 = userRepository.save(User.builder()
                .username("customer2")
                .password("password123")
                .email("customer2@example.com")
                .address("789 Client Blvd")
                .role(UserRole.CUSTOMER)
                .status(UserStatus.ACTIVE)
                .build());
        customerRepository.save(Customer.builder().user(customer2).build());

        User specialistUser1 = userRepository.save(User.builder()
                .username("specialist1")
                .password("password123")
                .email("specialist1@example.com")
                .address("101 Expert Lane")
                .role(UserRole.SPECIALIST)
                .status(UserStatus.ACTIVE)
                .build());

        User specialistUser2 = userRepository.save(User.builder()
                .username("specialist2")
                .password("password123")
                .email("specialist2@example.com")
                .address("202 Professional Plaza")
                .role(UserRole.SPECIALIST)
                .status(UserStatus.ACTIVE)
                .build());

        ExpertiseCategory category1 = categoryRepository.save(ExpertiseCategory.builder()
                .name("Software Engineering")
                .status("ACTIVE")
                .build());

        ExpertiseCategory category2 = categoryRepository.save(ExpertiseCategory.builder()
                .name("Data Science")
                .status("ACTIVE")
                .build());

        Level level1 = levelRepository.save(Level.builder()
                .name("Junior")
                .build());

        Level level2 = levelRepository.save(Level.builder()
                .name("Senior")
                .build());

        Specialist specialist1 = specialistRepository.save(Specialist.builder()
                .user(specialistUser1)
                .category(category1)
                .level(level2)
                .status(SpecialistStatus.ACTIVE)
                .fee(new BigDecimal("150.00"))
                .information("Expert in software architecture and design patterns")
                .build());

        Specialist specialist2 = specialistRepository.save(Specialist.builder()
                .user(specialistUser2)
                .category(category2)
                .level(level1)
                .status(SpecialistStatus.ACTIVE)
                .fee(new BigDecimal("100.00"))
                .information("Specialist in machine learning and data analysis")
                .build());

        LocalDate today = LocalDate.now();

        slotRepository.save(AvailabilitySlot.builder()
                .specialist(specialist1)
                .date(today.plusDays(1))
                .startTime(LocalTime.of(9, 0))
                .endTime(LocalTime.of(10, 0))
                .status(SlotStatus.AVAILABLE)
                .build());

        slotRepository.save(AvailabilitySlot.builder()
                .specialist(specialist1)
                .date(today.plusDays(1))
                .startTime(LocalTime.of(10, 0))
                .endTime(LocalTime.of(11, 0))
                .status(SlotStatus.AVAILABLE)
                .build());

        slotRepository.save(AvailabilitySlot.builder()
                .specialist(specialist1)
                .date(today.plusDays(2))
                .startTime(LocalTime.of(14, 0))
                .endTime(LocalTime.of(15, 0))
                .status(SlotStatus.AVAILABLE)
                .build());

        slotRepository.save(AvailabilitySlot.builder()
                .specialist(specialist2)
                .date(today.plusDays(1))
                .startTime(LocalTime.of(11, 0))
                .endTime(LocalTime.of(12, 0))
                .status(SlotStatus.AVAILABLE)
                .build());

        slotRepository.save(AvailabilitySlot.builder()
                .specialist(specialist2)
                .date(today.plusDays(2))
                .startTime(LocalTime.of(9, 0))
                .endTime(LocalTime.of(10, 0))
                .status(SlotStatus.AVAILABLE)
                .build());

        log.info("Sample data initialization completed");
    }
}

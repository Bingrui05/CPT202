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
import java.time.DayOfWeek;
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

        // ============================================================
        // USERS
        // ============================================================

        // Operation Manager
        User managerUser = userRepository.save(User.builder()
                .username("manager1")
                .password("password123")
                .email("manager1@example.com")
                .address("123 Admin St")
                .role(UserRole.MANAGER)
                .status(UserStatus.ACTIVE)
                .build());
        operationManagerRepository.save(OperationManager.builder().user(managerUser).build());

        // Customers
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

        // Specialists
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

        // Additional specialists for richer demo data
        User specialistUser3 = userRepository.save(User.builder()
                .username("career_specialist")
                .password("password123")
                .email("career@example.com")
                .address("303 Career Center")
                .role(UserRole.SPECIALIST)
                .status(UserStatus.ACTIVE)
                .build());

        User specialistUser4 = userRepository.save(User.builder()
                .username("finance_specialist")
                .password("password123")
                .email("finance@example.com")
                .address("404 Finance Tower")
                .role(UserRole.SPECIALIST)
                .status(UserStatus.ACTIVE)
                .build());

        User specialistUser5 = userRepository.save(User.builder()
                .username("academic_specialist")
                .password("password123")
                .email("academic@example.com")
                .address("505 University Ave")
                .role(UserRole.SPECIALIST)
                .status(UserStatus.ACTIVE)
                .build());

        // ============================================================
        // EXPERTISE CATEGORIES
        // Demo users: manager1, customer1, customer2, specialist1-5
        // ============================================================

        ExpertiseCategory softwareEng = categoryRepository.save(ExpertiseCategory.builder()
                .name("Software Engineering")
                .status("ACTIVE")
                .build());

        ExpertiseCategory dataScience = categoryRepository.save(ExpertiseCategory.builder()
                .name("Data Science")
                .status("ACTIVE")
                .build());

        ExpertiseCategory careerConsulting = categoryRepository.save(ExpertiseCategory.builder()
                .name("Career Consulting")
                .status("ACTIVE")
                .build());

        ExpertiseCategory dataAnalytics = categoryRepository.save(ExpertiseCategory.builder()
                .name("Data Analytics")
                .status("ACTIVE")
                .build());

        ExpertiseCategory financeConsulting = categoryRepository.save(ExpertiseCategory.builder()
                .name("Finance Consulting")
                .status("ACTIVE")
                .build());

        ExpertiseCategory businessStrategy = categoryRepository.save(ExpertiseCategory.builder()
                .name("Business Strategy")
                .status("ACTIVE")
                .build());

        ExpertiseCategory academicPlanning = categoryRepository.save(ExpertiseCategory.builder()
                .name("Academic Planning")
                .status("ACTIVE")
                .build());

        // ============================================================
        // LEVELS
        // ============================================================

        Level junior = levelRepository.save(Level.builder()
                .name("Junior")
                .build());

        Level senior = levelRepository.save(Level.builder()
                .name("Senior")
                .build());

        Level expert = levelRepository.save(Level.builder()
                .name("Expert")
                .build());

        // ============================================================
        // SPECIALISTS
        // ============================================================

        // Specialist 1: Software Engineering - Senior
        Specialist specialist1 = specialistRepository.save(Specialist.builder()
                .user(specialistUser1)
                .category(softwareEng)
                .level(senior)
                .status(SpecialistStatus.ACTIVE)
                .fee(new BigDecimal("150.00"))
                .information("Senior software architect with 10+ years of experience in enterprise systems. " +
                        "Expert in microservices architecture, cloud-native applications, and scalable system design. " +
                        "Previously led teams at major tech companies, helping startups design robust backend infrastructure. " +
                        "Ideal for technical leaders seeking architectural guidance, system modernization advice, " +
                        "or help with complex technical decisions.")
                .build());

        // Specialist 2: Data Science - Junior
        Specialist specialist2 = specialistRepository.save(Specialist.builder()
                .user(specialistUser2)
                .category(dataScience)
                .level(junior)
                .status(SpecialistStatus.ACTIVE)
                .fee(new BigDecimal("100.00"))
                .information("Data science professional specializing in machine learning and statistical analysis. " +
                        "Experienced in Python, R, and various ML frameworks including TensorFlow and PyTorch. " +
                        "Passionate about turning raw data into actionable insights. " +
                        "Great for beginners exploring data science careers or businesses looking to implement " +
                        "data-driven decision making.")
                .build());

        // Specialist 3: Career Consulting - Senior
        Specialist specialist3 = specialistRepository.save(Specialist.builder()
                .user(specialistUser3)
                .category(careerConsulting)
                .level(senior)
                .status(SpecialistStatus.ACTIVE)
                .fee(new BigDecimal("120.00"))
                .information("Career transformation coach with 15 years of experience across multiple industries. " +
                        "Specialized in helping professionals navigate career transitions, negotiate salaries, " +
                        "and develop leadership skills. Certified career counselor with a track record of helping " +
                        "hundreds of clients achieve their career goals. " +
                        "Perfect for anyone feeling stuck in their career or planning a major professional pivot.")
                .build());

        // Specialist 4: Finance Consulting - Expert
        Specialist specialist4 = specialistRepository.save(Specialist.builder()
                .user(specialistUser4)
                .category(financeConsulting)
                .level(expert)
                .status(SpecialistStatus.ACTIVE)
                .fee(new BigDecimal("200.00"))
                .information("Financial advisor and investment strategist with CFA certification and 20 years " +
                        "of Wall Street experience. Expert in personal wealth management, retirement planning, " +
                        "portfolio diversification, and risk assessment. Former hedge fund manager now dedicated " +
                        "to helping individuals and small businesses achieve financial independence. " +
                        "Suitable for clients seeking comprehensive financial planning and investment guidance.")
                .build());

        // Specialist 5: Academic Planning - Senior
        Specialist specialist5 = specialistRepository.save(Specialist.builder()
                .user(specialistUser5)
                .category(academicPlanning)
                .level(senior)
                .status(SpecialistStatus.ACTIVE)
                .fee(new BigDecimal("90.00"))
                .information("Education consultant specializing in college admissions and academic planning. " +
                        "Former admissions committee member at a top-tier university with insider knowledge " +
                        "of what makes applications stand out. Expert in standardized test preparation strategies, " +
                        "extracurricular development, and scholarship optimization. " +
                        "Ideal for high school students and their parents navigating the college application process.")
                .build());

        // ============================================================
        // AVAILABILITY SLOTS (Weekly Recurring)
        // ============================================================

        // Specialist 1 slots (Software Engineering)
        // MONDAY 09:00-10:00, 10:00-11:00; TUESDAY 14:00-15:00; WEDNESDAY 09:00-10:00; FRIDAY 13:00-14:00
        slotRepository.save(AvailabilitySlot.builder()
                .specialist(specialist1)
                .dayOfWeek(DayOfWeek.MONDAY)
                .startTime(LocalTime.of(9, 0))
                .endTime(LocalTime.of(10, 0))
                .status(SlotStatus.AVAILABLE)
                .build());

        slotRepository.save(AvailabilitySlot.builder()
                .specialist(specialist1)
                .dayOfWeek(DayOfWeek.MONDAY)
                .startTime(LocalTime.of(10, 0))
                .endTime(LocalTime.of(11, 0))
                .status(SlotStatus.AVAILABLE)
                .build());

        slotRepository.save(AvailabilitySlot.builder()
                .specialist(specialist1)
                .dayOfWeek(DayOfWeek.TUESDAY)
                .startTime(LocalTime.of(14, 0))
                .endTime(LocalTime.of(15, 0))
                .status(SlotStatus.AVAILABLE)
                .build());

        slotRepository.save(AvailabilitySlot.builder()
                .specialist(specialist1)
                .dayOfWeek(DayOfWeek.WEDNESDAY)
                .startTime(LocalTime.of(9, 0))
                .endTime(LocalTime.of(10, 0))
                .status(SlotStatus.AVAILABLE)
                .build());

        slotRepository.save(AvailabilitySlot.builder()
                .specialist(specialist1)
                .dayOfWeek(DayOfWeek.FRIDAY)
                .startTime(LocalTime.of(13, 0))
                .endTime(LocalTime.of(14, 0))
                .status(SlotStatus.AVAILABLE)
                .build());

        // Specialist 2 slots (Data Science)
        // MONDAY 11:00-12:00; TUESDAY 09:00-10:00; WEDNESDAY 15:00-16:00; THURSDAY 14:00-15:00; FRIDAY 10:00-11:00
        slotRepository.save(AvailabilitySlot.builder()
                .specialist(specialist2)
                .dayOfWeek(DayOfWeek.MONDAY)
                .startTime(LocalTime.of(11, 0))
                .endTime(LocalTime.of(12, 0))
                .status(SlotStatus.AVAILABLE)
                .build());

        slotRepository.save(AvailabilitySlot.builder()
                .specialist(specialist2)
                .dayOfWeek(DayOfWeek.TUESDAY)
                .startTime(LocalTime.of(9, 0))
                .endTime(LocalTime.of(10, 0))
                .status(SlotStatus.AVAILABLE)
                .build());

        slotRepository.save(AvailabilitySlot.builder()
                .specialist(specialist2)
                .dayOfWeek(DayOfWeek.WEDNESDAY)
                .startTime(LocalTime.of(15, 0))
                .endTime(LocalTime.of(16, 0))
                .status(SlotStatus.AVAILABLE)
                .build());

        slotRepository.save(AvailabilitySlot.builder()
                .specialist(specialist2)
                .dayOfWeek(DayOfWeek.THURSDAY)
                .startTime(LocalTime.of(14, 0))
                .endTime(LocalTime.of(15, 0))
                .status(SlotStatus.AVAILABLE)
                .build());

        slotRepository.save(AvailabilitySlot.builder()
                .specialist(specialist2)
                .dayOfWeek(DayOfWeek.FRIDAY)
                .startTime(LocalTime.of(10, 0))
                .endTime(LocalTime.of(11, 0))
                .status(SlotStatus.AVAILABLE)
                .build());

        // Specialist 3 slots (Career Consulting) - Monday, Wednesday, Friday
        slotRepository.save(AvailabilitySlot.builder()
                .specialist(specialist3)
                .dayOfWeek(DayOfWeek.MONDAY)
                .startTime(LocalTime.of(13, 0))
                .endTime(LocalTime.of(14, 0))
                .status(SlotStatus.AVAILABLE)
                .build());

        slotRepository.save(AvailabilitySlot.builder()
                .specialist(specialist3)
                .dayOfWeek(DayOfWeek.WEDNESDAY)
                .startTime(LocalTime.of(10, 0))
                .endTime(LocalTime.of(11, 0))
                .status(SlotStatus.AVAILABLE)
                .build());

        slotRepository.save(AvailabilitySlot.builder()
                .specialist(specialist3)
                .dayOfWeek(DayOfWeek.FRIDAY)
                .startTime(LocalTime.of(15, 0))
                .endTime(LocalTime.of(16, 0))
                .status(SlotStatus.AVAILABLE)
                .build());

        // Specialist 4 slots (Finance Consulting) - Tuesday and Thursday
        slotRepository.save(AvailabilitySlot.builder()
                .specialist(specialist4)
                .dayOfWeek(DayOfWeek.TUESDAY)
                .startTime(LocalTime.of(15, 0))
                .endTime(LocalTime.of(16, 0))
                .status(SlotStatus.AVAILABLE)
                .build());

        slotRepository.save(AvailabilitySlot.builder()
                .specialist(specialist4)
                .dayOfWeek(DayOfWeek.THURSDAY)
                .startTime(LocalTime.of(10, 0))
                .endTime(LocalTime.of(11, 0))
                .status(SlotStatus.AVAILABLE)
                .build());

        slotRepository.save(AvailabilitySlot.builder()
                .specialist(specialist4)
                .dayOfWeek(DayOfWeek.FRIDAY)
                .startTime(LocalTime.of(14, 0))
                .endTime(LocalTime.of(15, 0))
                .status(SlotStatus.AVAILABLE)
                .build());

        // Specialist 5 slots (Academic Planning) - Wednesday, Thursday, Friday
        slotRepository.save(AvailabilitySlot.builder()
                .specialist(specialist5)
                .dayOfWeek(DayOfWeek.WEDNESDAY)
                .startTime(LocalTime.of(11, 0))
                .endTime(LocalTime.of(12, 0))
                .status(SlotStatus.AVAILABLE)
                .build());

        slotRepository.save(AvailabilitySlot.builder()
                .specialist(specialist5)
                .dayOfWeek(DayOfWeek.THURSDAY)
                .startTime(LocalTime.of(9, 0))
                .endTime(LocalTime.of(10, 0))
                .status(SlotStatus.AVAILABLE)
                .build());

        slotRepository.save(AvailabilitySlot.builder()
                .specialist(specialist5)
                .dayOfWeek(DayOfWeek.FRIDAY)
                .startTime(LocalTime.of(14, 0))
                .endTime(LocalTime.of(15, 0))
                .status(SlotStatus.AVAILABLE)
                .build());

        log.info("Sample data initialization completed");
    }
}

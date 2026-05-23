package com.cpt202.consultationbooking.service;

import com.cpt202.consultationbooking.dto.request.CreateBookingRequest;
import com.cpt202.consultationbooking.dto.request.RescheduleBookingRequest;
import com.cpt202.consultationbooking.dto.response.BookingResponse;
import com.cpt202.consultationbooking.entity.AvailabilitySlot;
import com.cpt202.consultationbooking.entity.Booking;
import com.cpt202.consultationbooking.entity.Customer;
import com.cpt202.consultationbooking.entity.Specialist;
import com.cpt202.consultationbooking.entity.User;
import com.cpt202.consultationbooking.enums.BookingStatus;
import com.cpt202.consultationbooking.enums.SlotStatus;
import com.cpt202.consultationbooking.enums.SpecialistStatus;
import com.cpt202.consultationbooking.enums.UserRole;
import com.cpt202.consultationbooking.enums.UserStatus;
import com.cpt202.consultationbooking.exception.BusinessException;
import com.cpt202.consultationbooking.exception.ResourceNotFoundException;
import com.cpt202.consultationbooking.repository.AvailabilitySlotRepository;
import com.cpt202.consultationbooking.repository.BookingRepository;
import com.cpt202.consultationbooking.repository.CustomerRepository;
import com.cpt202.consultationbooking.repository.SpecialistRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BookingServiceTest {

    @Mock
    private BookingRepository bookingRepository;

    @Mock
    private CustomerRepository customerRepository;

    @Mock
    private SpecialistRepository specialistRepository;

    @Mock
    private AvailabilitySlotRepository slotRepository;

    @InjectMocks
    private BookingService bookingService;

    private User testUser;
    private Customer testCustomer;
    private Specialist testSpecialist;
    private AvailabilitySlot testSlot;
    private AvailabilitySlot testSlot2;
    private AvailabilitySlot otherSpecialistSlot;
    private Booking testBooking;

    @BeforeEach
    void setUp() {
        testUser = User.builder()
                .userId(1L)
                .username("testuser")
                .password("password")
                .email("test@example.com")
                .role(UserRole.CUSTOMER)
                .status(UserStatus.ACTIVE)
                .build();

        testCustomer = Customer.builder()
                .customerId(1L)
                .user(testUser)
                .build();

        Specialist dummySpecialist = Specialist.builder()
                .specialistId(1L)
                .user(testUser)
                .category(mockCategory(1L, "Engineering"))
                .level(mockLevel(1L, "Senior"))
                .status(SpecialistStatus.ACTIVE)
                .fee(new BigDecimal("150.00"))
                .information("Senior engineer")
                .build();

        testSpecialist = dummySpecialist;

        User specialistUser = User.builder()
                .userId(2L)
                .username("specialist1")
                .password("password")
                .email("specialist@example.com")
                .role(UserRole.SPECIALIST)
                .status(UserStatus.ACTIVE)
                .build();

        testSpecialist = Specialist.builder()
                .specialistId(1L)
                .user(specialistUser)
                .category(mockCategory(1L, "Engineering"))
                .level(mockLevel(1L, "Senior"))
                .status(SpecialistStatus.ACTIVE)
                .fee(new BigDecimal("150.00"))
                .information("Senior engineer")
                .build();

        testSlot = AvailabilitySlot.builder()
                .slotId(1L)
                .specialist(testSpecialist)
                .dayOfWeek(DayOfWeek.MONDAY)
                .startTime(LocalTime.of(9, 0))
                .endTime(LocalTime.of(10, 0))
                .status(SlotStatus.AVAILABLE)
                .build();

        // Persist testSpecialist2 separately so it is not overwritten
        User specialist2User = User.builder()
                .userId(3L)
                .username("specialist2")
                .password("password")
                .email("specialist2@example.com")
                .role(UserRole.SPECIALIST)
                .status(UserStatus.ACTIVE)
                .build();

        Specialist testSpecialist2 = Specialist.builder()
                .specialistId(2L)
                .user(specialist2User)
                .category(mockCategory(1L, "Engineering"))
                .level(mockLevel(1L, "Senior"))
                .status(SpecialistStatus.ACTIVE)
                .fee(new BigDecimal("200.00"))
                .build();

        // testSlot2 belongs to the SAME specialist as testBooking (specialistId=1)
        // This allows reschedule tests between two slots of the same specialist
        testSlot2 = AvailabilitySlot.builder()
                .slotId(2L)
                .specialist(testSpecialist)  // same specialist as testBooking
                .dayOfWeek(DayOfWeek.MONDAY)
                .startTime(LocalTime.of(10, 0))
                .endTime(LocalTime.of(11, 0))
                .status(SlotStatus.AVAILABLE)
                .build();

        // A slot belonging to a DIFFERENT specialist (specialistId=2)
        // Used for wrong-specialist validation tests
        otherSpecialistSlot = AvailabilitySlot.builder()
                .slotId(99L)
                .specialist(testSpecialist2)
                .dayOfWeek(DayOfWeek.MONDAY)
                .startTime(LocalTime.of(14, 0))
                .endTime(LocalTime.of(15, 0))
                .status(SlotStatus.AVAILABLE)
                .build();

        testBooking = Booking.builder()
                .bookingId(1L)
                .customer(testCustomer)
                .specialist(testSpecialist)
                .slot(testSlot)
                .appointmentDate(LocalDate.now().plusDays(7))
                .topic("Career advice")
                .notes("Need help")
                .status(BookingStatus.PENDING)
                .price(new BigDecimal("150.00"))
                .createdAt(LocalDateTime.now())
                .build();
    }

    // -------------------------------------------------------------------------
    // Helper methods
    // -------------------------------------------------------------------------

    private static com.cpt202.consultationbooking.entity.ExpertiseCategory mockCategory(Long id, String name) {
        return com.cpt202.consultationbooking.entity.ExpertiseCategory.builder()
                .categoryId(id)
                .name(name)
                .status("ACTIVE")
                .build();
    }

    private static com.cpt202.consultationbooking.entity.Level mockLevel(Long id, String name) {
        return com.cpt202.consultationbooking.entity.Level.builder()
                .levelId(id)
                .name(name)
                .build();
    }

    private static LocalDate futureDate(DayOfWeek dayOfWeek) {
        LocalDate today = LocalDate.now();
        return today.plusDays((7 + dayOfWeek.getValue() - today.getDayOfWeek().getValue()) % 7 + 7);
    }

    private static CreateBookingRequest makeCreateRequest(Long customerId, Long specialistId, Long slotId,
                                                          DayOfWeek dayOfWeek, String topic) {
        return new CreateBookingRequest(
                customerId,
                specialistId,
                slotId,
                futureDate(dayOfWeek),
                topic,
                "Test notes"
        );
    }

    // -------------------------------------------------------------------------
    // Test 1: createBooking — happy path
    // -------------------------------------------------------------------------

    @Nested
    @DisplayName("createBooking")
    class CreateBookingTests {

        @Test
        @DisplayName("should create PENDING booking and copy specialist fee into price")
        void createBooking_happyPath_copiesFeeAndSetsPendingStatus() {
            // Arrange
            CreateBookingRequest request = makeCreateRequest(1L, 1L, 1L, DayOfWeek.MONDAY, "Career consultation");

            when(customerRepository.findById(1L)).thenReturn(Optional.of(testCustomer));
            when(specialistRepository.findById(1L)).thenReturn(Optional.of(testSpecialist));
            when(slotRepository.findById(1L)).thenReturn(Optional.of(testSlot));
            when(bookingRepository.existsBySlot_SlotIdAndAppointmentDateAndStatusIn(
                    eq(1L), any(LocalDate.class), any())).thenReturn(false);
            when(bookingRepository.save(any(Booking.class))).thenAnswer(inv -> {
                Booking b = inv.getArgument(0);
                b.setBookingId(99L);
                return b;
            });

            // Act
            BookingResponse response = bookingService.createBooking(request);

            // Assert
            assertThat(response.getStatus()).isEqualTo(BookingStatus.PENDING);
            assertThat(response.getPrice()).isEqualByComparingTo(new BigDecimal("150.00"));
            assertThat(response.getSpecialistId()).isEqualTo(1L);
            assertThat(response.getCustomerId()).isEqualTo(1L);

            ArgumentCaptor<Booking> captor = ArgumentCaptor.forClass(Booking.class);
            verify(bookingRepository).save(captor.capture());
            Booking saved = captor.getValue();
            assertThat(saved.getStatus()).isEqualTo(BookingStatus.PENDING);
            assertThat(saved.getPrice()).isEqualByComparingTo(new BigDecimal("150.00"));
        }

        @Test
        @DisplayName("should reject when slot is occupied by an active PENDING booking")
        void createBooking_slotOccupiedByPending_throwsException() {
            CreateBookingRequest request = makeCreateRequest(1L, 1L, 1L, DayOfWeek.MONDAY, "Career consultation");
            LocalDate futureMon = futureDate(DayOfWeek.MONDAY);

            when(customerRepository.findById(1L)).thenReturn(Optional.of(testCustomer));
            when(specialistRepository.findById(1L)).thenReturn(Optional.of(testSpecialist));
            when(slotRepository.findById(1L)).thenReturn(Optional.of(testSlot));
            when(bookingRepository.existsBySlot_SlotIdAndAppointmentDateAndStatusIn(
                    1L, futureMon, Arrays.asList(BookingStatus.PENDING, BookingStatus.CONFIRMED, BookingStatus.COMPLETED)))
                    .thenReturn(true);

            assertThatThrownBy(() -> bookingService.createBooking(request))
                    .isInstanceOf(BusinessException.class)
                    .hasMessageContaining("no longer available");

            verify(bookingRepository, never()).save(any());
        }

        @Test
        @DisplayName("should reject when slot is occupied by an active CONFIRMED booking")
        void createBooking_slotOccupiedByConfirmed_throwsException() {
            CreateBookingRequest request = makeCreateRequest(1L, 1L, 1L, DayOfWeek.MONDAY, "Career consultation");
            LocalDate futureMon = futureDate(DayOfWeek.MONDAY);

            when(customerRepository.findById(1L)).thenReturn(Optional.of(testCustomer));
            when(specialistRepository.findById(1L)).thenReturn(Optional.of(testSpecialist));
            when(slotRepository.findById(1L)).thenReturn(Optional.of(testSlot));
            when(bookingRepository.existsBySlot_SlotIdAndAppointmentDateAndStatusIn(
                    1L, futureMon, Arrays.asList(BookingStatus.PENDING, BookingStatus.CONFIRMED, BookingStatus.COMPLETED)))
                    .thenReturn(true);

            assertThatThrownBy(() -> bookingService.createBooking(request))
                    .isInstanceOf(BusinessException.class)
                    .hasMessageContaining("no longer available");
        }

        @Test
        @DisplayName("should reject when slot is occupied by a COMPLETED booking")
        void createBooking_slotOccupiedByCompleted_throwsException() {
            CreateBookingRequest request = makeCreateRequest(1L, 1L, 1L, DayOfWeek.MONDAY, "Career consultation");
            LocalDate futureMon = futureDate(DayOfWeek.MONDAY);

            when(customerRepository.findById(1L)).thenReturn(Optional.of(testCustomer));
            when(specialistRepository.findById(1L)).thenReturn(Optional.of(testSpecialist));
            when(slotRepository.findById(1L)).thenReturn(Optional.of(testSlot));
            when(bookingRepository.existsBySlot_SlotIdAndAppointmentDateAndStatusIn(
                    1L, futureMon, Arrays.asList(BookingStatus.PENDING, BookingStatus.CONFIRMED, BookingStatus.COMPLETED)))
                    .thenReturn(true);

            assertThatThrownBy(() -> bookingService.createBooking(request))
                    .isInstanceOf(BusinessException.class)
                    .hasMessageContaining("no longer available");
        }

        @Test
        @DisplayName("should allow booking when slot was previously CANCELLED")
        void createBooking_cancelledSlotAllowsNewBooking() {
            CreateBookingRequest request = makeCreateRequest(1L, 1L, 1L, DayOfWeek.MONDAY, "Career consultation");

            when(customerRepository.findById(1L)).thenReturn(Optional.of(testCustomer));
            when(specialistRepository.findById(1L)).thenReturn(Optional.of(testSpecialist));
            when(slotRepository.findById(1L)).thenReturn(Optional.of(testSlot));
            when(bookingRepository.existsBySlot_SlotIdAndAppointmentDateAndStatusIn(
                    eq(1L), any(LocalDate.class), any())).thenReturn(false);
            when(bookingRepository.save(any(Booking.class))).thenAnswer(inv -> {
                Booking b = inv.getArgument(0);
                b.setBookingId(99L);
                return b;
            });

            BookingResponse response = bookingService.createBooking(request);

            assertThat(response.getStatus()).isEqualTo(BookingStatus.PENDING);
            verify(bookingRepository).save(any(Booking.class));
        }

        @Test
        @DisplayName("should reject when customer does not exist")
        void createBooking_customerNotFound_throwsException() {
            CreateBookingRequest request = makeCreateRequest(999L, 1L, 1L, DayOfWeek.MONDAY, "Test");

            when(customerRepository.findById(999L)).thenReturn(Optional.empty());

            assertThatThrownBy(() -> bookingService.createBooking(request))
                    .isInstanceOf(ResourceNotFoundException.class)
                    .hasMessageContaining("Customer not found");
        }

        @Test
        @DisplayName("should reject when specialist does not exist")
        void createBooking_specialistNotFound_throwsException() {
            CreateBookingRequest request = makeCreateRequest(1L, 999L, 1L, DayOfWeek.MONDAY, "Test");

            when(customerRepository.findById(1L)).thenReturn(Optional.of(testCustomer));
            when(specialistRepository.findById(999L)).thenReturn(Optional.empty());

            assertThatThrownBy(() -> bookingService.createBooking(request))
                    .isInstanceOf(ResourceNotFoundException.class)
                    .hasMessageContaining("Specialist not found");
        }

        @Test
        @DisplayName("should reject when slot does not belong to selected specialist")
        void createBooking_slotBelongsToDifferentSpecialist_throwsException() {
            // otherSpecialistSlot belongs to specialistId=2, but request specialistId=1
            CreateBookingRequest request = new CreateBookingRequest(
                    1L, 1L, 99L,
                    futureDate(DayOfWeek.MONDAY),
                    "Test", "Notes"
            );

            when(customerRepository.findById(1L)).thenReturn(Optional.of(testCustomer));
            when(specialistRepository.findById(1L)).thenReturn(Optional.of(testSpecialist));
            // slot specialist mismatch throws BEFORE save is called, so save stub is not needed
            when(slotRepository.findById(99L)).thenReturn(Optional.of(otherSpecialistSlot));

            assertThatThrownBy(() -> bookingService.createBooking(request))
                    .isInstanceOf(BusinessException.class)
                    .hasMessageContaining("does not belong to the selected specialist");
        }

        @Test
        @DisplayName("should reject when appointment date day-of-week does not match slot")
        void createBooking_dayOfWeekMismatch_throwsException() {
            // Slot is MONDAY; request for TUESDAY triggers the day mismatch
            CreateBookingRequest request = new CreateBookingRequest(
                    1L, 1L, 1L,
                    futureDate(DayOfWeek.TUESDAY),
                    "Test", "Notes"
            );

            when(customerRepository.findById(1L)).thenReturn(Optional.of(testCustomer));
            when(specialistRepository.findById(1L)).thenReturn(Optional.of(testSpecialist));
            // day-of-week mismatch throws BEFORE existsBySlot or save are called
            when(slotRepository.findById(1L)).thenReturn(Optional.of(testSlot));

            assertThatThrownBy(() -> bookingService.createBooking(request))
                    .isInstanceOf(BusinessException.class)
                    .hasMessageContaining("must be on");
        }

        @Test
        @DisplayName("should reject when appointment date is in the past")
        void createBooking_dateInPast_throwsException() {
            CreateBookingRequest pastRequest = new CreateBookingRequest(
                    1L, 1L, 1L,
                    LocalDate.now().minusDays(1),
                    "Test", "Notes"
            );

            when(customerRepository.findById(1L)).thenReturn(Optional.of(testCustomer));
            when(specialistRepository.findById(1L)).thenReturn(Optional.of(testSpecialist));
            when(slotRepository.findById(1L)).thenReturn(Optional.of(testSlot));

            assertThatThrownBy(() -> bookingService.createBooking(pastRequest))
                    .isInstanceOf(BusinessException.class)
                    .hasMessageContaining("past");
        }
    }

    // -------------------------------------------------------------------------
    // Test 2: rescheduleBooking — releases old slot, books new slot, resets to PENDING
    // -------------------------------------------------------------------------

    @Nested
    @DisplayName("rescheduleBooking")
    class RescheduleBookingTests {

        @Test
        @DisplayName("should update slot and reset status to PENDING when rescheduling from PENDING")
        void rescheduleBooking_fromPending_releasesOldSlotUpdatesNewSlotAndResetsToPending() {
            testBooking.setStatus(BookingStatus.PENDING);
            RescheduleBookingRequest request = new RescheduleBookingRequest(2L, futureDate(DayOfWeek.MONDAY));

            when(bookingRepository.findById(1L)).thenReturn(Optional.of(testBooking));
            when(slotRepository.findById(2L)).thenReturn(Optional.of(testSlot2));
            when(bookingRepository.existsBySlot_SlotIdAndAppointmentDateAndStatusIn(
                    eq(2L), any(LocalDate.class), any())).thenReturn(false);
            when(bookingRepository.save(any(Booking.class))).thenReturn(testBooking);

            BookingResponse response = bookingService.rescheduleBooking(1L, request);

            ArgumentCaptor<Booking> captor = ArgumentCaptor.forClass(Booking.class);
            verify(bookingRepository).save(captor.capture());
            Booking saved = captor.getValue();

            assertThat(saved.getSlot()).isEqualTo(testSlot2);
            assertThat(saved.getStatus()).isEqualTo(BookingStatus.PENDING);
        }

        @Test
        @DisplayName("should reset CONFIRMED booking to PENDING when rescheduled")
        void rescheduleBooking_fromConfirmed_resetsToPending() {
            testBooking.setStatus(BookingStatus.CONFIRMED);
            RescheduleBookingRequest request = new RescheduleBookingRequest(2L, futureDate(DayOfWeek.MONDAY));

            when(bookingRepository.findById(1L)).thenReturn(Optional.of(testBooking));
            when(slotRepository.findById(2L)).thenReturn(Optional.of(testSlot2));
            when(bookingRepository.existsBySlot_SlotIdAndAppointmentDateAndStatusIn(
                    eq(2L), any(LocalDate.class), any())).thenReturn(false);
            when(bookingRepository.save(any(Booking.class))).thenReturn(testBooking);

            BookingResponse response = bookingService.rescheduleBooking(1L, request);

            ArgumentCaptor<Booking> captor = ArgumentCaptor.forClass(Booking.class);
            verify(bookingRepository).save(captor.capture());
            assertThat(captor.getValue().getStatus()).isEqualTo(BookingStatus.PENDING);
        }

        @Test
        @DisplayName("should reject when new slot belongs to a different specialist")
        void rescheduleBooking_differentSpecialist_throwsException() {
            // otherSpecialistSlot has specialistId=99, but testBooking has specialistId=1
            RescheduleBookingRequest request = new RescheduleBookingRequest(99L, futureDate(DayOfWeek.MONDAY));

            when(bookingRepository.findById(1L)).thenReturn(Optional.of(testBooking));
            when(slotRepository.findById(99L)).thenReturn(Optional.of(otherSpecialistSlot));

            assertThatThrownBy(() -> bookingService.rescheduleBooking(1L, request))
                    .isInstanceOf(BusinessException.class)
                    .hasMessageContaining("does not belong to the same specialist");
        }

        @Test
        @DisplayName("should reject COMPLETED booking from being rescheduled")
        void rescheduleBooking_completed_throwsException() {
            testBooking.setStatus(BookingStatus.COMPLETED);
            RescheduleBookingRequest request = new RescheduleBookingRequest(2L, futureDate(DayOfWeek.MONDAY));

            when(bookingRepository.findById(1L)).thenReturn(Optional.of(testBooking));

            assertThatThrownBy(() -> bookingService.rescheduleBooking(1L, request))
                    .isInstanceOf(BusinessException.class)
                    .hasMessageContaining("COMPLETED booking cannot be rescheduled");
        }

        @Test
        @DisplayName("should reject CANCELLED booking from being rescheduled")
        void rescheduleBooking_cancelled_throwsException() {
            testBooking.setStatus(BookingStatus.CANCELLED);
            RescheduleBookingRequest request = new RescheduleBookingRequest(2L, futureDate(DayOfWeek.MONDAY));

            when(bookingRepository.findById(1L)).thenReturn(Optional.of(testBooking));

            assertThatThrownBy(() -> bookingService.rescheduleBooking(1L, request))
                    .isInstanceOf(BusinessException.class)
                    .hasMessageContaining("CANCELLED booking cannot be rescheduled");
        }

        @Test
        @DisplayName("should reject when new slot is already occupied by a blocking booking")
        void rescheduleBooking_newSlotOccupied_throwsException() {
            testBooking.setStatus(BookingStatus.PENDING);
            RescheduleBookingRequest request = new RescheduleBookingRequest(2L, futureDate(DayOfWeek.MONDAY));

            when(bookingRepository.findById(1L)).thenReturn(Optional.of(testBooking));
            when(slotRepository.findById(2L)).thenReturn(Optional.of(testSlot2));
            when(bookingRepository.existsBySlot_SlotIdAndAppointmentDateAndStatusIn(
                    eq(2L), any(LocalDate.class), any())).thenReturn(true);

            assertThatThrownBy(() -> bookingService.rescheduleBooking(1L, request))
                    .isInstanceOf(BusinessException.class)
                    .hasMessageContaining("no longer available");
        }
    }

    // -------------------------------------------------------------------------
    // Test 3: completeBooking — COMPLETED status
    // -------------------------------------------------------------------------

    @Nested
    @DisplayName("completeBooking")
    class CompleteBookingTests {

        @Test
        @DisplayName("should change status to COMPLETED for CONFIRMED booking")
        void completeBooking_confirmedBooking_changesStatusToCompleted() {
            testBooking.setStatus(BookingStatus.CONFIRMED);

            when(bookingRepository.findById(1L)).thenReturn(Optional.of(testBooking));
            when(bookingRepository.save(any(Booking.class))).thenReturn(testBooking);

            BookingResponse response = bookingService.completeBooking(1L);

            ArgumentCaptor<Booking> captor = ArgumentCaptor.forClass(Booking.class);
            verify(bookingRepository).save(captor.capture());
            assertThat(captor.getValue().getStatus()).isEqualTo(BookingStatus.COMPLETED);
        }

        @Test
        @DisplayName("should reject when booking is PENDING")
        void completeBooking_pending_throwsException() {
            testBooking.setStatus(BookingStatus.PENDING);

            when(bookingRepository.findById(1L)).thenReturn(Optional.of(testBooking));

            assertThatThrownBy(() -> bookingService.completeBooking(1L))
                    .isInstanceOf(BusinessException.class)
                    .hasMessageContaining("Only CONFIRMED booking can be completed");
        }

        @Test
        @DisplayName("should reject when booking is already COMPLETED")
        void completeBooking_alreadyCompleted_throwsException() {
            testBooking.setStatus(BookingStatus.COMPLETED);

            when(bookingRepository.findById(1L)).thenReturn(Optional.of(testBooking));

            assertThatThrownBy(() -> bookingService.completeBooking(1L))
                    .isInstanceOf(BusinessException.class)
                    .hasMessageContaining("Only CONFIRMED booking can be completed");
        }

        @Test
        @DisplayName("should reject when booking is CANCELLED")
        void completeBooking_cancelled_throwsException() {
            testBooking.setStatus(BookingStatus.CANCELLED);

            when(bookingRepository.findById(1L)).thenReturn(Optional.of(testBooking));

            assertThatThrownBy(() -> bookingService.completeBooking(1L))
                    .isInstanceOf(BusinessException.class)
                    .hasMessageContaining("Only CONFIRMED booking can be completed");
        }

        @Test
        @DisplayName("should reject when booking does not exist")
        void completeBooking_notFound_throwsException() {
            when(bookingRepository.findById(999L)).thenReturn(Optional.empty());

            assertThatThrownBy(() -> bookingService.completeBooking(999L))
                    .isInstanceOf(ResourceNotFoundException.class)
                    .hasMessageContaining("Booking not found");
        }
    }

    // -------------------------------------------------------------------------
    // Test 4: confirmBooking and cancelBooking — state transitions
    // -------------------------------------------------------------------------

    @Nested
    @DisplayName("confirmBooking")
    class ConfirmBookingTests {

        @Test
        @DisplayName("should change status from PENDING to CONFIRMED")
        void confirmBooking_pendingChangesToConfirmed() {
            testBooking.setStatus(BookingStatus.PENDING);

            when(bookingRepository.findById(1L)).thenReturn(Optional.of(testBooking));
            when(bookingRepository.save(any(Booking.class))).thenReturn(testBooking);

            BookingResponse response = bookingService.confirmBooking(1L);

            ArgumentCaptor<Booking> captor = ArgumentCaptor.forClass(Booking.class);
            verify(bookingRepository).save(captor.capture());
            assertThat(captor.getValue().getStatus()).isEqualTo(BookingStatus.CONFIRMED);
        }

        @Test
        @DisplayName("should reject when booking is already CONFIRMED")
        void confirmBooking_alreadyConfirmed_throwsException() {
            testBooking.setStatus(BookingStatus.CONFIRMED);

            when(bookingRepository.findById(1L)).thenReturn(Optional.of(testBooking));

            assertThatThrownBy(() -> bookingService.confirmBooking(1L))
                    .isInstanceOf(BusinessException.class)
                    .hasMessageContaining("Only PENDING booking can be confirmed");
        }

        @Test
        @DisplayName("should reject when booking does not exist")
        void confirmBooking_notFound_throwsException() {
            when(bookingRepository.findById(999L)).thenReturn(Optional.empty());

            assertThatThrownBy(() -> bookingService.confirmBooking(999L))
                    .isInstanceOf(ResourceNotFoundException.class)
                    .hasMessageContaining("Booking not found");
        }
    }

    @Nested
    @DisplayName("cancelBooking")
    class CancelBookingTests {

        @Test
        @DisplayName("should change PENDING booking to CANCELLED")
        void cancelBooking_fromPending_succeeds() {
            testBooking.setStatus(BookingStatus.PENDING);

            when(bookingRepository.findById(1L)).thenReturn(Optional.of(testBooking));
            when(bookingRepository.save(any(Booking.class))).thenReturn(testBooking);

            bookingService.cancelBooking(1L);

            ArgumentCaptor<Booking> captor = ArgumentCaptor.forClass(Booking.class);
            verify(bookingRepository).save(captor.capture());
            assertThat(captor.getValue().getStatus()).isEqualTo(BookingStatus.CANCELLED);
        }

        @Test
        @DisplayName("should change CONFIRMED booking to CANCELLED")
        void cancelBooking_fromConfirmed_succeeds() {
            testBooking.setStatus(BookingStatus.CONFIRMED);

            when(bookingRepository.findById(1L)).thenReturn(Optional.of(testBooking));
            when(bookingRepository.save(any(Booking.class))).thenReturn(testBooking);

            bookingService.cancelBooking(1L);

            ArgumentCaptor<Booking> captor = ArgumentCaptor.forClass(Booking.class);
            verify(bookingRepository).save(captor.capture());
            assertThat(captor.getValue().getStatus()).isEqualTo(BookingStatus.CANCELLED);
        }

        @Test
        @DisplayName("should reject when booking is already CANCELLED")
        void cancelBooking_alreadyCancelled_throwsException() {
            testBooking.setStatus(BookingStatus.CANCELLED);

            when(bookingRepository.findById(1L)).thenReturn(Optional.of(testBooking));

            assertThatThrownBy(() -> bookingService.cancelBooking(1L))
                    .isInstanceOf(BusinessException.class)
                    .hasMessageContaining("already been cancelled");
        }

        @Test
        @DisplayName("should reject when booking is COMPLETED")
        void cancelBooking_completed_throwsException() {
            testBooking.setStatus(BookingStatus.COMPLETED);

            when(bookingRepository.findById(1L)).thenReturn(Optional.of(testBooking));

            assertThatThrownBy(() -> bookingService.cancelBooking(1L))
                    .isInstanceOf(BusinessException.class)
                    .hasMessageContaining("COMPLETED booking cannot be cancelled");
        }
    }

    // -------------------------------------------------------------------------
    // Test 5: getAllBookings and getBookingsByCustomer
    // -------------------------------------------------------------------------

    @Nested
    @DisplayName("getAllBookings and getBookingsByCustomer")
    class QueryMethodsTests {

        @Test
        @DisplayName("getAllBookings should return all bookings as responses")
        void getAllBookings_returnsAllBookings() {
            when(bookingRepository.findAll()).thenReturn(List.of(testBooking));

            List<BookingResponse> result = bookingService.getAllBookings();

            assertThat(result).hasSize(1);
            assertThat(result.get(0).getBookingId()).isEqualTo(1L);
        }

        @Test
        @DisplayName("getAllBookings should return empty list when no bookings exist")
        void getAllBookings_emptyDatabase_returnsEmptyList() {
            when(bookingRepository.findAll()).thenReturn(Collections.emptyList());

            List<BookingResponse> result = bookingService.getAllBookings();

            assertThat(result).isEmpty();
        }

        @Test
        @DisplayName("getBookingsByCustomer should return bookings for valid customer")
        void getBookingsByCustomer_validCustomer_returnsBookings() {
            when(customerRepository.existsById(1L)).thenReturn(true);
            when(bookingRepository.findByCustomer_CustomerId(1L)).thenReturn(List.of(testBooking));

            List<BookingResponse> result = bookingService.getBookingsByCustomer(1L);

            assertThat(result).hasSize(1);
            assertThat(result.get(0).getCustomerId()).isEqualTo(1L);
        }

        @Test
        @DisplayName("getBookingsByCustomer should reject when customer does not exist")
        void getBookingsByCustomer_invalidCustomer_throwsException() {
            when(customerRepository.existsById(999L)).thenReturn(false);

            assertThatThrownBy(() -> bookingService.getBookingsByCustomer(999L))
                    .isInstanceOf(ResourceNotFoundException.class)
                    .hasMessageContaining("Customer not found");
        }
    }
}

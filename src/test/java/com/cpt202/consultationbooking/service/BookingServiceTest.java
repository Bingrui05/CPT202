package com.cpt202.consultationbooking.service;

import com.cpt202.consultationbooking.dto.request.CreateBookingRequest;
import com.cpt202.consultationbooking.dto.response.BookingResponse;
import com.cpt202.consultationbooking.entity.AvailabilitySlot;
import com.cpt202.consultationbooking.entity.Booking;
import com.cpt202.consultationbooking.entity.Customer;
import com.cpt202.consultationbooking.entity.Specialist;
import com.cpt202.consultationbooking.entity.User;
import com.cpt202.consultationbooking.enums.BookingStatus;
import com.cpt202.consultationbooking.exception.BusinessException;
import com.cpt202.consultationbooking.repository.AvailabilitySlotRepository;
import com.cpt202.consultationbooking.repository.BookingRepository;
import com.cpt202.consultationbooking.repository.CustomerRepository;
import com.cpt202.consultationbooking.repository.SpecialistRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

class BookingServiceTest {

    private BookingRepository bookingRepository;
    private CustomerRepository customerRepository;
    private SpecialistRepository specialistRepository;
    private AvailabilitySlotRepository slotRepository;
    private BookingService bookingService;

    @BeforeEach
    void setUp() {
        bookingRepository = mock(BookingRepository.class);
        customerRepository = mock(CustomerRepository.class);
        specialistRepository = mock(SpecialistRepository.class);
        slotRepository = mock(AvailabilitySlotRepository.class);

        bookingService = new BookingService(
                bookingRepository,
                customerRepository,
                specialistRepository,
                slotRepository
        );
    }

    @Test
    void createBooking_shouldCreatePendingBookingAndRecordPrice_whenInputIsValid() {
        LocalDate appointmentDate = nextDateFor(DayOfWeek.MONDAY);

        Customer customer = createCustomer(1L, "customer1");
        Specialist specialist = createSpecialist(1L, "specialist1", new BigDecimal("150.00"));
        AvailabilitySlot slot = createSlot(1L, specialist, DayOfWeek.MONDAY);

        CreateBookingRequest request = new CreateBookingRequest(
                1L,
                1L,
                1L,
                appointmentDate,
                "Career consultation",
                "Need advice"
        );

        when(customerRepository.findById(1L)).thenReturn(Optional.of(customer));
        when(specialistRepository.findById(1L)).thenReturn(Optional.of(specialist));
        when(slotRepository.findById(1L)).thenReturn(Optional.of(slot));
        when(bookingRepository.existsBySlot_SlotIdAndAppointmentDateAndStatusIn(
                eq(1L),
                eq(appointmentDate),
                anyList()
        )).thenReturn(false);

        when(bookingRepository.save(any(Booking.class))).thenAnswer(invocation -> {
            Booking booking = invocation.getArgument(0);
            setField(booking, "bookingId", 1L);
            return booking;
        });

        BookingResponse response = bookingService.createBooking(request);

        assertEquals(1L, response.getBookingId());
        assertEquals(BookingStatus.PENDING, response.getStatus());
        assertEquals(new BigDecimal("150.00"), response.getPrice());
        assertEquals(appointmentDate, response.getAppointmentDate());

        verify(bookingRepository).save(any(Booking.class));
    }

    @Test
    void createBooking_shouldRejectDuplicateBooking_whenSlotAlreadyHasBlockingBooking() {
        LocalDate appointmentDate = nextDateFor(DayOfWeek.MONDAY);

        Customer customer = createCustomer(1L, "customer1");
        Specialist specialist = createSpecialist(1L, "specialist1", new BigDecimal("150.00"));
        AvailabilitySlot slot = createSlot(1L, specialist, DayOfWeek.MONDAY);

        CreateBookingRequest request = new CreateBookingRequest(
                1L,
                1L,
                1L,
                appointmentDate,
                "Career consultation",
                "Duplicate attempt"
        );

        when(customerRepository.findById(1L)).thenReturn(Optional.of(customer));
        when(specialistRepository.findById(1L)).thenReturn(Optional.of(specialist));
        when(slotRepository.findById(1L)).thenReturn(Optional.of(slot));
        when(bookingRepository.existsBySlot_SlotIdAndAppointmentDateAndStatusIn(
                eq(1L),
                eq(appointmentDate),
                anyList()
        )).thenReturn(true);

        BusinessException exception = assertThrows(
                BusinessException.class,
                () -> bookingService.createBooking(request)
        );

        assertEquals("This appointment time is no longer available.", exception.getMessage());
        verify(bookingRepository, never()).save(any(Booking.class));
    }

    @Test
    void confirmBooking_shouldRejectInvalidStatusTransition_whenBookingIsNotPending() {
        Booking booking = Booking.builder()
                .status(BookingStatus.CONFIRMED)
                .build();

        setField(booking, "bookingId", 1L);

        when(bookingRepository.findById(1L)).thenReturn(Optional.of(booking));

        BusinessException exception = assertThrows(
                BusinessException.class,
                () -> bookingService.confirmBooking(1L)
        );

        assertEquals("Only PENDING booking can be confirmed", exception.getMessage());
        verify(bookingRepository, never()).save(any(Booking.class));
    }

    private Customer createCustomer(Long customerId, String username) {
        Customer customer = new Customer();
        setField(customer, "customerId", customerId);
        setField(customer, "user", createUser(username));
        return customer;
    }

    private Specialist createSpecialist(Long specialistId, String username, BigDecimal fee) {
        Specialist specialist = new Specialist();
        setField(specialist, "specialistId", specialistId);
        setField(specialist, "user", createUser(username));
        setField(specialist, "fee", fee);
        return specialist;
    }

    private AvailabilitySlot createSlot(Long slotId, Specialist specialist, DayOfWeek dayOfWeek) {
        AvailabilitySlot slot = new AvailabilitySlot();
        setField(slot, "slotId", slotId);
        setField(slot, "specialist", specialist);
        setField(slot, "dayOfWeek", dayOfWeek);
        setField(slot, "startTime", LocalTime.of(9, 0));
        setField(slot, "endTime", LocalTime.of(10, 0));
        return slot;
    }

    private User createUser(String username) {
        User user = new User();
        setField(user, "username", username);
        return user;
    }

    private LocalDate nextDateFor(DayOfWeek dayOfWeek) {
        LocalDate date = LocalDate.now().plusDays(1);
        while (date.getDayOfWeek() != dayOfWeek) {
            date = date.plusDays(1);
        }
        return date;
    }

    private void setField(Object target, String fieldName, Object value) {
        Class<?> current = target.getClass();

        while (current != null) {
            try {
                Field field = current.getDeclaredField(fieldName);
                field.setAccessible(true);
                field.set(target, value);
                return;
            } catch (NoSuchFieldException e) {
                current = current.getSuperclass();
            } catch (IllegalAccessException e) {
                throw new RuntimeException("Cannot set field: " + fieldName, e);
            }
        }

        throw new RuntimeException("Field not found: " + fieldName);
    }
}
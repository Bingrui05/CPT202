package com.cpt202.consultationbooking.service;

import com.cpt202.consultationbooking.dto.request.CreateSpecialistUpdateRequest;
import com.cpt202.consultationbooking.entity.Specialist;
import com.cpt202.consultationbooking.entity.SpecialistUpdateRequest;
import com.cpt202.consultationbooking.entity.User;
import com.cpt202.consultationbooking.enums.RequestAction;
import com.cpt202.consultationbooking.enums.RequestStatus;
import com.cpt202.consultationbooking.enums.RequestType;
import com.cpt202.consultationbooking.exception.BusinessException;
import com.cpt202.consultationbooking.repository.AvailabilitySlotRepository;
import com.cpt202.consultationbooking.repository.BookingRepository;
import com.cpt202.consultationbooking.repository.ExpertiseCategoryRepository;
import com.cpt202.consultationbooking.repository.SpecialistRepository;
import com.cpt202.consultationbooking.repository.SpecialistUpdateRequestRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class SpecialistUpdateRequestServiceTest {

    private SpecialistUpdateRequestRepository requestRepository;
    private SpecialistRepository specialistRepository;
    private AvailabilitySlotRepository slotRepository;
    private ExpertiseCategoryRepository categoryRepository;
    private BookingRepository bookingRepository;
    private SpecialistUpdateRequestService service;

    @BeforeEach
    void setUp() {
        requestRepository = mock(SpecialistUpdateRequestRepository.class);
        specialistRepository = mock(SpecialistRepository.class);
        slotRepository = mock(AvailabilitySlotRepository.class);
        categoryRepository = mock(ExpertiseCategoryRepository.class);
        bookingRepository = mock(BookingRepository.class);

        service = new SpecialistUpdateRequestService(
                requestRepository,
                specialistRepository,
                slotRepository,
                categoryRepository,
                bookingRepository,
                new ObjectMapper()
        );
    }

    @Test
    void createRequest_shouldSavePendingRequest_whenSpecialistSubmitsValidProfileRequest() {
        Specialist specialist = createSpecialist(1L, "specialist1");

        CreateSpecialistUpdateRequest request = new CreateSpecialistUpdateRequest(
                1L,
                RequestType.PROFILE,
                RequestAction.UPDATE,
                null,
                "{\"fee\":\"150.00\"}",
                "{\"fee\":\"200.00\"}",
                "Request to update consultation fee"
        );

        when(specialistRepository.findById(1L)).thenReturn(Optional.of(specialist));
        when(requestRepository.save(any(SpecialistUpdateRequest.class))).thenAnswer(invocation -> {
            SpecialistUpdateRequest saved = invocation.getArgument(0);
            saved.setRequestId(1L);
            return saved;
        });

        service.createRequest(request);

        verify(requestRepository).save(argThat(saved ->
                saved.getSpecialist().getSpecialistId().equals(1L)
                        && saved.getRequestType() == RequestType.PROFILE
                        && saved.getActionType() == RequestAction.UPDATE
                        && saved.getStatus() == RequestStatus.PENDING
        ));
    }

    @Test
    void approveRequest_shouldApprovePendingRequestAndApplyProfileChange() {
        Specialist specialist = createSpecialist(1L, "specialist1");

        SpecialistUpdateRequest request = SpecialistUpdateRequest.builder()
                .requestId(1L)
                .specialist(specialist)
                .requestType(RequestType.PROFILE)
                .actionType(RequestAction.UPDATE)
                .currentValue("{\"fee\":\"150.00\"}")
                .requestedValue("{\"fee\":\"200.00\",\"information\":\"Updated profile\"}")
                .reason("Update profile information")
                .status(RequestStatus.PENDING)
                .createdAt(LocalDateTime.now())
                .build();

        when(requestRepository.findById(1L)).thenReturn(Optional.of(request));
        when(requestRepository.save(any(SpecialistUpdateRequest.class))).thenAnswer(invocation -> invocation.getArgument(0));

        service.approveRequest(1L, 99L);

        assertEquals(RequestStatus.APPROVED, request.getStatus());
        assertEquals(99L, request.getReviewedBy());
        assertNotNull(request.getReviewedAt());
        assertEquals(new BigDecimal("200.00"), specialist.getFee());
        assertEquals("Updated profile", specialist.getInformation());

        verify(specialistRepository).save(specialist);
        verify(requestRepository).save(request);
    }

    @Test
    void rejectRequest_shouldRejectPendingRequestAndStoreManagerComment() {
        Specialist specialist = createSpecialist(1L, "specialist1");

        SpecialistUpdateRequest request = SpecialistUpdateRequest.builder()
                .requestId(1L)
                .specialist(specialist)
                .requestType(RequestType.PROFILE)
                .actionType(RequestAction.UPDATE)
                .currentValue("{\"fee\":\"150.00\"}")
                .requestedValue("{\"fee\":\"200.00\"}")
                .reason("Update fee")
                .status(RequestStatus.PENDING)
                .createdAt(LocalDateTime.now())
                .build();

        when(requestRepository.findById(1L)).thenReturn(Optional.of(request));
        when(requestRepository.save(any(SpecialistUpdateRequest.class))).thenAnswer(invocation -> invocation.getArgument(0));

        service.rejectRequest(1L, "Rejected because evidence is insufficient", 99L);

        assertEquals(RequestStatus.REJECTED, request.getStatus());
        assertEquals("Rejected because evidence is insufficient", request.getManagerComment());
        assertEquals(99L, request.getReviewedBy());
        assertNotNull(request.getReviewedAt());

        verify(specialistRepository, never()).save(any(Specialist.class));
        verify(requestRepository).save(request);
    }

    @Test
    void approveRequest_shouldRejectNonPendingRequest() {
        Specialist specialist = createSpecialist(1L, "specialist1");

        SpecialistUpdateRequest request = SpecialistUpdateRequest.builder()
                .requestId(1L)
                .specialist(specialist)
                .requestType(RequestType.PROFILE)
                .actionType(RequestAction.UPDATE)
                .requestedValue("{\"fee\":\"200.00\"}")
                .status(RequestStatus.APPROVED)
                .createdAt(LocalDateTime.now())
                .build();

        when(requestRepository.findById(1L)).thenReturn(Optional.of(request));

        BusinessException exception = assertThrows(
                BusinessException.class,
                () -> service.approveRequest(1L, 99L)
        );

        assertEquals("Only PENDING requests can be approved", exception.getMessage());
        verify(requestRepository, never()).save(any(SpecialistUpdateRequest.class));
    }

    private Specialist createSpecialist(Long specialistId, String username) {
        Specialist specialist = new Specialist();
        setField(specialist, "specialistId", specialistId);
        setField(specialist, "user", createUser(username));
        specialist.setFee(new BigDecimal("150.00"));
        specialist.setInformation("Original profile");
        return specialist;
    }

    private User createUser(String username) {
        User user = new User();
        setField(user, "username", username);
        return user;
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
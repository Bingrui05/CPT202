package com.cpt202.consultationbooking.dto.request;

import com.cpt202.consultationbooking.enums.BookingStatus;
import jakarta.validation.constraints.NotNull;

public class UpdateBookingStatusRequest {

    @NotNull(message = "Status is required")
    private BookingStatus status;

    public UpdateBookingStatusRequest() {}

    public UpdateBookingStatusRequest(BookingStatus status) {
        this.status = status;
    }

    public BookingStatus getStatus() { return status; }
    public void setStatus(BookingStatus status) { this.status = status; }
}

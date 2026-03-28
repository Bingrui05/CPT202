package cpt2021.test.dto;

public class BookingSummary {
    private Long bookingId;
    private String specialistName;
    private String date;
    private String status;

    public BookingSummary(Long bookingId, String specialistName, String date, String status) {
        this.bookingId = bookingId;
        this.specialistName = specialistName;
        this.date = date;
        this.status = status;
    }

    public Long getBookingId() {
        return bookingId;
    }

    public String getSpecialistName() {
        return specialistName;
    }

    public String getDate() {
        return date;
    }

    public String getStatus() {
        return status;
    }
}
package cpt2021.test.dto;

public class ScheduleItem {
    private Long bookingId;
    private String customerName;
    private String date;
    private String status;

    public ScheduleItem(Long bookingId, String customerName, String date, String status) {
        this.bookingId = bookingId;
        this.customerName = customerName;
        this.date = date;
        this.status = status;
    }

    public Long getBookingId() {
        return bookingId;
    }

    public String getCustomerName() {
        return customerName;
    }

    public String getDate() {
        return date;
    }

    public String getStatus() {
        return status;
    }
}
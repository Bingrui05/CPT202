#!/bin/bash
# =============================================================================
# book_once.sh - Send a single booking request to test concurrency
# =============================================================================
#
# USAGE:
#   ./book_once.sh
#
# PREREQUISITES:
#   1. Start Spring Boot application:
#      cd CPT202 && mvn spring-boot:run
#
#   2. Ensure you have a fresh AVAILABLE slot:
#      - Check existing slots: GET http://localhost:8080/api/slots/specialist/{specialistId}
#      - Or create a new slot via Manager dashboard
#
#   3. Set the correct SLOT_ID and APPOINTMENT_DATE below
#
# OUTPUT:
#   - Prints HTTP status code
#   - Prints response body (JSON)
# =============================================================================

# -----------------------------------------------------------------------------
# CONFIGURATION - Edit these values before running
# -----------------------------------------------------------------------------
BASE_URL="http://localhost:8080/api"
CUSTOMER_ID=1
SPECIALIST_ID=1

# CHANGE THIS to an actual available slot ID
SLOT_ID=22

# CHANGE THIS to a future date (format: YYYY-MM-DD)
# Make sure the date matches the slot's day of week
APPOINTMENT_DATE="2026-05-25"

TOPIC="concurrency_test"
NOTES="concurrency test request"

# -----------------------------------------------------------------------------
# Send booking request
# -----------------------------------------------------------------------------
echo "Sending booking request..."
echo "  Customer ID: $CUSTOMER_ID"
echo "  Specialist ID: $SPECIALIST_ID"
echo "  Slot ID: $SLOT_ID"
echo "  Appointment Date: $APPOINTMENT_DATE"
echo ""

# Build JSON payload
JSON_PAYLOAD=$(cat <<EOF
{
    "customerId": $CUSTOMER_ID,
    "specialistId": $SPECIALIST_ID,
    "slotId": $SLOT_ID,
    "appointmentDate": "$APPOINTMENT_DATE",
    "topic": "$TOPIC",
    "notes": "$NOTES"
}
EOF
)

# Send request and capture response
RESPONSE=$(curl -s -w "\nHTTP_STATUS:%{http_code}" \
    -X POST "$BASE_URL/bookings" \
    -H "Content-Type: application/json" \
    -d "$JSON_PAYLOAD")

# Extract status code
HTTP_STATUS=$(echo "$RESPONSE" | grep "HTTP_STATUS:" | cut -d: -f2)
BODY=$(echo "$RESPONSE" | sed '/HTTP_STATUS:/d')

# Print results
echo "=============================================="
echo "HTTP Status: $HTTP_STATUS"
echo "----------------------------------------------"
echo "Response Body:"
echo "$BODY" | jq '.' 2>/dev/null || echo "$BODY"
echo "=============================================="

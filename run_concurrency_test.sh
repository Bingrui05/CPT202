#!/bin/bash
# =============================================================================
# run_concurrency_test.sh - Run concurrent booking test
# =============================================================================
#
# PURPOSE:
#   Simulate 20 concurrent booking requests for the same slot.
#   With pessimistic locking, only ONE request should succeed.
#
# USAGE:
#   ./run_concurrency_test.sh
#
# STEPS:
#   1. Start Spring Boot: cd CPT202 && mvn spring-boot:run
#   2. Edit book_once.sh with correct SLOT_ID and APPOINTMENT_DATE
#   3. Run this script: ./run_concurrency_test.sh
#
# EXPECTED RESULT:
#   - Exactly 1 request should succeed (HTTP 200, success: true)
#   - 19 requests should fail with "no longer available" error
#
# =============================================================================

SCRIPT_DIR="$(cd "$(dirname "$0")" && pwd)"
BOOK_SCRIPT="$SCRIPT_DIR/book_once.sh"
OUTPUT_DIR="$SCRIPT_DIR/concurrency_test_results"
NUM_REQUESTS=20

# Colors for output
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
NC='\033[0m' # No Color

echo "================================================"
echo "  CONCURRENCY BOOKING TEST"
echo "================================================"
echo ""
echo "Configuration:"
echo "  Script: $BOOK_SCRIPT"
echo "  Number of concurrent requests: $NUM_REQUESTS"
echo "  Output directory: $OUTPUT_DIR"
echo ""

# Check if book_once.sh exists
if [ ! -f "$BOOK_SCRIPT" ]; then
    echo -e "${RED}ERROR: book_once.sh not found at $BOOK_SCRIPT${NC}"
    exit 1
fi

# Create output directory
rm -rf "$OUTPUT_DIR"
mkdir -p "$OUTPUT_DIR"

echo "Starting $NUM_REQUESTS concurrent booking requests..."
echo "----------------------------------------------"

# Array to store PIDs
PIDS=()

# Launch all requests concurrently
for i in $(seq 1 $NUM_REQUESTS); do
    OUTPUT_FILE="$OUTPUT_DIR/out_${i}.txt"
    (
        # Run the booking script and save output
        bash "$BOOK_SCRIPT" > "$OUTPUT_FILE" 2>&1
    ) &
    PIDS+=($!)
    echo "Launched request #$i (PID: ${PIDS[-1]})"
done

echo ""
echo "Waiting for all requests to complete..."
echo "----------------------------------------------"

# Wait for all background processes to finish
FAILED=0
for pid in "${PIDS[@]}"; do
    if ! wait $pid; then
        ((FAILED++))
    fi
done

echo ""
echo "================================================"
echo "  TEST COMPLETE - ANALYZING RESULTS"
echo "================================================"
echo ""

# Analyze results
SUCCESS_COUNT=0
FAIL_COUNT=0
ALREADY_BOOKED_COUNT=0
OTHER_ERROR_COUNT=0

for i in $(seq 1 $NUM_REQUESTS); do
    OUTPUT_FILE="$OUTPUT_DIR/out_${i}.txt"

    if [ -f "$OUTPUT_FILE" ]; then
        # Check for success indicators
        if grep -qi '"success":true' "$OUTPUT_FILE" || grep -qi 'success.*true' "$OUTPUT_FILE"; then
            ((SUCCESS_COUNT++))
            STATUS="${GREEN}SUCCESS${NC}"
        # Check for "no longer available" error
        elif grep -qi 'no longer available' "$OUTPUT_FILE"; then
            ((ALREADY_BOOKED_COUNT++))
            STATUS="${YELLOW}UNAVAILABLE${NC}"
        # Check for other errors
        else
            if grep -qi '"success":false' "$OUTPUT_FILE" || grep -qi 'error' "$OUTPUT_FILE"; then
                ((OTHER_ERROR_COUNT++))
            else
                ((OTHER_ERROR_COUNT++))
            fi
            STATUS="${RED}FAILED${NC}"
        fi

        # Get HTTP status
        HTTP_CODE=$(grep "HTTP Status:" "$OUTPUT_FILE" | awk '{print $3}' | tr -d '\r')

        echo "Request #$i: $STATUS (HTTP: $HTTP_CODE)"
    else
        echo -e "Request #$i: ${RED}NO OUTPUT${NC}"
    fi
done

echo ""
echo "================================================"
echo "  SUMMARY"
echo "================================================"
echo ""
echo -e "  ${GREEN}Successful bookings:${NC}  $SUCCESS_COUNT"
echo -e "  ${YELLOW}Slot unavailable:${NC}     $ALREADY_BOOKED_COUNT"
echo -e "  ${RED}Other errors:${NC}          $OTHER_ERROR_COUNT"
echo ""
echo "  Total requests: $NUM_REQUESTS"
echo ""

# Verification
if [ $SUCCESS_COUNT -eq 1 ]; then
    echo -e "${GREEN}✓ TEST PASSED: Exactly 1 booking succeeded (expected behavior)${NC}"
    echo ""
    echo "  This confirms pessimistic locking is working correctly."
else
    echo -e "${RED}✗ TEST FAILED: Expected 1 success, got $SUCCESS_COUNT${NC}"
    echo ""
    echo "  WARNING: Either locking is not working or there's a race condition."
fi

echo ""
echo "Detailed outputs saved in: $OUTPUT_DIR"
echo ""
echo "To check database state:"
echo "  mysql -u root -p -e \"SELECT * FROM consultation_booking.bookings WHERE topic='concurrency_test';\""
echo ""
echo "To check slot bookings:"
echo "  mysql -u root -p -e \"SELECT b.booking_id, b.status, b.slot_id, b.appointment_date"
echo "    FROM consultation_booking.bookings b"
echo "    JOIN consultation_booking.availability_slots s ON b.slot_id = s.slot_id"
echo "    WHERE b.topic='concurrency_test';\""
echo ""

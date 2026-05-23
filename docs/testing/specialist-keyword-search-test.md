# Specialist Keyword and Availability Search Test Record

## Module
Specialist keyword and availability search

## Branch
feature/specialist-keyword-search

## Updated Endpoint
GET /api/specialists/search

## Supported Query Parameters
- keyword optional
- categoryId optional
- levelId optional
- status optional
- onlyAvailable optional boolean

## Search Rule
The keyword search supports case-insensitive matching across:
- specialist username
- specialist email
- expertise category name
- level name
- specialist information
- specialist status

Existing categoryId, levelId, and status filters should continue to work.

## Availability Filter Rule
If `onlyAvailable=true`, the system should return only specialists who have at least one availability slot with status `AVAILABLE`.

---

## Test Case 1: Search by expertise category keyword

Example:
GET /api/specialists/search?keyword=software

Expected result:
- Specialists whose expertise category or related information contains "software" should be returned.

Actual result:
- To be completed during testing.

Screenshot:
- docs/testing/screenshots/specialist-keyword-category.png

---

## Test Case 2: Search by level keyword

Example:
GET /api/specialists/search?keyword=senior

Expected result:
- Specialists whose level name contains "senior" should be returned.

Actual result:
- To be completed during testing.

Screenshot:
- docs/testing/screenshots/specialist-keyword-level.png

---

## Test Case 3: Search by specialist username keyword

Example:
GET /api/specialists/search?keyword=specialist1

Expected result:
- Matching specialist records should be returned.

Actual result:
- To be completed during testing.

Screenshot:
- docs/testing/screenshots/specialist-keyword-username.png

---

## Test Case 4: Search by information keyword

Example:
GET /api/specialists/search?keyword=career

Expected result:
- Specialists whose information field contains "career" should be returned.

Actual result:
- To be completed during testing.

Screenshot:
- docs/testing/screenshots/specialist-keyword-information.png

---

## Test Case 5: Search by keyword and status

Example:
GET /api/specialists/search?keyword=software&status=ACTIVE

Expected result:
- Only ACTIVE specialists matching the keyword should be returned.

Actual result:
- To be completed during testing.

Screenshot:
- docs/testing/screenshots/specialist-keyword-status.png

---

## Test Case 6: Search only specialists with available slots

Example:
GET /api/specialists/search?onlyAvailable=true

Expected result:
- Only specialists who have at least one AVAILABLE slot should be returned.
- Specialists without AVAILABLE slots should be excluded.

Actual result:
- To be completed during testing.

Screenshot:
- docs/testing/screenshots/specialist-only-available.png

---

## Test Case 7: Search by keyword and availability

Example:
GET /api/specialists/search?keyword=software&onlyAvailable=true

Expected result:
- Only specialists matching the keyword and having at least one AVAILABLE slot should be returned.

Actual result:
- To be completed during testing.

Screenshot:
- docs/testing/screenshots/specialist-keyword-available.png

---

## Test Case 8: Search by category, level, status, and availability

Example:
GET /api/specialists/search?categoryId=1&levelId=1&status=ACTIVE&onlyAvailable=true

Expected result:
- Only specialists matching all filters and having at least one AVAILABLE slot should be returned.

Actual result:
- To be completed during testing.

Screenshot:
- docs/testing/screenshots/specialist-filter-available.png

---

## Test Case 9: Search with no result

Example:
GET /api/specialists/search?keyword=notexistingkeyword

Expected result:
- System returns an empty list.
- Application does not crash.

Actual result:
- To be completed during testing.

Screenshot:
- docs/testing/screenshots/specialist-keyword-no-result.png

---

## Test Case 10: Search with no parameters

Example:
GET /api/specialists/search

Expected result:
- System returns all specialists.

Actual result:
- To be completed during testing.

Screenshot:
- docs/testing/screenshots/specialist-keyword-no-params.png

---

## Notes for Group Report

The specialist search module was improved from technical ID-based filtering to user-friendly keyword search and availability-based filtering. Customers can search specialists using expertise names, level names, usernames, email addresses, status values, or profile information. They can also choose to show only specialists with available consultation slots. This makes the specialist browsing workflow closer to a real user-facing consultation booking system.

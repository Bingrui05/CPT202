# Admin Category CRUD Test Record

## Module
Admin category CRUD (extends admin-management module)

## Owner
Student F

## Branch
feature/admin-category-crud

## Related Issue
Extend admin-management module so that administrators can manage expertise categories (CRUD operations)

## Test Environment
- Java 23
- Spring Boot 3.3.0
- Maven
- MySQL
- Localhost port: 8080

---

## API Endpoints

| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | /api/admin/expertise-categories | Get all expertise categories |
| GET | /api/admin/expertise-categories/{categoryId} | Get one expertise category by ID |
| POST | /api/admin/expertise-categories | Create a new expertise category |
| PUT | /api/admin/expertise-categories/{categoryId} | Update an existing expertise category |
| DELETE | /api/admin/expertise-categories/{categoryId} | Soft delete / deactivate an expertise category |

---

## Test Case 1: Get all expertise categories

**Endpoint:**

`GET /api/admin/expertise-categories`

**Expected result:**
- Returns HTTP 200 with a list of all expertise categories.
- Each category contains: `categoryId`, `name`, `status`.
- ApiResponse format: `{"success": true, "message": "...", "data": [...]}`

**Actual result:**
- **PLACEHOLDER** - Fill after testing

**Screenshot:**
- docs/screenshots/admin-category-crud/get-all-categories.png

---

## Test Case 2: Get expertise category by ID

**Endpoint:**

`GET /api/admin/expertise-categories/{categoryId}`

**Precondition:** At least one expertise category exists in the database.

**Test steps:**
1. Send GET request with an existing `categoryId` (e.g., 1).

**Expected result:**
- Returns HTTP 200 with the category matching the given ID.
- Response body: `{"success": true, "message": "Expertise category retrieved successfully", "data": {"categoryId": 1, "name": "Software Engineering", "status": "ACTIVE"}}`

**Actual result:**
- **PLACEHOLDER** - Fill after testing

**Screenshot:**
- docs/screenshots/admin-category-crud/get-category-by-id.png

---

## Test Case 3: Get expertise category by ID — not found

**Endpoint:**

`GET /api/admin/expertise-categories/{categoryId}`

**Test steps:**
1. Send GET request with a non-existent `categoryId` (e.g., 9999).

**Expected result:**
- Returns HTTP 404.
- Response body: `{"success": false, "message": "Expertise category not found with id: 9999", "data": null}`

**Actual result:**
- **PLACEHOLDER** - Fill after testing

**Screenshot:**
- docs/screenshots/admin-category-crud/get-category-not-found.png

---

## Test Case 4: Create expertise category successfully

**Endpoint:**

`POST /api/admin/expertise-categories`

**Test steps:**
1. Send POST request with the following JSON body:

```json
{
  "name": "Career Consulting",
  "status": "ACTIVE"
}
```

**Expected result:**
- Returns HTTP 201 Created.
- Response body contains the newly created category with a generated `categoryId`.
- Example: `{"success": true, "message": "Expertise category created successfully", "data": {"categoryId": 3, "name": "Career Consulting", "status": "ACTIVE"}}`

**Actual result:**
- **PLACEHOLDER** - Fill after testing

**Screenshot:**
- docs/screenshots/admin-category-crud/create-category-success.png

---

## Test Case 5: Create expertise category — status defaults to ACTIVE when not provided

**Endpoint:**

`POST /api/admin/expertise-categories`

**Test steps:**
1. Send POST request with only the name, without status:

```json
{
  "name": "Mental Health Support"
}
```

**Expected result:**
- Returns HTTP 201 Created.
- The category's `status` defaults to `"ACTIVE"`.
- Example: `{"success": true, "message": "Expertise category created successfully", "data": {"categoryId": 4, "name": "Mental Health Support", "status": "ACTIVE"}}`

**Actual result:**
- **PLACEHOLDER** - Fill after testing

**Screenshot:**
- docs/screenshots/admin-category-crud/create-category-default-status.png

---

## Test Case 6: Create expertise category — reject blank category name

**Endpoint:**

`POST /api/admin/expertise-categories`

**Test steps:**
1. Send POST request with a blank or empty name:

```json
{
  "name": "",
  "status": "ACTIVE"
}
```

**Expected result:**
- Returns HTTP 400 Bad Request.
- Response body contains validation error: `"Category name is required"` or Spring validation error format.
- The category is NOT created in the database.

**Actual result:**
- **PLACEHOLDER** - Fill after testing

**Screenshot:**
- docs/screenshots/admin-category-crud/create-category-blank-name.png

---

## Test Case 7: Create expertise category — reject duplicate name (case-insensitive)

**Endpoint:**

`POST /api/admin/expertise-categories`

**Precondition:** A category named "Software Engineering" already exists.

**Test steps:**
1. Send POST request with a duplicate name (any case variation):

```json
{
  "name": "software engineering",
  "status": "ACTIVE"
}
```

**Expected result:**
- Returns HTTP 400 Bad Request.
- Response body: `{"success": false, "message": "Expertise category with name 'software engineering' already exists", "data": null}`

**Actual result:**
- **PLACEHOLDER** - Fill after testing

**Screenshot:**
- docs/screenshots/admin-category-crud/create-category-duplicate.png

---

## Test Case 8: Update category name successfully

**Endpoint:**

`PUT /api/admin/expertise-categories/{categoryId}`

**Precondition:** At least one expertise category exists.

**Test steps:**
1. Send PUT request with an existing `categoryId` and new name:

```json
{
  "name": "Updated Category Name",
  "status": "ACTIVE"
}
```

**Expected result:**
- Returns HTTP 200.
- The category's name is updated in the database.
- Response body contains the updated category.

**Actual result:**
- **PLACEHOLDER** - Fill after testing

**Screenshot:**
- docs/screenshots/admin-category-crud/update-category-name.png

---

## Test Case 9: Update category status successfully

**Endpoint:**

`PUT /api/admin/expertise-categories/{categoryId}`

**Precondition:** At least one expertise category exists.

**Test steps:**
1. Send PUT request changing only the status:

```json
{
  "name": "Software Engineering",
  "status": "INACTIVE"
}
```

**Expected result:**
- Returns HTTP 200.
- The category's `status` is changed to `"INACTIVE"`.
- The `name` remains unchanged.

**Actual result:**
- **PLACEHOLDER** - Fill after testing

**Screenshot:**
- docs/screenshots/admin-category-crud/update-category-status.png

---

## Test Case 10: Update expertise category — reject updating non-existing category

**Endpoint:**

`PUT /api/admin/expertise-categories/{categoryId}`

**Test steps:**
1. Send PUT request with a non-existent `categoryId` (e.g., 9999):

```json
{
  "name": "Some Name",
  "status": "ACTIVE"
}
```

**Expected result:**
- Returns HTTP 404.
- Response body: `{"success": false, "message": "Expertise category not found with id: 9999", "data": null}`

**Actual result:**
- **PLACEHOLDER** - Fill after testing

**Screenshot:**
- docs/screenshots/admin-category-crud/update-category-not-found.png

---

## Test Case 11: Update expertise category — reject blank name

**Endpoint:**

`PUT /api/admin/expertise-categories/{categoryId}`

**Test steps:**
1. Send PUT request with an existing `categoryId` and a blank name:

```json
{
  "name": "",
  "status": "ACTIVE"
}
```

**Expected result:**
- Returns HTTP 400 Bad Request.
- Validation error for blank name.
- No changes are made to the category.

**Actual result:**
- **PLACEHOLDER** - Fill after testing

**Screenshot:**
- docs/screenshots/admin-category-crud/update-category-blank-name.png

---

## Test Case 12: Update expertise category — reject duplicate name

**Endpoint:**

`PUT /api/admin/expertise-categories/{categoryId}`

**Precondition:** At least two different expertise categories exist.

**Test steps:**
1. Send PUT request to change category 1's name to match category 2's existing name.

**Expected result:**
- Returns HTTP 400 Bad Request.
- Response body: `{"success": false, "message": "Expertise category with name '...' already exists", "data": null}`

**Actual result:**
- **PLACEHOLDER** - Fill after testing

**Screenshot:**
- docs/screenshots/admin-category-crud/update-category-duplicate-name.png

---

## Test Case 13: Delete / deactivate category successfully

**Endpoint:**

`DELETE /api/admin/expertise-categories/{categoryId}`

**Precondition:** At least one expertise category exists.

**Test steps:**
1. Send DELETE request with an existing `categoryId`.

**Expected result:**
- Returns HTTP 200.
- The category is **soft deleted** — status is set to `"INACTIVE"` but the record remains in the database.
- Response body contains the updated category with `status: "INACTIVE"`.
- The category is NOT physically removed from the database.

**Actual result:**
- **PLACEHOLDER** - Fill after testing

**Screenshot:**
- docs/screenshots/admin-category-crud/delete-category-success.png

---

## Test Case 14: Verify deactivated category status becomes INACTIVE

**Endpoint:**

`GET /api/admin/expertise-categories/{categoryId}`

**Precondition:** A category was deactivated in Test Case 13.

**Test steps:**
1. Send GET request to retrieve the deactivated category by its ID.
2. Verify the `status` field is `"INACTIVE"`.

**Expected result:**
- Returns HTTP 200.
- The category's `status` is `"INACTIVE"`.
- All other fields (name, categoryId) remain unchanged.

**Actual result:**
- **PLACEHOLDER** - Fill after testing

**Screenshot:**
- docs/screenshots/admin-category-crud/verify-category-inactive.png

---

## Test Case 15: Delete non-existing category

**Endpoint:**

`DELETE /api/admin/expertise-categories/{categoryId}`

**Test steps:**
1. Send DELETE request with a non-existent `categoryId` (e.g., 9999).

**Expected result:**
- Returns HTTP 404.
- Response body: `{"success": false, "message": "Expertise category not found with id: 9999", "data": null}`

**Actual result:**
- **PLACEHOLDER** - Fill after testing

**Screenshot:**
- docs/screenshots/admin-category-crud/delete-category-not-found.png

---

## Notes for Group Report

The admin category CRUD module extends the existing admin-management module by providing full CRUD operations for expertise categories. This module is designed for the first release.

**Implemented functionality:**

1. **GET /api/admin/expertise-categories** — Lists all categories regardless of status (ACTIVE or INACTIVE).
2. **GET /api/admin/expertise-categories/{id}** — Retrieves a single category by ID, returns 404 if not found.
3. **POST /api/admin/expertise-categories** — Creates a new category with validation:
   - Name is required and cannot be blank.
   - Name uniqueness is enforced case-insensitively.
   - Status defaults to ACTIVE if not provided.
4. **PUT /api/admin/expertise-categories/{id}** — Updates an existing category:
   - Name and status can be updated.
   - Name cannot be blank.
   - Duplicate name (case-insensitive) is rejected.
   - Status must be ACTIVE or INACTIVE.
5. **DELETE /api/admin/expertise-categories/{id}** — Soft deletes a category by setting status to INACTIVE (no hard delete).

**Business rules enforced:**
- No hard delete — categories remain in database to avoid breaking specialist references.
- Case-insensitive name uniqueness prevents "Software Engineering" and "software engineering" from coexisting.
- Proper error messages returned for all failure scenarios.

**Security Notes:**
- No authentication/authorization implemented in this module.
- Role-based access control should be added in future releases.

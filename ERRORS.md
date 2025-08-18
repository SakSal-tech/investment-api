# Error Log and Solutions

This file documents errors encountered during development and how they were solved.

## Example Entry

**Error:**
401 Unauthorized when accessing /api/clients in Postman

**Solution:**
Created a SecurityConfig class to allow all /api/** endpoints without authentication.

---
**Error:**
403 Forbidden

**Solution:**
The client I was trying to access (c57b9a5f-7c78-11f0-a429-84ba590b0166) has active = 0x00, which likely means inactive.Spring APIs is configured to forbid access to inactive clients, even if the request is valid. That is why your request returns 403 Forbidden.
Add your errors and solutions below:


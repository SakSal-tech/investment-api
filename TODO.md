# To-Do List
- [ ] Write unit tests for user managements pack
- [ ] Research and understand why use GenerationType.AUTO instead of GenerationType.UUID, change in the data model classes if it more efficient
- [ ] create HTTP requests (get,post,put and delete) ready for Postman using correct api endpoints.
- [ ] Read about Regex and validate password and email format in User class.
- [ ]  Build more tests for the validate method from user service class
- [ ] RiskService class         
         Check the setDetailsJson methods. This gave me errors in the past as JPA (Hibernate) doesn’t natively support JSON fields unless your DB column is set to a JSON type (like in PostgreSQL with jsonb). AS I am using H2, MySQL, or plain VARCHAR columns, this JSON will just be stored as a string.
        risk.setDetailsJson("{\"shockFactor\":" + shockFactor + "}");If  later need queryable JSON fields (e.g., filter by keys inside the JSON), MySQL JSON column type would be better. JPA itself doesn’t support JSON natively, but I can: Use Hibernate Types library (@Type(JsonType.class)).Or keep it as text (your current way) but push JSON querying into custom SQL.


- [ ] Add JUnitParams parameterised test .




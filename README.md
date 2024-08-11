# Installation

> Copy file from `src/main/resources/app.properties.example` to `src/main/resources/app.properties`, and change all setting for you own

```
db.driver=com.mysql.cj.jdbc.Driver
db.url=jdbc:mysql://localhost:3306/database
db.username=root
db.password=root
# JDBC - 1, Hibernate - 2
db.mode=1
```
> db.mode property switch `UserDaoJDBCImpl` or `UserDaoHibernateImpl` classes in `UserServiceImpl` class
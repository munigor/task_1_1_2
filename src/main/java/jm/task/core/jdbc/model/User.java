package jm.task.core.jdbc.model;

import jm.task.core.jdbc.util.ColumnValue;

import javax.persistence.*;

@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ColumnValue(value = "int NOT NULL AUTO_INCREMENT PRIMARY KEY")
    private Long id;

    @Column
    @ColumnValue(value = "VARCHAR(40) NOT NULL")
    private String name;

    @Column(name = "last_name")
    @ColumnValue(value = "VARCHAR(40) NOT NULL")
    private String lastName;

    @Column
    @ColumnValue(value = "TINYINT NOT NULL")
    private Byte age;

    public User() {

    }

    public User(String name, String lastName, Byte age) {
        this.name = name;
        this.lastName = lastName;
        this.age = age;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public Byte getAge() {
        return age;
    }

    public void setAge(Byte age) {
        this.age = age;
    }

    @Override
    public String toString() {
        return "%s{id=%d, name='%s', lastName='%s', age=%d}".formatted(User.class.getSimpleName(), id, name, lastName, age);
    }
}

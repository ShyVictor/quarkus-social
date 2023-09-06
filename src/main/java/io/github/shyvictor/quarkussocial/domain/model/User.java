package io.github.shyvictor.quarkussocial.domain.model;


import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Table;
import jakarta.persistence.Column;
import lombok.Getter;
import lombok.Setter;


@Entity
@Table(name="users")
@Getter @Setter
public class User {


    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id;
    @Column
    private String name;
    @Column
    private Integer age;

}

package co.com.tecnohalecatez.r2dbc.entity;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.math.BigInteger;
import java.time.LocalDate;

@Table("users")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder(toBuilder = true)
public class UserEntity {
    @Id
    private BigInteger id;
    private String name;
    private String surname;
    @Column("birth_date")
    private LocalDate birthDate;
    private String address;
    private String phone;
    private String email;
    @Column("base_salary")
    private Double baseSalary;
}

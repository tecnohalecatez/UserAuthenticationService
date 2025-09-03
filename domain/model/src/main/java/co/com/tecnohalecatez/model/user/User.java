package co.com.tecnohalecatez.model.user;

import lombok.*;

import java.math.BigInteger;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
@ToString
public class User {
    private BigInteger id;
    private String name;
    private String surname;
    private LocalDate birthDate;
    private String address;
    private String phone;
    private String email;
    private String password;
    private Double baseSalary;
    private Integer roleId;
}

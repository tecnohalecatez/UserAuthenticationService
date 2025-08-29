package co.com.tecnohalecatez.model.user;

import org.junit.jupiter.api.Test;

import java.math.BigInteger;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class UserTest {

    @Test
    void createUser_WithBuilder_ShouldSetAllFields() {
        LocalDate birthDate = LocalDate.of(1990, 1, 1);
        
        User user = User.builder()
                .id(BigInteger.ONE)
                .name("John")
                .surname("Doe")
                .birthDate(birthDate)
                .address("123 Main St")
                .phone("555-1234")
                .email("john.doe@example.com")
                .baseSalary(50000.0)
                .build();

        assertAll(
                () -> assertEquals(BigInteger.ONE, user.getId()),
                () -> assertEquals("John", user.getName()),
                () -> assertEquals("Doe", user.getSurname()),
                () -> assertEquals(birthDate, user.getBirthDate()),
                () -> assertEquals("123 Main St", user.getAddress()),
                () -> assertEquals("555-1234", user.getPhone()),
                () -> assertEquals("john.doe@example.com", user.getEmail()),
                () -> assertEquals(50000.0, user.getBaseSalary())
        );
    }

    @Test
    void createUser_WithNoArgsConstructor_ShouldCreateEmptyUser() {
        User user = new User();
        
        assertAll(
                () -> assertNull(user.getId()),
                () -> assertNull(user.getName()),
                () -> assertNull(user.getSurname()),
                () -> assertNull(user.getBirthDate()),
                () -> assertNull(user.getAddress()),
                () -> assertNull(user.getPhone()),
                () -> assertNull(user.getEmail()),
                () -> assertNull(user.getBaseSalary())
        );
    }

    @Test
    void createUser_WithAllArgsConstructor_ShouldSetAllFields() {
        LocalDate birthDate = LocalDate.of(1990, 1, 1);
        
        User user = new User(
                BigInteger.ONE,
                "John",
                "Doe",
                birthDate,
                "123 Main St",
                "555-1234",
                "john.doe@example.com",
                50000.0
        );

        assertAll(
                () -> assertEquals(BigInteger.ONE, user.getId()),
                () -> assertEquals("John", user.getName()),
                () -> assertEquals("Doe", user.getSurname()),
                () -> assertEquals(birthDate, user.getBirthDate()),
                () -> assertEquals("123 Main St", user.getAddress()),
                () -> assertEquals("555-1234", user.getPhone()),
                () -> assertEquals("john.doe@example.com", user.getEmail()),
                () -> assertEquals(50000.0, user.getBaseSalary())
        );
    }

    @Test
    void toBuilder_ShouldCreateCopyWithModifications() {
        User originalUser = User.builder()
                .id(BigInteger.ONE)
                .name("John")
                .surname("Doe")
                .email("john.doe@example.com")
                .build();

        User modifiedUser = originalUser.toBuilder()
                .name("Jane")
                .email("jane.doe@example.com")
                .build();

        assertAll(
                () -> assertEquals(BigInteger.ONE, modifiedUser.getId()),
                () -> assertEquals("Jane", modifiedUser.getName()),
                () -> assertEquals("Doe", modifiedUser.getSurname()),
                () -> assertEquals("jane.doe@example.com", modifiedUser.getEmail())
        );
    }

    @Test
    void toString_ShouldReturnStringRepresentation() {
        User user = User.builder()
                .id(BigInteger.ONE)
                .name("John")
                .surname("Doe")
                .email("john.doe@example.com")
                .build();

        String userString = user.toString();
        
        assertAll(
                () -> assertNotNull(userString),
                () -> assertTrue(userString.contains("John")),
                () -> assertTrue(userString.contains("Doe")),
                () -> assertTrue(userString.contains("john.doe@example.com"))
        );
    }

    @Test
    void settersAndGetters_ShouldWorkCorrectly() {
        User user = new User();
        LocalDate birthDate = LocalDate.of(1990, 1, 1);

        user.setId(BigInteger.ONE);
        user.setName("John");
        user.setSurname("Doe");
        user.setBirthDate(birthDate);
        user.setAddress("123 Main St");
        user.setPhone("555-1234");
        user.setEmail("john.doe@example.com");
        user.setBaseSalary(50000.0);

        assertAll(
                () -> assertEquals(BigInteger.ONE, user.getId()),
                () -> assertEquals("John", user.getName()),
                () -> assertEquals("Doe", user.getSurname()),
                () -> assertEquals(birthDate, user.getBirthDate()),
                () -> assertEquals("123 Main St", user.getAddress()),
                () -> assertEquals("555-1234", user.getPhone()),
                () -> assertEquals("john.doe@example.com", user.getEmail()),
                () -> assertEquals(50000.0, user.getBaseSalary())
        );
    }
}
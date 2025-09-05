package co.com.tecnohalecatez.model.role;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
@ToString
public class Role {
    private Integer id;
    private String name;
    private String description;
}

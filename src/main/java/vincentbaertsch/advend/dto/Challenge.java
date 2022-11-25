package vincentbaertsch.advend.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@Table(name = "challenges")
public class Challenge {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Temporal(TemporalType.DATE)
    @Column(unique=true)
    private Date day;
    private String title;
    private String descriptions;
    private Integer points;

    @ElementCollection
    private List<String> videoLinks  = new ArrayList<>();
}

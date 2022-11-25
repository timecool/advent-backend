package vincentbaertsch.advend.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Setter
@Getter
@Entity
@NoArgsConstructor
public class UserChallenge {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private boolean isChecked = false;

    private String filePath;
    @ManyToOne
    @JoinColumn(name = "challenge_id")
    private Challenge challenge;

}

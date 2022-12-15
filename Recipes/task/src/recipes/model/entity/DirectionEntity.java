package recipes.model.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;

@Entity
@Table(name = "directions")
@Setter
@Getter
@NoArgsConstructor
@ToString
public class DirectionEntity {
    @Id
    @SequenceGenerator(name = "DIRECTION_SEQUENCE", sequenceName = "DIRECTION_SEQUENCE_ID", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "DIRECTION_SEQUENCE_ID")
    private long id;

    @ManyToOne
    @JoinColumn(name = "recipe_id")
    private RecipeEntity recipe;

    @Column(name = "direction")
    private String direction;

    public DirectionEntity(String direction) {
        this.direction = direction;
    }
}


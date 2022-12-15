package recipes.model.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;

@Entity
@Table(name = "ingredients")
@Setter
@Getter
@NoArgsConstructor
@ToString
public class IngredientEntity {
    @Id
    @SequenceGenerator(name = "INGREDIENT_SEQUENCE", sequenceName = "INGREDIENT_SEQUENCE_ID", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "INGREDIENT_SEQUENCE_ID")
    private long id;

    @ManyToOne
    @JoinColumn(name = "recipe_id")
    private RecipeEntity recipe;

    @Column(name = "ingredient")
    private String ingredient;

    public IngredientEntity(String ingredient) {
        this.ingredient = ingredient;
    }
}

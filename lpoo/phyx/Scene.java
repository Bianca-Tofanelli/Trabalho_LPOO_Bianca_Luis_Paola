package lpoo.phyx;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Bianca Tofanelli
 *         Luis Cardoso
 *         Paola Vendruscolo 
 */
/* Classe pública que representa a cena
 */
public class Scene {
    private String name;
    private List<RigidBody> actors;

    // inicializa a cena
    public Scene (String name) {
        if (name == null){
            throw new IllegalArgumentException("O nome não pode ser nulo.");
        }

        this.name = name;
        this.actors = new ArrayList<>();
    }  

    // adiciona um corpo rígido, ator, na cena
    public void addActor (RigidBody actor) {
       if (actor == null) {
            throw new IllegalArgumentException("O ator não pode ser nulo.");
        }
        
        this.actors.add(actor);
    }

    // para pegar o nome
    public String getName() {
        return name;
    }

    // para pegar a lista contendo todos os atores
    public List<RigidBody> getActors() {
        return actors;
    }
} // Scene

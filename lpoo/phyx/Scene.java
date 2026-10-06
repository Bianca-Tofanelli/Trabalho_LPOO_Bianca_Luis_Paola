package lpoo.phyx;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Bianca Tofanelli
 *         Luis Cardoso
 *         Paola Vendruscolo 
 */
public class Scene {
    private String name;
    private List<RigidBody> actors;
    public Scene (String name){
        if (name == null){
            throw new IllegalArgumentException("O nome não pode ser nulo.");
        }
        this.name = name;
        this.actors = new ArrayList<>();
    }  
    public void addActor (RigidBody actor){
       if (actor == null) {
            throw new IllegalArgumentException("O ator não pode ser nulo.");
        }
        this.actors.add(actor);
    }
    public String getName(){
        return name;
    }
    public List<RigidBody> getActors(){
        return actors;
    }
}

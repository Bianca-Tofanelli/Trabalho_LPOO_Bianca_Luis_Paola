package lpoo.geom;
import lpoo.math.*;

/**
 *
 * @author Bianca Tofanelli
 *         Luis Cardoso
 *         Paola Vendruscolo 
 */
/* Classe abstrata que define uma Shape
 */
public abstract class Shape {
    protected String name;
    protected Pose pose;
    protected float mass;
    protected Vector3 center_mass;
    protected Matrix3 local_inertia;

    // inicializa a forma
    public Shape(String name){
        this.name = name;
        this.pose = new Pose(); 
        this.center_mass = Vector3.NULL;
        this.local_inertia = Matrix3.identity(); 
        this.mass = 0;
    }    

    // para pegar o nome
    public String getName(){
        return name;
    }

    // para pegar a pose
    public Pose getPose(){
        return pose;
    }

    // para pegar a massa total
    public float getMass(){
        return this.mass;
    }

    // para pegar o centro de massa
    public Vector3 getCenterOfMass(){
        return this.center_mass;
    }

    // para pegar a inércia local
    public Matrix3 getLocalInertia(){
        return this.local_inertia;
    }

    // para definir uma nova pose
    public void setPose(Pose pose){
        this.pose = pose;
    }

    // método abstrato para calcular a área
    public abstract float getArea();

    // método abstrato para calcular o volume
    public abstract float getVolume();

    // método abstrato para calcular a caixa limitante
    public abstract Bounds3 getBounds();
} // Shape

    
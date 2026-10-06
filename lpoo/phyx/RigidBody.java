package lpoo.phyx;

import lpoo.geom.*;
import lpoo.math.*;

/**
 *
 * @author Bianca Tofanelli
 *         Luis Cardoso
 *         Paola Vendruscolo 
 */
// Classe pública que representa um corpo rígido
 
public class RigidBody {
    private String name;
    private Pose pose;
    private Shape shape;

    // inicializa o corpo rígido
    public RigidBody(String name, Pose pose, Shape shape){
        if (shape == null){
            throw new IllegalArgumentException("A forma do corpo rígido não pode ser nula.");
        }

        this.name = name;

        if (pose != null){
          this.pose = pose;
        } 
        
        else {
          this.pose = new Pose();
        }

        this.shape = shape;
    }

    // para pegar o nome
    public String getName(){
        return name;
    }

    // para pegar a pose
    public Pose getPose() {
        return pose;
    }

    // para pegar a forma
    public Shape getShape() {
        return shape;
    }

    // para pegar a area
    public float getArea() {
        return shape.getArea(); 
    }

    // para pegar o volume
    public float getVolume() {
        return shape.getVolume(); 
    }

    // para pegar a massa
    public float getMass() {
        return shape.getMass(); 
    }

    // calcular o centro de massa
    public Vector3 getCenterOfMass() {
        return pose.transformTR(shape.getCenterOfMass());
    }

    // calcular o tensor de inércia
    public Matrix3 getInertia() {
        Matrix3 rot = pose.getRotationMatrix();
        Matrix3 Local = shape.getLocalInertia();
        Matrix3 rotTransposed = rot.transpose();
        return rot.mul(Local).mul(rotTransposed);
    }

    // calcular a caixa limitante
    public Bounds3 getBounds() {
        Bounds3 localBounds = shape.getBounds();

        if (localBounds == null) {
            return null;
        }

        Vector3 min = localBounds.min();
        Vector3 max = localBounds.max();
        Vector3[] vertices = {
            new Vector3(min.x, min.y, min.z),
            new Vector3(max.x, min.y, min.z),
            new Vector3(min.x, max.y, min.z),
            new Vector3(max.x, max.y, min.z),
            new Vector3(min.x, min.y, max.z),
            new Vector3(max.x, min.y, max.z),
            new Vector3(min.x, max.y, max.z),
            new Vector3(max.x, max.y, max.z)
        };
        Bounds3 globalBounds = new Bounds3();

        for (int i = 0; i < vertices.length; i++) {
            Vector3 transformedPoint = pose.transformTR(vertices[i]);
            globalBounds.expand(transformedPoint);
        }
        
        return globalBounds;
    } 
} // RigidBody
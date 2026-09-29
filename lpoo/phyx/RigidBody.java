package lpoo.phyx;

import lpoo.geom.*;
import lpoo.math.*;
/**
 *
 * @author insert your name here
 */
public class RigidBody{
    private String name;
    private Pose pose;
    private Shape shape;
    public RigidBody(String name, Pose pose, Shape shape){
        if (shape == null){
            throw new IllegalArgumentException("A forma do corpo rígido não pode ser nula.");
        }
        this.name = name;
        if (pose != null){
          this.pose = pose;
        } else {
          this.pose = new Pose();
        }
        this.shape = shape;
    }
    public String getName(){
        return name;
    }
    public Pose getPose() {
        return pose;
    }
    public Shape getShape() {
        return shape;
    }
    public float getArea() {
        return shape.getArea(); 
    }
    public float getVolume() {
        return shape.getVolume(); 
    }
    public float getMass() {
        return shape.getMass(); 
    }
    public Vector3 getCenterOfMass() {
        return pose.transformTR(shape.getCenterOfMass());
    }
    public Matrix3 getInertia() {
        Matrix3 rot = pose.getRotationMatrix();
        Matrix3 Local = shape.getLocalInertia();
        Matrix3 rotTransposed = rot.transpose();
        return rot.mul(Local).mul(rotTransposed);
    }
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
}
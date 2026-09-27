package lpoo.geom;
import lpoo.math.*;

public abstract class Shape{
    protected String name;
    protected Pose pose;
    protected float mass;
    protected Vector3 center_mass;
    protected Matrix3 local_inertia;
    public Shape(String name){
        this.name = name;
        this.pose = new Pose(); 
        this.center_mass = Vector3.NULL;
        this.local_inertia = Matrix3.identity(); 
        this.mass = 0;
    }    
    public String getName(){
        return name;
    }
    public Pose getPose(){
        return pose;
    }
    public void setPose(Pose pose){
        this.pose = pose;
    }
    public float getMass() {
        return this.mass;
    }
    public Vector3 getCenterOfMass() {
        return this.center_mass;
    }
    public Matrix3 getLocalInertia() {
        return this.local_inertia;
    }
    public abstract float getArea();
    public abstract float getVolume();
    public abstract Bounds3 getBounds();
    }

    
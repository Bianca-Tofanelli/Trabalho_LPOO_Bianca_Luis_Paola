package lpoo.geom;

import lpoo.math.*;
import java.util.ArrayList;
import java.util.List;

public class CompositeShape extends Shape{
    private List<Shape> shapes;
    public CompositeShape ( String name){
        super(name);
        this.shapes = new ArrayList<>();
    }
    public void addShape ( Shape shape){
        if (shape == null) {
            return;
        }
        this.shapes.add(shape);
        this.mass = 0;
        for (int i = 0; i < this.shapes.size(); i++) {
           this.mass = this.mass + this.shapes.get(i).getMass();
        }
        float sumx = 0;
        float sumy = 0;
        float sumz = 0;
        this.center_mass = Vector3.NULL;
        for (int i = 0; i < this.shapes.size(); i++) {
            Vector3 centerfigure = this.shapes.get(i).getCenterOfMass();
            sumx = sumx + (centerfigure.x * this.shapes.get(i).getMass());
            sumy = sumy + (centerfigure.y * this.shapes.get(i).getMass());
            sumz = sumz + (centerfigure.z * this.shapes.get(i).getMass());
        }
        if (this.mass > 0) {
            float centroX = sumx / this.mass;
            float centroY = sumy / this.mass;
            float centroZ = sumz / this.mass;
            this.center_mass = new Vector3(centroX, centroY, centroZ);
        }
        this.local_inertia = computeLocalInertia();
    }  
    public float getArea(){ 
        float sumarea = 0;
        for (int i = 0; i < this.shapes.size(); i++) {
            sumarea = sumarea + this.shapes.get(i).getArea();
        }
        return sumarea;
    }
    public float getVolume(){ 
        float sumvolume = 0;
        for (int i = 0; i < this.shapes.size(); i++) {
            sumvolume = sumvolume + this.shapes.get(i).getVolume();
        }
        return sumvolume;
    }
    
    public Bounds3 getBounds(){
        if (this.shapes.isEmpty()){
            return null;
        }
        Bounds3 completeBound = new Bounds3();
        for (int i = 0; i < this.shapes.size(); i++) {
           completeBound.expand(this.shapes.get(i).getBounds());
        }
        return completeBound;
    }
    protected Matrix3 computeLocalInertia(){
       Matrix3 accumulatorInertia = Matrix3.zero();
       for (int i = 0; i < this.shapes.size(); i++) {
        Vector3 distance = this.shapes.get(i).getCenterOfMass().sub(this.center_mass);
        float distance2 = (distance.x * distance.x) + (distance.y * distance.y) + (distance.z * distance.z);
        Matrix3 term1 = Matrix3.diagonal(this.shapes.get(i).getMass() * distance2);
        Matrix3 term2 = Matrix3.outer(distance, this.shapes.get(i).getMass());
        Matrix3 steiner = term1.add(term2.mul(-1f));
        Matrix3 inertiaTotalPeca = this.shapes.get(i).getLocalInertia().add(steiner);
        accumulatorInertia = accumulatorInertia.add(inertiaTotalPeca);
    }
    return accumulatorInertia;
  }
}  
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
    }  
}  
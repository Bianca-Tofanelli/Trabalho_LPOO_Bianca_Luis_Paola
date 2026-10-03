package lpoo.geom;

import lpoo.math.*;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Bianca Tofanelli
 *         Luis Cardoso
 *         Paola Vendruscolo 
 */
/* A classe pública CompositeShape, guarda as formas colocadas nela, "filha" da classe Shape. 
 */
public class CompositeShape extends Shape
{
    private List<Shape> shapes; // lista de shapes

    public CompositeShape ( String name)
    {
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
            Vector3 pi = this.shapes.get(i).getPose().transformTR(this.shapes.get(i).getCenterOfMass());
            
            sumx = sumx + (pi.x * this.shapes.get(i).getMass());
            sumy = sumy + (pi.y * this.shapes.get(i).getMass());
            sumz = sumz + (pi.z * this.shapes.get(i).getMass());
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
    
    public Bounds3 getBounds() {
        if (this.shapes.isEmpty()) {
            return null;
        }
        Bounds3 completeBound = new Bounds3();

        for (int i = 0; i < this.shapes.size(); i++) {
            Bounds3 childBounds = this.shapes.get(i).getBounds();
            if (childBounds == null){
                continue;
            }
            
            Vector3 min = childBounds.min();
            Vector3 max = childBounds.max();
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

            
            for (Vector3 vertex : vertices) {
                Vector3 transformedPoint = this.shapes.get(i).getPose().transformTR(vertex);
                completeBound.expand(transformedPoint);
            }
        }
        return completeBound;
    }
    protected Matrix3 computeLocalInertia() {
        Matrix3 accumulatorInertia = Matrix3.zero();

        for (int i = 0; i < this.shapes.size(); i++) {
            Shape child = this.shapes.get(i);
            
            
            Vector3 pi = child.getPose().transformTR(child.getCenterOfMass());
            
           
            Vector3 distance = pi.sub(this.center_mass);
            float distance2 = (distance.x * distance.x) + (distance.y * distance.y) + (distance.z * distance.z);
            
            
            Matrix3 term1 = Matrix3.diagonal(child.getMass() * distance2);
            Matrix3 term2 = Matrix3.outer(distance, child.getMass());
            Matrix3 steiner = term1.add(term2.mul(-1f));
            
            
            Matrix3 ri = child.getPose().getRotationMatrix();
            Matrix3 riT = ri.transpose();
            Matrix3 rotatedInertia = ri.mul(child.getLocalInertia()).mul(riT);
            
            Matrix3 inertiaTotalPeca = rotatedInertia.add(steiner);
            accumulatorInertia = accumulatorInertia.add(inertiaTotalPeca);
        }
        return accumulatorInertia;
    }
  public List<Shape> getShapes() {
        return this.shapes;
    }
}  
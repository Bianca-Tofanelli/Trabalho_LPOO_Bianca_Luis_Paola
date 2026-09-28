package lpoo.geom;

import lpoo.math.*;
import java.util.ArrayList;
import java.util.Arrays;
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
    }
}    
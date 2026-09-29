package lpoo.util;

import lpoo.geom.*;
import lpoo.phyx.*;
import java.io.*;
import java.util.*;

/**
 *
 * @author insert your name here
 */
public final class SceneReport
{
  public static void write(List<RigidBody> bodies, PrintWriter out)
  {
    for(RigidBody body : bodies)
    {
      out.println("Rigid Body description:");
      out.printf("Actor: %s\n", body.getName());
      out.printf("Area (Global): %.3f\n", body.getArea());
      out.printf("Volume (Global): %.3f\n", body.getVolume());
      out.printf("Mass (Global): %.3f\n", body.getMass());
      out.printf("Center of Mass (Global): %s\n", body.getCenterOfMass());

      if(body.getInertia() != null)
      {
        out.printf("Inertia Tensor (Global): %s\n", body.getInertia());
      }

      Bounds3 bounds = body.getBounds();
      if(bounds != null)
      {
        out.printf("AABB Min: %s\n", bounds.min());
        out.printf("AABB Max: %s\n", bounds.max());
      }

      out.println("\nShape description:");

      Deque<Shape> shapeStack = new ArrayDeque<>();
      Deque<String> indentStack = new ArrayDeque<>();
    
      if(body.getShape() != null)
      {
        shapeStack.push(body.getShape());
        indentStack.push("  ");
      }

      while(!shapeStack.isEmpty())
      {
        Shape currentShape = shapeStack.pop();
        String indent = indentStack.pop();
        String typeName = currentShape.getClass().getSimpleName();

        out.printf("%sShape: %s\n", indent, typeName);
        out.printf("%sName: %s\n", indent, currentShape.getName());
        out.printf("%sArea (Local): %.3f\n", indent, currentShape.getArea());
        out.printf("%sVolume (Local): %.3f\n", indent, currentShape.getVolume());
        out.printf("%sMass (Local): %.3f\n", indent, currentShape.getMass());
        out.printf("%sCenter of Mass (Local): %s\n", indent, currentShape.getCenterOfMass());
    
        if(currentShape.getLocalInertia() != null)
        {
          String matrixString = currentShape.getLocalInertia().toString();
          out.printf("%sInertia Tensor (Local): %s\n", indent, matrixString);
        }

        if(currentShape instanceof CompositeShape)
        {
          CompositeShape comp = (CompositeShape) currentShape;
          List<Shape> children = comp.getShapes();

          if(children != null)
          {
            for(int i = children.size() - 1; i >= 0; i--)
            {
              shapeStack.push(children.get(i));
              indentStack.push(indent + "    ");
            }
          }
        }
      }
      out.println();
    }
      out.flush();
  }
} // SceneReport

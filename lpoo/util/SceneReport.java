package lpoo.util;

import lpoo.geom.*;
import lpoo.phyx.*;
import java.io.*;
import java.util.*;

public final class SceneReport {
  
  public static void write(List<RigidBody> bodies, PrintWriter out) {
    for (RigidBody body : bodies) {
      out.printf("Ator: %s\n", body.getName());
      out.println("--- Propriedades Globais ---");
      out.printf("Área: %.3f | Volume: %.3f | Massa: %.3f\n", body.getArea(), body.getVolume(), body.getMass());
      out.printf("Centro de Massa: %s\n", body.getCenterOfMass());

      if (body.getInertia() != null) {
        out.printf("Tensor de Inércia: %s\n", body.getInertia().toString().replace("\n", "  "));
      }

      Bounds3 bounds = body.getBounds();
      if (bounds != null) {
        out.printf("Caixa Limitante: Mín %s | Máx %s\n", bounds.min(), bounds.max());
      }

      out.println("\n--- Geometria (Sistema Local) ---");

      Deque<Shape> shapeStack = new ArrayDeque<>();
      Deque<String> indentStack = new ArrayDeque<>();
    
      if (body.getShape() != null) {
        shapeStack.push(body.getShape());
        indentStack.push("");
      }

      while (!shapeStack.isEmpty()) {
        Shape currentShape = shapeStack.pop();
        String indent = indentStack.pop();
        String typeName = currentShape.getClass().getSimpleName();

        out.printf("%s > %s (%s)\n", indent, currentShape.getName(), typeName);
        out.printf("%s  Área: %.3f | Volume: %.3f | Massa: %.3f\n", indent, currentShape.getArea(), currentShape.getVolume(), currentShape.getMass());
        out.printf("%s  Centro de Massa: %s\n", indent, currentShape.getCenterOfMass());
    
        if (currentShape.getLocalInertia() != null) {
          out.printf("%s  Tensor de Inércia: %s\n", indent, currentShape.getLocalInertia().toString().replace("\n", "  "));
        }

        if (currentShape instanceof Mesh) {
          Mesh triangleMesh = (Mesh) currentShape;
          out.printf("%s  Vértices: %d | Triângulos: %d\n", indent, triangleMesh.getVertexCount(), triangleMesh.getTriangleCount());
        }

        if (currentShape instanceof CompositeShape) {
          CompositeShape comp = (CompositeShape) currentShape;
          List<Shape> children = comp.getShapes();

          if (children != null) {
            for (int i = children.size() - 1; i >= 0; i--) {
              shapeStack.push(children.get(i));
              indentStack.push(indent + "   ");
            }
          }
        }
      }
      out.println("\n======================================================\n");
    }
    out.flush();
  }
}
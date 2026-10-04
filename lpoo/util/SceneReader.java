package lpoo.util;

import lpoo.geom.*;
import lpoo.math.*;
import lpoo.phyx.*;
import java.io.*;
import java.util.*;

public final class SceneReader {
    
    public static Scene readScene(String filePath) throws FileNotFoundException {
        File file = new File(filePath);
        String sceneName = file.getName().replaceFirst("[.][^.]+$", ""); 
        Scene scene = new Scene(sceneName);

        try (Scanner sc = new Scanner(file)) {
            sc.useLocale(Locale.US); 
            SceneReader reader = new SceneReader(sc);
            List<RigidBody> bodies = reader.readFile();
            for(RigidBody body : bodies) {
                scene.addActor(body);
            }
        }
        return scene;
    }
    private final Scanner sc;
    private SceneReader(Scanner sc) {
        this.sc = sc;
    }
    private List<RigidBody> readFile() {
        List<RigidBody> actors = new ArrayList<>();
        Map<String, CompositeShape> compositeDictionary = new HashMap<>();
        while(sc.hasNext()) {
            String token = sc.next();

            if(token.equals("composite")) {
                String compositeName = sc.next();
                CompositeShape composite = new CompositeShape(compositeName);
                while(sc.hasNext()) {
                    String childType = sc.next();
                    if(childType.equals("end")) {
                        break;
                    }
                    Shape childShape = null;
                    if(childType.equals("box")) {
                        String name = sc.next();
                        float sx = sc.nextFloat();
                        float sy = sc.nextFloat();
                        float sz = sc.nextFloat();
                        float density = sc.nextFloat();
                        childShape = new Box(name, density, sx, sy, sz);
                    } 
                    else if(childType.equals("sphere")) {
                        String name = sc.next();
                        float radius = sc.nextFloat();
                        float density = sc.nextFloat();
                        childShape = new Sphere(name, density, radius);
                    } 
                    else if(childType.equals("cylinder")) {
                        String name = sc.next();
                        float radius = sc.nextFloat();
                        float halfHeight = sc.nextFloat();
                        float density = sc.nextFloat();
                        childShape = new Cylinder(name, density, radius, halfHeight);
                    }
                    else if(childType.equals("capsule")) {
                        String name = sc.next();
                        float radius = sc.nextFloat();
                        float halfHeight = sc.nextFloat();
                        float density = sc.nextFloat();
                        childShape = new Capsule(name, density, radius, halfHeight);
                    }
                    else if(childType.equals("instance")) {
                        String instanceName = sc.next(); 
                        String baseName = sc.next();
                        CompositeShape base = compositeDictionary.get(baseName);
                        if (base == null) {
                            throw new IllegalArgumentException("Forma composta não encontrada no dicionário: " + baseName);
                        }
                        childShape = new CompositeShapeInstance(instanceName, base);
                    }
                    else {
                        throw new IllegalArgumentException("Tipo de forma base desconhecida: " + childType);
                    }
                    if(sc.hasNext("pose")) {
                        sc.next();
                        Vector3 position = readVector3();
                        Quaternion orientation = readQuaternion();
                        childShape.setPose(new Pose(position, orientation));
                    }
                    if(childShape != null) {
                        composite.addShape(childShape);
                    }
                }
                if(sc.hasNext("pose")) {
                    sc.next();
                    Vector3 position = readVector3();
                    Quaternion orientation = readQuaternion();
                    composite.setPose(new Pose(position, orientation));
                }
                compositeDictionary.put(compositeName, composite);
            }

            else if(token.equals("mesh")) {
                String meshName = sc.next();
                String filename = sc.next();
                float density = sc.nextFloat();
                Shape meshShape = null;
                try{
                    TriangleMesh mesh = ObjReader.read(filename);

                    Vector3[] vertex = new Vector3[mesh.vertexCount()];
                    for(int i=0; i<mesh.vertexCount(); i++){
                        vertex[i] = mesh.vertex(i);
                    }
                    Index3[] triangles = new Index3[mesh.triangleCount()];
                    for(int i=0; i<mesh.triangleCount(); i++){
                        triangles[i] = mesh.triangle(i);
                    }

                    meshShape = new Mesh(meshName, density, triangles, vertex);
                }
                catch(IOException e) {
                    throw new RuntimeException("Falha ao ler arquivo OBJ: " + filename, e);
                }
                Pose meshPose = new Pose();
                if(sc.hasNext("pose")) {
                    sc.next();
                    Vector3 position = readVector3();
                    Quaternion orientation = readQuaternion();
                    meshPose = new Pose(position, orientation);
                }
                RigidBody body = new RigidBody(meshName, meshPose, meshShape);
                actors.add(body);
            }

            else if(token.equals("actor")) {
                String actorName = sc.next();
                String shapeType = sc.next();
                Shape shape = null;
                if(shapeType.equals("box")) {
                    String name = sc.next();
                    float sx = sc.nextFloat();
                    float sy = sc.nextFloat();
                    float sz = sc.nextFloat();
                    float density = sc.nextFloat();
                    shape = new Box(name, density, sx, sy, sz);
                } 
                else if(shapeType.equals("sphere")) {
                    String name = sc.next();
                    float radius = sc.nextFloat();
                    float density = sc.nextFloat();
                    shape = new Sphere(name, density, radius);
                } 
                else if(shapeType.equals("cylinder")) {
                    String name = sc.next();
                    float radius = sc.nextFloat();
                    float halfHeight = sc.nextFloat();
                    float density = sc.nextFloat();
                    shape = new Cylinder(name, density, radius, halfHeight);
                }
                else if(shapeType.equals("capsule")) {
                    String name = sc.next();
                    float radius = sc.nextFloat();
                    float halfHeight = sc.nextFloat();
                    float density = sc.nextFloat();
                    shape = new Capsule(name, density, radius, halfHeight);
                }
                else if(shapeType.equals("instance")) {
                    String instanceName = sc.next(); 
                    String baseName = sc.next();
                    CompositeShape base = compositeDictionary.get(baseName);
                    if (base == null) {
                        throw new IllegalArgumentException("Forma composta não encontrada no dicionário: " + baseName);
                    }
                    shape = new CompositeShapeInstance(instanceName, base);
                }
                else {
                    throw new IllegalArgumentException("Tipo de forma de ator desconhecida: " + shapeType);
                }

                Pose actorPose = new Pose();
                if(sc.hasNext("pose")) {
                    sc.next();
                    Vector3 position = readVector3();
                    Quaternion orientation = readQuaternion();
                    actorPose = new Pose(position, orientation);
                }
                RigidBody body = new RigidBody(actorName, actorPose, shape);
                actors.add(body);
            }
        } 
        return actors;
    }
    private Vector3 readVector3() {
        float x = sc.nextFloat();
        float y = sc.nextFloat();
        float z = sc.nextFloat();
        return new Vector3(x, y, z);
    }
    private Quaternion readQuaternion() {
        float x = sc.nextFloat();
        float y = sc.nextFloat();
        float z = sc.nextFloat();
        float w = sc.nextFloat();
        return new Quaternion(x, y, z, w);
    }
}

package lpoo.geom;
 
import lpoo.math.*;
 
/**
 *
 * @author Bianca Tofanelli
 *         Luis Cardoso
 *         Paola Vendruscolo 
 */
// Classe pública, "filha" de Primitive, da esfera
public class Sphere extends Primitive { 
    private float radius;
 
    // inicializa a esfera
    public Sphere(String name, float density, float radius) {
        super(name, density);

        if (radius <= 0) {
            throw new IllegalArgumentException("radius must be an positive non-zero number");
        }

        this.radius = radius;
        updateMassProperties();
    }
 
    // para pegar o raio 
    public float getRadius() {
        return radius;
    }

    // calcular o volume
    @Override
    public float getVolume() {
        return (4f / 3f) * (float) Math.PI * radius * radius * radius;
    }
 
    // calcular a área
    @Override
    public float getArea() {
        return 4f * (float) Math.PI * radius * radius;
    }
 
    // calcular o tensor de inércia
    @Override
    protected Matrix3 computeLocalInertia() {
        float m = getMass();
        float i = (2f / 5f) * m * radius * radius;
        return Matrix3.diagonal(i);
    }
 
    // para pegar a caixa limitante
    @Override
    public Bounds3 getBounds() {
        return new Bounds3(new Vector3(-radius, -radius, -radius),new Vector3(radius, radius, radius));
    }
} // Sphere
 
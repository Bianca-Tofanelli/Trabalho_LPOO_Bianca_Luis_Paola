package lpoo.geom;

import lpoo.math.*;

/**
 *
 * @author Bianca Tofanelli
 *         Luis Cardoso
 *         Paola Vendruscolo 
 */
/* Classe abstrata das formas primitivas
 */
public abstract class Primitive extends Shape {

    protected float density;

    // inicializa o primitivo
    public Primitive(String name, float density) {
        super(name);

        if (density <= 0) {
            throw new IllegalArgumentException("The density must be an positive non-zero number");}
        this.density = density;
    }

    // para pegar a densidade
    public float getDensity() {
        return density;
    }

    // calcula a massa e o tensor de inércia novos
    protected void updateMassProperties() {
        this.mass = density * getVolume();
        this.local_inertia = computeLocalInertia();
    }

    // método abstrato para as filhas calcularem seus próprios tensores de inércia
    protected abstract Matrix3 computeLocalInertia();
}// Primitive
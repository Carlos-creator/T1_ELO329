public class Equipo {

    //Clase base para los celulares, tablets, y EloTelTags

    public Equipo(String owner, float _x, float _y) {
        ownerName = owner;
        x=_x;
        y=_y;
    }
    public float getX() {
        return x;
    }
    public float getY() {
        return y;
    }

    public void move(float dx, float dy) {
        this.x += dx;
        this.y += dy;
    }

    public String getOwnerName() {
        return ownerName;
    }

    protected final String ownerName;
    protected float x,y;
}

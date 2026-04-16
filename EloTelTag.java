public class EloTelTag {
    protected String name;
    protected String owner_name;
    protected float x;
    protected float y;

    public EloTelTag(String owner_name, String name, float x, float y) {
        this.owner_name = owner_name;
        this.name       = name;
        this.x          = x;
        this.y          = y;
    }

    public void move(float dx, float dy) {
        this.x += dx;
        this.y += dy;
    }

    public String getName()      { return name; }
    public String getOwnerName() { return owner_name; }
    public float  getX()         { return x; }
    public float  getY()         { return y; }
}
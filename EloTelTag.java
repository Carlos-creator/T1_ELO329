public class EloTelTag {
    protected String name;
    protected String owner_name;
    protected float x;
    protected float y;

    // Constructor matches Stage 2: EloTelTag(personName, tagName, x, y)
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
}

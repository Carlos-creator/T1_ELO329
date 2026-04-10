public class EloTelTag {
    protected String name;
    protected String owner_name;
    public double x;
    public double y;

    public EloTelTag(String name, String owner_name, double x, double y) {
        this.name = name;
        this.owner_name = owner_name;
        this.x = x;
        this.y = y;
    }

    // Method to update the tag position
    public void move(double dx, double dy) {
        this.x += dx;
        this.y += dy;
    }
}
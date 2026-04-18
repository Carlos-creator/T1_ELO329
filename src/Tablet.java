public class Tablet extends EloTelTag {
    public Tablet(String ownerName, float x, float y, ETNube nube) {
        super(ownerName, "tablet", x, y);
        viewer = new Viewer(ownerName, nube);
    }

    public void findMy() {
        viewer.show();
    }

    private Viewer viewer;
}

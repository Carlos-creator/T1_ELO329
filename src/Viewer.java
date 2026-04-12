public class Viewer {
    private String ownerName;
    private ETNube nube;

    public Viewer(String ownerName, ETNube nube) {
        this.ownerName = ownerName;
        this.nube = nube;
    }

    public void show() {
        nube.showOwnerLocations(ownerName);
    }
}
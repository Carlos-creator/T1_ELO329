import java.util.ArrayList;

public class Viewer {

    private String ownerName;

    public Viewer(String ownerName) {
        this.ownerName = ownerName;
    }

    public void show(ArrayList<EloTelTag> tags) {
        System.out.println("=== FindMy de " + ownerName + " ===");

        boolean found = false;

        for (EloTelTag tag : tags) {
            if (tag.owner_name.equals(ownerName)) {
                System.out.println(tag.owner_name + "." + tag.name +
                                   " x:" + tag.x + " y:" + tag.y);
                found = true;
            }
        }

        if (!found) {
            System.out.println("No hay equipos para " + ownerName);
        }
    }
}
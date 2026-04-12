import java.util.ArrayList;
import java.awt.geom.Point2D;
import java.io.PrintStream;

public class ETNube {
    public ETNube() {
        cloudData = new ArrayList<Data>();
    }

    public void updateLocation(String owner, String equipment, float x, float y) {
        Point2D location;
        if ((location = getLocation(owner, equipment)) == null) {
            location = new Point2D.Float(x, y);
            Data data = new Data(owner, equipment, location);
            cloudData.add(data);
        }
        location.setLocation(x, y);
    }

    public Point2D getLocation(String owner, String equipment) {
        for (Data data : cloudData) {
            if (data.ownerName.equals(owner) && data.equipmentName.equals(equipment)) {
                return data.location;
            }
        }
        return null;
    }

    public void showOwnerLocations(String owner) {
        System.out.println("Bienes de " + owner + ":");
        for (Data data : cloudData) {
            if (data.ownerName.equals(owner)) {
                System.out.printf("%s.%s: %.1f, %.1f%n",
                        data.ownerName,
                        data.equipmentName,
                        data.location.getX(),
                        data.location.getY());
            }
        }
    }

    public void printHeader(PrintStream output) {
        output.printf("Step\t");
        for (Data data : cloudData) {
            output.printf("%s.%s.x\t.y\t", data.ownerName, data.equipmentName);
        }
    }

    public void printState(PrintStream output, int step) {
        output.printf("%n%d", step);
        for (Data data : cloudData) {
            output.printf("\t%.1f\t%.1f", data.location.getX(), data.location.getY());
        }
    }

    private ArrayList<Data> cloudData;

    private static class Data {
        public Data(String owner, String equipment, Point2D loc) {
            ownerName = owner;
            equipmentName = equipment;
            location = loc;
        }

        public Point2D location;
        public String ownerName, equipmentName;
    }
}
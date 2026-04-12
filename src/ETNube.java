import java.util.ArrayList;
import java.awt.geom.Point2D;
import java.io.PrintStream;

public class ETNube{
    public ETNube(){
        cloudData = new ArrayList<Data>();
    }


    public void updateLocation(String owner, String equipment, float x, float y){

        //Actualiza la ubicacion del equipo, si no hay datos los crea

        Point2D location;
        if ((location = getLocation(owner, equipment))== null){
            location = new Point2D.Float(x, y);
            Data data = new Data(owner, equipment, location);
            cloudData.add(data);
        }
        location.setLocation(x, y);
    }

    public Point2D getLocation(String owner, String equipment){
        for (Data data : cloudData){
            if (data.ownerName == owner && data.equipmentName ==  equipment){
                return data.location;
            }
        }
        return null;
    }

    public void printHeader(PrintStream output){
        //Imprime la cabecera de la tabla de datos
        output.printf("Step\t");

        for (Data data : cloudData)
            output.printf("%s.%s.x .y\t", data.ownerName, data.equipmentName);
    }

    public void printState(PrintStream output, int step){

        //Imprime la ubicación actual de cada equipo, se llama desde T1Stage2 runSimulation() cada vez que se mueve un equipo desde move.txt

        output.printf("\n%d", step);
        for (Data data : cloudData){
            output.printf("\t%.1f\t%.1f", data.location.getX(), data.location.getY());
        }
    }

    private ArrayList<Data> cloudData;

    private static class Data{
        public Data(String owner, String equipment, Point2D loc){
            ownerName = owner;
            equipmentName = equipment;
            location = loc;
        }
        public Point2D location;
        public String ownerName, equipmentName;
    }
}
import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.util.Scanner;

//Este es el codigo base para la stage 2 entregado por el profe :P

public class T1Stage2 {
    T1Stage2() {
        territory = new Territory();
        nube = new ETNube();
    }
    public static void main (String args[]) throws IOException {
                                // throws exception to avoid catching
                                // exception in the program
        if (args.length != 2) {
            System.out.println("Usage: java T1Stage2 <configFile> <moveFile>");
            System.exit(-1);
        }
        Scanner confFile = new Scanner(new File(args[0]));
        Scanner movFile = new Scanner(new File(args[1]));
        T1Stage2 stage = new T1Stage2();
        stage.setupSimulator(confFile);  // read configuration file and create objects
        stage.runSimulation(movFile, System.out); // execute file's instructions
    }
    public void setupSimulator(Scanner in) {  // create objects from file
        int personNumber = in.nextInt();
        for (int i = 0; i < personNumber; i++)
            setupPersonEquipment(in);
    }
    private void setupPersonEquipment(Scanner in){
        Cellular cellular;
        float x, y;

        String personName = in.next();
        int tagNumber = in.nextInt();
        boolean isThereTablet= in.nextInt()==1;
        x = in.nextFloat(); // cellular's location
        y = in.nextFloat();
        cellular = new Cellular(personName, x, y, nube);
        territory.addCellular(cellular);
        nube.updateLocation(personName, "celular", x, y);
        for (int j = 0; j < tagNumber; j++)
           setupEloTags(in, personName);
        if (isThereTablet) {
            in.nextFloat(); in.nextFloat();  // skip tablet's location
        }
    }
    private void setupEloTags(Scanner in, String personName) {
        EloTelTag tag;
        float x, y;
        String tagName = in.next();
        x = in.nextFloat();
        y = in.nextFloat();
        tag = new EloTelTag(personName,tagName,x, y);
        territory.addTag(tag);
        nube.updateLocation(tag.getOwnerName(),tag.getName(), x, y);
    }
    public void runSimulation(Scanner in, PrintStream output) {
        nube.printHeader(output); // in this stage, print cloud's state
        nube.printState(output,step);
        while (in.hasNextLine()) {
            step++;
            String equipment = in.next(); // read person'a name . equipment's name
            String[] parts = equipment.split("\\.");
            String personName = parts[0];
            String equipmentName = parts[1];
            float deltaX = in.nextFloat();
            float deltaY = in.nextFloat();


            //REMOVE THIS WHEN TABLETS ARE IMPLEMENTED :)
            if (equipmentName.equals("tablet")) continue;


            if (equipmentName.equals("celular"))
                territory.getCellular(personName).move(deltaX,deltaY);
            else {  // in this stage, it must be a tag
                EloTelTag tag = territory.getTag(personName, equipmentName); // find this user's tag
                tag.move(deltaX, deltaY);
            }
            territory.forEachTagTryToReportLocation();
            nube.printState(output, step);
        }
    }
    private int step=0;
    private Territory territory;  // it knows all the equipments and checks cellular nearby tags.
    private ETNube nube;
}

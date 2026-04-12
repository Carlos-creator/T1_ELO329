import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.util.Locale;
import java.util.Scanner;

public class T1Stage3 {
    T1Stage3() {
        territory = new Territory();
        nube = new ETNube();
    }

    public static void main(String args[]) throws IOException {
        if (args.length != 2) {
            System.out.println("Usage: java T1Stage3 <configFile> <moveFile>");
            System.exit(-1);
        }

        Scanner confFile = new Scanner(new File(args[0])).useLocale(Locale.US);
        Scanner movFile = new Scanner(new File(args[1])).useLocale(Locale.US);

        T1Stage3 stage = new T1Stage3();
        stage.setupSimulator(confFile);
        stage.runSimulation(movFile, System.out);
    }

    public void setupSimulator(Scanner in) {
        int personNumber = in.nextInt();
        for (int i = 0; i < personNumber; i++) {
            setupPersonEquipment(in);
        }
    }

    private void setupPersonEquipment(Scanner in) {
        Cellular cellular;
        float x, y;

        String personName = in.next();
        int tagNumber = in.nextInt();
        boolean isThereTablet = in.nextInt() == 1;

        x = in.nextFloat();
        y = in.nextFloat();

        cellular = new Cellular(personName, x, y, nube);
        territory.addCellular(cellular);
        nube.updateLocation(personName, "celular", x, y);

        for (int j = 0; j < tagNumber; j++) {
            setupEloTags(in, personName);
        }

        if (isThereTablet) {
            float tabletX = in.nextFloat();
            float tabletY = in.nextFloat();
            nube.updateLocation(personName, "tablet", tabletX, tabletY);
        }
    }

    private void setupEloTags(Scanner in, String personName) {
        float x, y;
        String tagName = in.next();
        x = in.nextFloat();
        y = in.nextFloat();

        EloTelTag tag = new EloTelTag(personName, tagName, x, y);
        territory.addTag(tag);
        nube.updateLocation(tag.getOwnerName(), tag.getName(), x, y);
    }

    public void runSimulation(Scanner in, PrintStream output) {
        nube.printHeader(output);
        nube.printState(output, step);

        while (in.hasNext()) {
            String equipment = in.next();
            String[] parts = equipment.split("\\.");

            if (parts.length != 2) {
                if (in.hasNextLine()) {
                    in.nextLine();
                }
                continue;
            }

            String personName = parts[0];
            String equipmentName = parts[1];

            if (!in.hasNext()) {
                break;
            }

            String nextToken = in.next();

            if (nextToken.equals("FindMy")) {
                System.out.println();
                Viewer viewer = new Viewer(personName, nube);
                viewer.show();
                System.out.println();
                continue;
            }

            float deltaX;
            float deltaY;

            try {
                deltaX = Float.parseFloat(nextToken);
                if (!in.hasNextFloat()) {
                    continue;
                }
                deltaY = in.nextFloat();
            } catch (NumberFormatException e) {
                continue;
            }

            step++;

            if (equipmentName.equals("tablet")) {
                nube.printState(output, step);
                continue;
            }

            if (equipmentName.equals("celular")) {
                territory.getCellular(personName).move(deltaX, deltaY);
            } else {
                EloTelTag tag = territory.getTag(personName, equipmentName);
                tag.move(deltaX, deltaY);
            }

            territory.forEachTagTryToReportLocation();
            nube.printState(output, step);
        }
    }

    private int step = 0;
    private Territory territory;
    private ETNube nube;
}
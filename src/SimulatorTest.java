import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.util.Locale;
import java.util.Scanner;

public class SimulatorTest {
    SimulatorTest() {
        territory = new Territory();
        nube = new ETNube();
    }

    public static void main(String args[]) throws IOException {
        if (args.length != 2) {
            System.out.println("Usage: java SimulatorTest <configFile> <moveFile>");
            System.exit(-1);
        }

        Scanner confFile = new Scanner(new File(args[0])).useLocale(Locale.US);
        Scanner movFile = new Scanner(new File(args[1])).useLocale(Locale.US);

        SimulatorTest sim = new SimulatorTest();
        sim.setupSimulator(confFile);

        PrintStream output = new PrintStream(new File("output.csv"));
        sim.runSimulation(movFile, output);
        output.close();
    }

    public void setupSimulator(Scanner in) {
        int personNumber = in.nextInt();
        for (int i = 0; i < personNumber; i++) {
            setupPersonEquipment(in);
        }
    }

    private void setupPersonEquipment(Scanner in) {
        String personName = in.next();
        int tagNumber = in.nextInt();
        boolean isThereTablet = in.nextInt() == 1;

        float x = in.nextFloat();
        float y = in.nextFloat();

        Cellular cellular = new Cellular(personName, x, y, nube);
        territory.addCellular(cellular);
        nube.updateLocation(personName, "celular", x, y);

        for (int j = 0; j < tagNumber; j++) {
            setupEloTags(in, personName);
        }

        if (isThereTablet) {
            float tabletX = in.nextFloat();
            float tabletY = in.nextFloat();
            Tablet tablet = new Tablet(personName, tabletX, tabletY, nube);
            territory.addTablet(tablet);
            nube.updateLocation(personName, "tablet", tabletX, tabletY);
        }
    }

    private void setupEloTags(Scanner in, String personName) {
        String tagName = in.next();
        float x = in.nextFloat();
        float y = in.nextFloat();

        EloTelTag tag = new EloTelTag(personName, tagName, x, y);
        territory.addTag(tag);
        nube.updateLocation(personName, tagName, x, y);
    }

    public void runSimulation(Scanner in, PrintStream output) {
        nube.printHeader(output);
        nube.printState(output, step);

        while (in.hasNext()) {
            String equipment = in.next();
            String[] parts = equipment.split("\\.");

            if (parts.length != 2) {
                if (in.hasNextLine()) in.nextLine();
                continue;
            }

            String personName = parts[0];
            String equipmentName = parts[1];

            if (!in.hasNext()) break;

            String nextToken = in.next();

            if (nextToken.equals("FindMy")) {
                System.out.println();
                new Viewer(personName, nube).show();
                System.out.println();
                continue;
            }

            float deltaX;
            float deltaY;
            try {
                deltaX = Float.parseFloat(nextToken);
                if (!in.hasNextFloat()) continue;
                deltaY = in.nextFloat();
            } catch (NumberFormatException e) {
                continue;
            }

            step++;

            if (equipmentName.equals("celular")) {
                Cellular cel = territory.getCellular(personName);
                cel.move(deltaX, deltaY);
                nube.updateLocation(personName, "celular", cel.getX(), cel.getY());
            } else if (equipmentName.equals("tablet")) {
                Tablet tablet = territory.getTablet(personName);
                if (tablet != null) tablet.move(deltaX, deltaY);
            } else {
                EloTelTag tag = territory.getTag(personName, equipmentName);
                if (tag != null) tag.move(deltaX, deltaY);
            }

            territory.forEachTagTryToReportLocation();
            nube.printState(output, step);
        }
    }

    private int step = 0;
    private Territory territory;
    private ETNube nube;
}

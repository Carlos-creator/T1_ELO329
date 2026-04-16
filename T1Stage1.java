import java.util.ArrayList;
import java.util.Scanner;
import java.io.File;
import java.io.IOException;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.io.PrintStream;

public class T1Stage1 {
    private ArrayList<EloTelTag> tags = new ArrayList<>();
    private PrintWriter csvWriter;

    public static void main(String[] args) throws IOException {
        if (args.length != 2) {
            System.out.println("Use: java T1Stage1 config.txt move.txt");
            System.exit(-1);
        }

        Scanner confScanner = new Scanner(new File(args[0]));
        Scanner moveScanner = new Scanner(new File(args[1]));

        T1Stage1 stage = new T1Stage1();
        stage.setupSimulator(confScanner);
        stage.runSimulation(moveScanner, System.out);

        confScanner.close();
        moveScanner.close();
    }

    public void setupSimulator(Scanner in) {
        int personNumber = in.nextInt();
        for (int i = 0; i < personNumber; i++) {
            setupPersonEquipment(in);
        }
    }

    private void setupPersonEquipment(Scanner in) {
        String personName = in.next();
        int numTags       = in.nextInt();
        int hasTabletFlag = in.nextInt();

        float cx = in.nextFloat();
        float cy = in.nextFloat();
        tags.add(new EloTelTag(personName, "celular", cx, cy));

        for (int j = 0; j < numTags; j++) {
            String tagName = in.next();
            float x = in.nextFloat();
            float y = in.nextFloat();
            tags.add(new EloTelTag(personName, tagName, x, y));
        }

        if (hasTabletFlag == 1) {
            in.nextFloat();
            in.nextFloat();
        }
    }

    public void runSimulation(Scanner in, PrintStream output) {
        try {
            csvWriter = new PrintWriter(new FileWriter("output.csv"));
        } catch (IOException e) {
            System.out.println("Error creating output.csv: " + e.getMessage());
            return;
        }

        printHeader(output);
        printState(output, 0);

        int step = 1;
        while (in.hasNext()) {
            String target    = in.next();
            String[] parts   = target.split("\\.");
            String ownerName = parts[0];
            String equipName = parts[1];

            if (equipName.equals("tablet")) {
                in.nextLine();
                continue;
            }

            String token = in.next();
            if (token.equalsIgnoreCase("FindMy")) {
                continue;
            }

            float dx = Float.parseFloat(token);
            float dy = in.nextFloat();

            for (EloTelTag tag : tags) {
                if (tag.getOwnerName().equals(ownerName) && tag.getName().equals(equipName)) {
                    tag.move(dx, dy);
                }
            }

            printState(output, step++);
        }
        csvWriter.close();
    }

    private void printHeader(PrintStream output) {
        StringBuilder sb = new StringBuilder("Step");
        for (EloTelTag tag : tags) {
            sb.append("\t").append(tag.getOwnerName()).append(".").append(tag.getName()).append(".x");
            sb.append("\t").append(".y");
        }
        output.println(sb.toString());
        csvWriter.println(sb.toString());
    }

    private void printState(PrintStream output, int step) {
        StringBuilder sb = new StringBuilder(String.valueOf(step));
        for (EloTelTag tag : tags) {
            sb.append("\t").append(tag.getX()).append("\t").append(tag.getY());
        }
        output.println(sb.toString());
        csvWriter.println(sb.toString());
    }
}
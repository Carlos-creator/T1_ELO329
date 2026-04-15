import java.util.ArrayList;
import java.util.Scanner;
import java.io.File;
import java.io.IOException;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.io.PrintStream;

/**
 * Stage 1: Reads config and move files, tracks EloTelTag positions,
 * and writes output.csv.
 *
 * Design mirrors Stage 2's structure so Stage 2 is a natural extension:
 *   - setupSimulator(Scanner)       -> same signature as Stage 2
 *   - runSimulation(Scanner, PrintStream) -> same signature as Stage 2
 *   - setupPersonEquipment(Scanner) -> same private helper as Stage 2
 *   - setupEloTags(Scanner, String) -> same private helper as Stage 2
 *
 * Key differences vs Stage 2 (intentional for Stage 1 scope):
 *   - No Territory, no ETNube, no Cellular: positions tracked directly here.
 *   - Output is output.csv showing raw tag coordinates after each command.
 *   - Celular and Tablet lines are skipped (not yet modeled).
 */
public class T1Stage1 {

    // All tags discovered during setup
    private ArrayList<EloTelTag> tags = new ArrayList<>();

    // CSV writer (opened before runSimulation, closed after)
    private PrintWriter csvWriter;

    // ---------------------------------------------------------------
    // Entry point
    // ---------------------------------------------------------------
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

    // ---------------------------------------------------------------
    // Setup  (same signature as Stage 2)
    // ---------------------------------------------------------------

    /** Reads the configuration file and creates all EloTelTag objects. */
    public void setupSimulator(Scanner in) {
        int personNumber = in.nextInt();
        for (int i = 0; i < personNumber; i++) {
            setupPersonEquipment(in);
        }
    }

    /**
     * Reads one person's block from the config file.
     * Format:
     *   <name> <numTags> <hasTablet>
     *   <celular_x> <celular_y>
     *   (<tagName> <x> <y>)*     <- all on one line
     *   [<tablet_x> <tablet_y>]  <- only if hasTablet == 1
     */
    private void setupPersonEquipment(Scanner in) {
        String personName  = in.next();
        int    numTags     = in.nextInt();
        boolean hasTablet  = in.nextInt() == 1;

        // Skip celular position (not modeled in Stage 1)
        in.nextFloat();
        in.nextFloat();

        // Read all tags for this person
        for (int j = 0; j < numTags; j++) {
            setupEloTags(in, personName);
        }

        // Skip tablet position if present (not modeled in Stage 1)
        if (hasTablet) {
            in.nextFloat();
            in.nextFloat();
        }
    }

    /**
     * Reads one EloTelTag entry and adds it to the tag list.
     * Same signature as Stage 2 so Stage 2 can reuse/override this method.
     */
    private void setupEloTags(Scanner in, String personName) {
        String tagName = in.next();
        float  x       = in.nextFloat();
        float  y       = in.nextFloat();

        // Constructor order: (ownerName, tagName, x, y) - matches Stage 2
        EloTelTag tag = new EloTelTag(personName, tagName, x, y);
        tags.add(tag);
    }

    // ---------------------------------------------------------------
    // Simulation  (same signature as Stage 2)
    // ---------------------------------------------------------------

    /**
     * Processes move commands and writes output.csv.
     * In Stage 1 positions are tracked directly (no ETNube involved).
     * In Stage 2 this method will delegate reporting to ETNube.
     */
    public void runSimulation(Scanner in, PrintStream output) {
        // Open CSV output file
        try {
            csvWriter = new PrintWriter(new FileWriter("output.csv"));
        } catch (IOException e) {
            System.out.println("Error creating output.csv: " + e.getMessage());
            return;
        }

        // Write CSV header and initial state (step 0)
        printHeader(output);
        printState(output, 0);

        int step = 1;
        while (in.hasNext()) {
            // Read "ownerName.equipmentName  dx  dy"
            String equipment = in.next();
            String[] parts   = equipment.split("\\.");
            String ownerName = parts[0];
            String equipName = parts[1];

            // In Stage 1, skip celular/tablet commands (not modeled yet)
            if (equipName.equals("celular") || equipName.equals("tablet")) {
                // Still need to consume the rest of this line's tokens
                // Check whether this line has dx/dy or a FindMy command
                String token = in.next();
                if (!token.equalsIgnoreCase("FindMy")) {
                    in.nextFloat(); // consume dy
                }
                continue;
            }

            // Skip FindMy commands (not modeled in Stage 1)
            String token = in.next();
            if (token.equalsIgnoreCase("FindMy")) {
                continue;
            }

            // token is actually dx; read dy normally
            float dx = Float.parseFloat(token);
            float dy = in.nextFloat();

            // Find the matching tag and move it
            for (EloTelTag tag : tags) {
                if (tag.getOwnerName().equals(ownerName) &&
                    tag.getName().equals(equipName)) {
                    tag.move(dx, dy);
                }
            }

            // Write state after this command
            printState(output, step);
            step++;
        }

        csvWriter.close();
        System.out.println("output.csv generated successfully.");
    }

    // ---------------------------------------------------------------
    // Output helpers
    // (named printHeader / printState to match ETNube's API in Stage 2,
    //  so the migration is straightforward)
    // ---------------------------------------------------------------

    /**
     * Prints the CSV header row.
     * In Stage 2 this responsibility moves to ETNube.printHeader().
     */
    private void printHeader(PrintStream output) {
        StringBuilder header = new StringBuilder("Step");
        for (EloTelTag tag : tags) {
            header.append("\t").append(tag.getOwnerName()).append(".")
                  .append(tag.getName()).append(".x");
            header.append("\t").append(".y");
        }
        String line = header.toString();
        output.println(line);       // console (optional, useful for debugging)
        csvWriter.println(line);    // CSV file
    }

    /**
     * Prints one data row with the current position of every tag.
     * In Stage 2 this responsibility moves to ETNube.printState().
     */
    private void printState(PrintStream output, int step) {
        StringBuilder row = new StringBuilder(String.valueOf(step));
        for (EloTelTag tag : tags) {
            row.append("\t").append(tag.getX());
            row.append("\t").append(tag.getY());
        }
        String line = row.toString();
        output.println(line);
        csvWriter.println(line);
    }
}

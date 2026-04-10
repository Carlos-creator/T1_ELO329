import java.util.ArrayList;
import java.util.Scanner;
import java.io.File;
import java.io.IOException;
import java.io.FileWriter;
import java.io.PrintWriter;

public class T1Stage1 {

    // Write the header of the CSV file
    public static void writeHeader(PrintWriter pw, ArrayList<EloTelTag> tags) {
        StringBuilder header = new StringBuilder();
        header.append("Step");
        for (EloTelTag tag : tags) {
            header.append("\t").append(tag.owner_name + "." + tag.name + ".x");
            header.append("\t").append(".y");
        }
        pw.println(header.toString());
    }

    // Write a row with the current positions of all tags
    public static void writeRow(PrintWriter pw, int step, ArrayList<EloTelTag> tags) {
        StringBuilder row = new StringBuilder();
        row.append(step);
        for (EloTelTag tag : tags) {
            row.append("\t").append(tag.x);
            row.append("\t").append(tag.y);
        }
        pw.println(row.toString());
    }

    public static void main(String[] args) {

        // Check arguments
        if (args.length < 2) {
            System.out.println("Use: java T1Stage1 config.txt move.txt");
            return;
        }

        String file_config = args[0];
        ArrayList<String> buffer_file_config = new ArrayList<>();
        String file_move = args[1];
        ArrayList<String> buffer_file_move = new ArrayList<>();

        // Read and save config file
        try {
            Scanner configScanner = new Scanner(new File(file_config));
            while (configScanner.hasNextLine()) {
                buffer_file_config.add(configScanner.nextLine());
            }
            configScanner.close();
        } catch (IOException e) {
            System.out.println("Error reading config file: " + e.getMessage());
            return;
        }

        // Read and save move file
        try {
            Scanner moveScanner = new Scanner(new File(file_move));
            while (moveScanner.hasNextLine()) {
                buffer_file_move.add(moveScanner.nextLine());
            }
            moveScanner.close();
        } catch (IOException e) {
            System.out.println("Error reading move file: " + e.getMessage());
            return;
        }

        // Create EloTelTag objects from config file
        ArrayList<EloTelTag> tags = new ArrayList<>();
        int i = 0;
        int numPeople = Integer.parseInt(buffer_file_config.get(i++));

        for (int p = 0; p < numPeople; p++) {

            // Read person information: name, numTags, hasTablet
            Scanner personLine = new Scanner(buffer_file_config.get(i++));
            String ownerName   = personLine.next();
            int numTags        = personLine.nextInt();
            int hasTablet      = personLine.nextInt();
            personLine.close();

            // Skip phone position line
            i++;

            // Read tags from the same line
            Scanner tagLine = new Scanner(buffer_file_config.get(i++));
            for (int t = 0; t < numTags; t++) {
                String tagName = tagLine.next();
                double tagX    = tagLine.nextDouble();
                double tagY    = tagLine.nextDouble();

                // Create EloTelTag object and add to list
                EloTelTag tag = new EloTelTag(tagName, ownerName, tagX, tagY);
                tags.add(tag);
            }
            tagLine.close();

            // Skip tablet position line if exists
            if (hasTablet == 1) {
                i++;
            }
        }

        // Create output CSV file
        PrintWriter pw = null;
        try {
            pw = new PrintWriter(new FileWriter("output.csv"));
        } catch (IOException e) {
            System.out.println("Error creating output.csv: " + e.getMessage());
            return;
        }

        // Write CSV header
        writeHeader(pw, tags);

        // Print and write initial positions (step 0)
        System.out.println("=== Initial positions (step 0) ===");
        for (EloTelTag tag : tags) {
            System.out.println(tag.owner_name + "." + tag.name +
                               " x:" + tag.x + " y:" + tag.y);
        }
        writeRow(pw, 0, tags);

        // Process move commands
        int step = 1;
        for (String moveLine : buffer_file_move) {

            Scanner lineMove = new Scanner(moveLine);
            String object    = lineMove.next();
            String[] parts   = object.split("\\.");
            String ownerName = parts[0];
            String tagName   = parts[1];
            double dx        = lineMove.nextDouble();
            double dy        = lineMove.nextDouble();
            lineMove.close();

            // Find tag and update position
            for (EloTelTag tag : tags) {
                if (tag.owner_name.equals(ownerName) && tag.name.equals(tagName)) {
                    tag.move(dx, dy);
                    System.out.println("Step " + step + " - Updated " +
                                       tag.owner_name + "." + tag.name +
                                       " x:" + tag.x + " y:" + tag.y);
                }
            }

            // Write current positions of ALL tags after each command
            writeRow(pw, step, tags);
            step++;
        }

        // Close CSV file
        pw.close();
        System.out.println("output.csv generated successfully");
    }
}
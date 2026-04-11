import java.util.ArrayList;
import java.util.Scanner;
import java.util.Locale;
import java.io.File;
import java.io.IOException;

public class T1Stage3 {

    public static void main(String[] args) {

        if (args.length < 2) {
            System.out.println("Use: java T1Stage3 config.txt move.txt");
            return;
        }

        String fileConfig = args[0];
        String fileMove = args[1];

        ArrayList<String> bufferConfig = new ArrayList<>();
        ArrayList<String> bufferMove = new ArrayList<>();

        try {
            Scanner configScanner = new Scanner(new File(fileConfig)).useLocale(Locale.US);
            while (configScanner.hasNextLine()) {
                bufferConfig.add(configScanner.nextLine());
            }
            configScanner.close();
        } catch (IOException e) {
            System.out.println("Error reading config file: " + e.getMessage());
            return;
        }

        try {
            Scanner moveScanner = new Scanner(new File(fileMove)).useLocale(Locale.US);
            while (moveScanner.hasNextLine()) {
                bufferMove.add(moveScanner.nextLine());
            }
            moveScanner.close();
        } catch (IOException e) {
            System.out.println("Error reading move file: " + e.getMessage());
            return;
        }

        ArrayList<EloTelTag> tags = new ArrayList<>();
        int i = 0;
        int numPeople = Integer.parseInt(bufferConfig.get(i++));

        for (int p = 0; p < numPeople; p++) {
            Scanner personLine = new Scanner(bufferConfig.get(i++)).useLocale(Locale.US);
            String ownerName = personLine.next();
            int numTags = personLine.nextInt();
            int hasTablet = personLine.nextInt();
            personLine.close();

            i++; // saltar línea del celular

            Scanner tagLine = new Scanner(bufferConfig.get(i++)).useLocale(Locale.US);
            for (int t = 0; t < numTags; t++) {
                String tagName = tagLine.next();
                double tagX = tagLine.nextDouble();
                double tagY = tagLine.nextDouble();

                tags.add(new EloTelTag(tagName, ownerName, tagX, tagY));
            }
            tagLine.close();

            if (hasTablet == 1) {
                i++; // saltar línea de la tablet
            }
        }

        for (String moveLine : bufferMove) {
            if (moveLine.trim().isEmpty()) {
                continue;
            }

            Scanner lineMove = new Scanner(moveLine).useLocale(Locale.US);

            if (!lineMove.hasNext()) {
                lineMove.close();
                continue;
            }

            String object = lineMove.next();
            String[] parts = object.split("\\.");

            if (parts.length != 2) {
                lineMove.close();
                continue;
            }

            String ownerName = parts[0];
            String objectName = parts[1];

            if (!lineMove.hasNext()) {
                lineMove.close();
                continue;
            }

            String action = lineMove.next();

            if (action.equals("FindMy")) {
                Viewer viewer = new Viewer(ownerName);
                viewer.show(tags);
                lineMove.close();
                continue;
            }

            // Ignorar movimientos de celular y tablet por ahora
            if (objectName.equals("celular") || objectName.equals("tablet")) {
                lineMove.close();
                continue;
            }

            double dx;
            double dy;

            try {
                dx = Double.parseDouble(action);
                if (!lineMove.hasNextDouble()) {
                    lineMove.close();
                    continue;
                }
                dy = lineMove.nextDouble();
            } catch (NumberFormatException e) {
                lineMove.close();
                continue;
            }

            for (EloTelTag tag : tags) {
                if (tag.owner_name.equals(ownerName) && tag.name.equals(objectName)) {
                    tag.move(dx, dy);
                    System.out.println("Updated " + tag.owner_name + "." + tag.name +
                                       " x:" + tag.x + " y:" + tag.y);
                }
            }

            lineMove.close();
        }
    }
}
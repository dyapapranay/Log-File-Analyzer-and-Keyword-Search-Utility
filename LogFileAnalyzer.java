import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;

class project {
    int lineNo;
    String date;
    String time;
    String level;
    String message;

    project(int lineNo, String date, String time,
             String level, String message) {
        this.lineNo = lineNo;
        this.date = date;
        this.time = time;
        this.level = level;
        this.message = message;
    }

    void display() {
        System.out.println("Line No : " + lineNo);
        System.out.println("Date    : " + date);
        System.out.println("Time    : " + time);
        System.out.println("Level   : " + level);
        System.out.println("Message : " + message);
        System.out.println("--------------------");
    }
}

public class LogFileAnalyzer {
    static ArrayList<project> entries = new ArrayList<>();
    static final String FILE = "server.log";

    static void loadLog() {
        File file = new File(FILE);
        if (!file.exists()) {
            System.out.println("Log file not found.");
            return;
        }
        entries.clear();
        try (BufferedReader reader =
                new BufferedReader(new FileReader(file))) {
            String line;
            int lineNo = 0;
            while ((line = reader.readLine()) != null) {
                lineNo++;
                String[] parts = line.split(" ", 4);
                if (parts.length < 4) {
                    continue;
                }
                entries.add(new project(lineNo, parts[0],
                        parts[1], parts[2], parts[3]));
            }
            System.out.println("Log file loaded successfully.");
        } catch (IOException e) {
            System.out.println("Error reading log file.");
        }
    }

    static void analyzeLog() {
        if (entries.isEmpty()) {
            System.out.println("No log entries found.");
            return;
        }
        HashMap<String, Integer> counts = new HashMap<>();
        counts.put("INFO", 0);
        counts.put("WARNING", 0);
        counts.put("ERROR", 0);
        for (project e : entries) {
            if (counts.containsKey(e.level)) {
                counts.put(e.level, counts.get(e.level) + 1);
            }
        }
        int total = entries.size();
        System.out.println("LOG SUMMARY");
        System.out.println("Total entries : " + total);
        for (String level : new String[]{"INFO", "WARNING", "ERROR"}) {
            int c = counts.get(level);
            double percent = (c * 100.0) / total;
            System.out.printf("%-8s: %d (%.1f%%)%n", level, c, percent);
        }
    }

    static void searchKeyword(BufferedReader br) throws IOException {
        System.out.print("Enter keyword to search: ");
        String keyword = br.readLine().trim().toLowerCase();
        if (keyword.isEmpty()) {
            System.out.println("Keyword cannot be empty.");
            return;
        }
        int found = 0;
        for (project e : entries) {
            if (e.message.toLowerCase().contains(keyword)) {
                if (found == 0) {
                    System.out.println("Matching entries:");
                }
                e.display();
                found++;
            }
        }
        if (found == 0) {
            System.out.println("No matching entries found.");
        } else {
            System.out.println("Total matches: " + found);
        }
    }

    static void displayErrors() {
        int count = 0;
        for (project e : entries) {
            if (e.level.equals("ERROR")) {
                e.display();
                count++;
            }
        }
        if (count == 0) {
            System.out.println("No error entries found.");
        }
    }

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(
                new InputStreamReader(System.in));
        loadLog();
        while (true) {
            System.out.println("\nLOG FILE ANALYZER");
            System.out.println("1. Analyze Log File");
            System.out.println("2. Search Keyword");
            System.out.println("3. Display Error Entries");
            System.out.println("4. Exit");
            System.out.print("Enter your choice: ");
            try {
                int choice = Integer.parseInt(br.readLine());
                switch (choice) {
                    case 1:
                        analyzeLog();
                        break;
                    case 2:
                        searchKeyword(br);
                        break;
                    case 3:
                        displayErrors();
                        break;
                    case 4:
                        System.out.println("Exiting program.");
                        return;
                    default:
                        System.out.println("Invalid choice.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Enter valid numeric input.");
            }
        }
    }
}
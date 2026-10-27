package com.pluralsight;
import java.io.*;
import java.util.*;
import tools.jackson.databind.ObjectMapper;

public class Payroll {
    public static void main(String[] args) throws IOException {
        Scanner s = new Scanner(System.in);
        String fileToRead = getInputFromConsole("Enter the employee roster file to open: ", s, false);
        String fileToWrite = getInputFromConsole("Enter the payroll file to write to: ", s, false);
        s.close();

        BufferedReader reader = new BufferedReader(new FileReader("src/main/resources/" + fileToRead));
        BufferedWriter writer = new BufferedWriter(new FileWriter("src/main/resources/" + fileToWrite));
        List<Employee> roster = new ArrayList<>();

        try {
            //Read from roster file
            while (reader.readLine() != null) {
                String fileLine = reader.readLine();
                if (fileLine == null) break;

                String[] tokens = fileLine.split("\\|");
                int employeeId = Integer.parseInt(tokens[0]);
                double hoursWorked = Double.parseDouble(tokens[2]);
                double payRate = Double.parseDouble(tokens[3]);

                Employee worker = new Employee(employeeId, tokens[1], hoursWorked, payRate);
                roster.add(worker);
            }

            //Write to output file
            if (fileToWrite.substring(fileToWrite.length() - 4).equalsIgnoreCase(".csv")) {
                writer.write("id|name|gross pay\n");
                for (Employee e : roster) {
                    writer.write(e.getEmployeeId() + "|" + e.getName() + "|" + e.getGrossPay() + "\n");
                }
            } else {    //Assume they want a .json because I'm lazy
                        //I want to do this long-term with only the Jackson libraries and not brute forcing the formatting but for now good enough
                ObjectMapper objMapper = new ObjectMapper();
                StringBuilder output = new StringBuilder("[\n");
                for (var i = 0; i < roster.size(); i++) {
                    String trail = (i == roster.size() - 1) ? "\n" : ",\n";
                    output.append(objMapper.writeValueAsString(roster.get(i))).append(trail);
                }
                output.append("]");
                writer.write(output.toString());
            }
        }

        catch (IOException e) {
            System.out.println("An error has occurred.");
            e.printStackTrace();
        }

        reader.close();
        writer.close();
    }

    public static String getInputFromConsole(String display, Scanner s, boolean clearBuffer) {
        if (clearBuffer) s.nextLine();
        System.out.println(display);
        return s.nextLine().trim();
    }
}

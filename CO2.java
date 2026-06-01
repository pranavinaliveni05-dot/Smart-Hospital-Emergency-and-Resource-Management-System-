import java.util.*;

public class CO2 {

    static HashMap<Integer, String> medicalRecords =
            new HashMap<>();

    static int[] beds = {10, 15, 8, 20, 12};

    public static void main(String[] args) {

        System.out.println("=== MEDICAL RECORDS ===");

        medicalRecords.put(101, "Diabetes");
        medicalRecords.put(102, "Fever");
        medicalRecords.put(103, "BP");

        for (Integer id : medicalRecords.keySet()) {

            System.out.println(
                    "ID : " + id +
                            " History : " +
                            medicalRecords.get(id));
        }

        System.out.println("\nSearch Record:");
        System.out.println(
                "History : " +
                        medicalRecords.get(101));

        System.out.println(
                "\n=== BED MANAGEMENT ===");

        for (int i = 0; i < beds.length; i++) {

            System.out.println(
                    "Ward " + (i + 1) +
                            " Beds : " + beds[i]);
        }

        int total = 0;

        for (int b : beds)
            total += b;

        System.out.println(
                "\nTotal Beds : " + total);

        System.out.println(
                "\n=== DAY-WISE STATISTICS ===");

        int[] patientStats =
                {0, 15, 12, 20, 10, 8};

        for (int i = 1;
             i < patientStats.length;
             i++) {

            System.out.println(
                    "Day " + i +
                            " Patients : " +
                            patientStats[i]);
        }
    }
}
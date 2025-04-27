package hospitalmanagementsystem;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.ResultSet;
import java.util.Scanner;

public class Patient {

    private Connection connection;
    private Scanner scanner;

    Patient(Connection con, Scanner sc) {
        this.connection = con;
        this.scanner = sc;
    }
    public void addPatient() {
        System.out.print("Enter patient name : ");
        String name = scanner.nextLine();
        System.out.print("Enter patient age : ");
        int age = scanner.nextInt();
        scanner.nextLine();
        System.out.print("Enter patient gender : ");
        String gender = scanner.nextLine();

        try {
            String Quary = "INSERT INTO patients(name,age,gender) VALUES(?,?,?)";
            PreparedStatement preparedstatment = connection.prepareStatement(Quary);
            preparedstatment.setString(1, name);
            preparedstatment.setInt(2, age);
            preparedstatment.setString(3, gender);
            int affectedRows = preparedstatment.executeUpdate();
            if (affectedRows > 0) {
                System.out.println("Patient added Successfully");
            } else {
                System.out.println("Failed to add patient !! Try again.");
            }

        } catch (SQLException e) {
            System.err.println("An error occurred while processing the database operation: " + e.getMessage());
            e.printStackTrace();
        }
    }
    // view patients
    public void viewPatients() {
        try {
            String query = "select * from patients";
            PreparedStatement pre = connection.prepareStatement(query);
            ResultSet resultSet = pre.executeQuery();
            System.out.println("+---------------+-------------------------+-----+----------+");
            System.out.println("| Patients Id   | Name                    | Age | Gender   |");
            System.out.println("+---------------+-------------------------+-----+----------+");
            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String name = resultSet.getString("name");
                int age = resultSet.getInt("age");
                String gender = resultSet.getString("gender");
                System.out.printf("|%-15s|%-25s|%-5d|%-10s|\n", id, name, age, gender);
            }
            System.out.println("+---------------+-------------------------+-----+----------+");
        } catch (SQLException e) {
            System.out.println("Failed to load patients details" + e.getMessage());
            e.printStackTrace();
        }
    }
    // check patient details
    public boolean getPatientById(int Pid) {

        String Query = "SELECT * FROM patients WHERE id =?";
        try {
            PreparedStatement pre = connection.prepareStatement(Query);
            pre.setInt(1, Pid);
            ResultSet resultSet = pre.executeQuery();
            if (resultSet.next()) {
                return true;
            } else {
                return false;
            }

        } catch (SQLException e) {
            System.out.println("Failed to load patient " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }
}

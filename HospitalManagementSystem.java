package hospitalmanagementsystem;

import java.sql.DriverManager;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.ResultSet;
import java.util.Scanner;

public class HospitalManagementSystem {

    private static final String url = "jdbc:mysql://localhost:3306/hospital";
    private static final String user = "root";
    private static final String password = "29344";
    
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            System.out.println("Failed to load driver" + e.getMessage());
            e.printStackTrace();
        }
        try {
            Connection connection = DriverManager.getConnection(url, user, password);
            Patient patient = new Patient(connection, scanner);
            Doctor doctor = new Doctor(connection, scanner);
            while (true) {
                System.out.println("              HOSPITAL MANAGEMENT SYSTEM");
                System.out.println("=========================================================");
                System.out.println("1. Add Patient");
                System.out.println("2. View Patients");
                System.out.println("3. View Doctors");
                System.out.println("4. Book Appointment");
                System.out.println("5. Exit");
                System.out.print("Enter your choice : ");
                int choice = scanner.nextInt();
                scanner.nextLine();
                switch (choice) {
                    case 1:// add patient
                        patient.addPatient();
                        System.out.println("");
                        break;
                    case 2: // view patients
                        patient.viewPatients();
                        System.out.println("");
                        break;
                    case 3: // view Doctors
                        doctor.viewDoctors();
                        System.out.println("");
                        break;
                    case 4: // Book Appointment
                        bookAppointment(patient, doctor, connection, scanner);
                        System.out.println("");
                        break;
                    case 5:
                        System.out.println("Thank you for use our service");
                        System.out.println("");
                        return;
                    default:
                        System.out.println("Enter valid choice!! Try again");

                }
            }
        } catch (SQLException e) {
            System.err.println("Database connection failed" + e.getMessage());
            e.printStackTrace();
        }

    }


 public static void bookAppointment(Patient patient, Doctor doctor, Connection connection, Scanner scanner) {

        System.out.print("Enter Patient Id : ");
        int patientId = scanner.nextInt();
        System.out.print("Enter Doctor Id : ");
        int doctorId = scanner.nextInt();
        System.out.print("Enter Appointment Date (YYYY-MM-DD): ");
        String appointmentDate = scanner.next();

        if (patient.getPatientById(patientId) && doctor.getDoctorById(doctorId)) {

            if (chackDoctorAvailability(doctorId, appointmentDate, connection)) {

                String appQuery = "INSERT INTO appoinments(patient_id,doctor_id,appointment_date) VALUES(?,?,?)";
                try {
                    PreparedStatement pre = connection.prepareStatement(appQuery);
                    pre.setInt(1, patientId);
                    pre.setInt(2, doctorId);
                    pre.setString(3, appointmentDate);
                    int affectedRows = pre.executeUpdate();
                    if(affectedRows>0){
                          System.out.println("Appointment Booked Successfully");
                    }else{
                        System.out.println("Failed to booked appointment");
                    }   
                } catch (SQLException e) {
                    e.printStackTrace();
                }

            } else {
                System.out.println("Doctor is not available on this date!! Please, Try again with new date");
            }

        } else {
            System.out.println("Enter valid doctor and patient Id");
        }   
    }
}

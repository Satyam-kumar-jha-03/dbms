import java.sql.*;
import java.time.LocalDate;
import java.util.Scanner;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;

public class HospitalManagementSystem {
    // Database configuration
    private static final String DB_URL = "jdbc:mysql://localhost:3306/hospital_management";
    private static final String DB_USERNAME = "root";
    private static final String DB_PASSWORD = "spsp.0011";
    
    private static Connection connection;
    private static Scanner scanner = new Scanner(System.in);
    private static LocalDate currentDate = LocalDate.now();

    public static void main(String[] args) {
        try {
            // Load JDBC driver
            Class.forName("com.mysql.jdbc.Driver");
            
            // Establish database connection
            connection = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD);
            System.out.println("Database connection established successfully.");
            
            // Start the application
            showMainMenu();
            
        } catch (ClassNotFoundException e) {
            System.err.println("MySQL JDBC Driver not found: " + e.getMessage());
        } catch (SQLException e) {
            System.err.println("Database connection failed: " + e.getMessage());
        } finally {
            // Close resources
            if (scanner != null) scanner.close();
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    System.err.println("Error closing connection: " + e.getMessage());
                }
            }
        }
    }

    private static void showMainMenu() {
        while (true) {
            System.out.println("\n=== Hospital Management System ===");
            System.out.println("1. Doctor Operations");
            System.out.println("2. Patient Operations");
            System.out.println("3. Hospital Operations");
            System.out.println("4. Medical Record Operations");
            System.out.println("5. Relative Operations");
            System.out.println("0. Exit");
            System.out.print("Choose an option: ");
            
            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline
            
            switch (choice) {
                case 1:
                    doctorOperations();
                    break;
                case 2:
                    patientOperations();
                    break;
                case 3:
                    hospitalOperations();
                    break;
                case 4:
                    recordOperations();
                    break;
                case 5:
                    relativeOperations();
                    break;
                case 0:
                    exitSystem();
                    return;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    // Doctor Operations
    private static void doctorOperations() {
        while (true) {
            System.out.println("\n=== Doctor Operations ===");
            System.out.println("1. Add Doctor");
            System.out.println("2. Delete Doctor");
            System.out.println("3. Update Doctor");
            System.out.println("4. View All Doctors");
            System.out.println("5. View Doctor Details");
            System.out.println("0. Back to Main Menu");
            System.out.print("Choose an option: ");
            
            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline
            
            try {
                switch (choice) {
                    case 1:
                        addDoctor();
                        break;
                    case 2:
                        deleteDoctor();
                        break;
                    case 3:
                        updateDoctor();
                        break;
                    case 4:
                        viewAllDoctors();
                        break;
                    case 5:
                        viewDoctorDetails();
                        break;
                    case 0:
                        return;
                    default:
                        System.out.println("Invalid choice. Please try again.");
                }
            } catch (SQLException | IOException e) {
                System.err.println("Error: " + e.getMessage());
            }
        }
    }

    private static void addDoctor() throws SQLException {
        System.out.println("\n--- Add New Doctor ---");
        
        System.out.print("Enter first name: ");
        String firstName = scanner.nextLine();
        
        System.out.print("Enter last name: ");
        String lastName = scanner.nextLine();
        
        System.out.print("Enter age: ");
        int age = scanner.nextInt();
        scanner.nextLine();
        
        System.out.print("Enter specialization: ");
        String specialization = scanner.nextLine();
        
        System.out.print("Enter gender: ");
        String gender = scanner.nextLine();
        
        System.out.print("Enter doctor ID: ");
        int doctorId = scanner.nextInt();
        
        System.out.print("Enter hospital ID: ");
        int hospitalId = scanner.nextInt();
        scanner.nextLine();

        String sql = "INSERT INTO doctor(F_name, L_name, age, specialist, gender, doctor_id, hospital_id) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, firstName);
            statement.setString(2, lastName);
            statement.setInt(3, age);
            statement.setString(4, specialization);
            statement.setString(5, gender);
            statement.setInt(6, doctorId);
            statement.setInt(7, hospitalId);

            int affectedRows = statement.executeUpdate();
            if (affectedRows > 0) {
                System.out.println("Doctor added successfully!");
            } else {
                System.out.println("Failed to add doctor.");
            }
        }
    }

    private static void deleteDoctor() throws SQLException {
        System.out.println("\n--- Delete Doctor ---");
        System.out.print("Enter doctor ID to delete: ");
        int doctorId = scanner.nextInt();
        scanner.nextLine();

        String sql = "DELETE FROM doctor WHERE doctor_id = ?";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, doctorId);

            int affectedRows = statement.executeUpdate();
            if (affectedRows > 0) {
                System.out.println("Doctor deleted successfully!");
            } else {
                System.out.println("No doctor found with ID: " + doctorId);
            }
        }
    }

    private static void updateDoctor() throws SQLException, IOException {
        System.out.println("\n--- Update Doctor ---");
        System.out.print("Enter doctor ID to update: ");
        int doctorId = scanner.nextInt();
        scanner.nextLine();

        System.out.println("What do you want to update?");
        System.out.println("1. First name");
        System.out.println("2. Last name");
        System.out.println("3. Age");
        System.out.println("4. Specialization");
        System.out.print("Enter your choice: ");
        int choice = scanner.nextInt();
        scanner.nextLine();

        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        String columnName = "";
        String newValue = "";
        int newAge = 0;

        switch (choice) {
            case 1:
                System.out.print("Enter new first name: ");
                columnName = "F_name";
                newValue = br.readLine();
                break;
            case 2:
                System.out.print("Enter new last name: ");
                columnName = "L_name";
                newValue = br.readLine();
                break;
            case 3:
                System.out.print("Enter new age: ");
                columnName = "age";
                newAge = Integer.parseInt(br.readLine());
                break;
            case 4:
                System.out.print("Enter new specialization: ");
                columnName = "specialist";
                newValue = br.readLine();
                break;
            default:
                System.out.println("Invalid choice.");
                return;
        }

        String sql;
        if (choice == 3) {
            sql = "UPDATE doctor SET " + columnName + " = ? WHERE doctor_id = ?";
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setInt(1, newAge);
                statement.setInt(2, doctorId);
                executeUpdate(statement);
            }
        } else {
            sql = "UPDATE doctor SET " + columnName + " = ? WHERE doctor_id = ?";
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setString(1, newValue);
                statement.setInt(2, doctorId);
                executeUpdate(statement);
            }
        }
    }

    private static void viewAllDoctors() throws SQLException {
        System.out.println("\n--- All Doctors ---");
        String sql = "SELECT * FROM doctor";

        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(sql)) {

            System.out.println("+------------+-----------+-----+----------------+--------+------------+--------------+");
            System.out.println("| First Name | Last Name | Age | Specialization | Gender | Doctor ID  | Hospital ID  |");
            System.out.println("+------------+-----------+-----+----------------+--------+------------+--------------+");

            while (resultSet.next()) {
                String firstName = resultSet.getString("F_name");
                String lastName = resultSet.getString("L_name");
                int age = resultSet.getInt("age");
                String specialization = resultSet.getString("specialist");
                String gender = resultSet.getString("gender");
                int doctorId = resultSet.getInt("doctor_id");
                int hospitalId = resultSet.getInt("hospital_id");

                System.out.printf("| %-10s | %-9s | %-3d | %-14s | %-6s | %-10d | %-12d |\n",
                        firstName, lastName, age, specialization, gender, doctorId, hospitalId);
                System.out.println("+------------+-----------+-----+----------------+--------+------------+--------------+");
            }
        }
    }

    private static void viewDoctorDetails() throws SQLException {
        System.out.println("\n--- Doctor Details ---");
        System.out.print("Enter doctor ID: ");
        int doctorId = scanner.nextInt();
        scanner.nextLine();

        String sql = "SELECT * FROM doctor WHERE doctor_id = ?";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, doctorId);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    System.out.println("\nDoctor Details:");
                    System.out.println("+------------+-----------+-----+----------------+--------+------------+--------------+");
                    System.out.println("| First Name | Last Name | Age | Specialization | Gender | Doctor ID  | Hospital ID  |");
                    System.out.println("+------------+-----------+-----+----------------+--------+------------+--------------+");

                    String firstName = resultSet.getString("F_name");
                    String lastName = resultSet.getString("L_name");
                    int age = resultSet.getInt("age");
                    String specialization = resultSet.getString("specialist");
                    String gender = resultSet.getString("gender");
                    int docId = resultSet.getInt("doctor_id");
                    int hospitalId = resultSet.getInt("hospital_id");

                    System.out.printf("| %-10s | %-9s | %-3d | %-14s | %-6s | %-10d | %-12d |\n",
                            firstName, lastName, age, specialization, gender, docId, hospitalId);
                    System.out.println("+------------+-----------+-----+----------------+--------+------------+--------------+");
                } else {
                    System.out.println("No doctor found with ID: " + doctorId);
                }
            }
        }
    }

    // Patient Operations (similar structure to Doctor Operations)
    private static void patientOperations() {
        while (true) {
            System.out.println("\n=== Patient Operations ===");
            System.out.println("1. Add Patient");
            System.out.println("2. Delete Patient");
            System.out.println("3. Update Patient");
            System.out.println("4. View All Patients");
            System.out.println("5. View Patient Details");
            System.out.println("0. Back to Main Menu");
            System.out.print("Choose an option: ");
            
            int choice = scanner.nextInt();
            scanner.nextLine();
            
            try {
                switch (choice) {
                    case 1:
                        addPatient();
                        break;
                    case 2:
                        deletePatient();
                        break;
                    case 3:
                        updatePatient();
                        break;
                    case 4:
                        viewAllPatients();
                        break;
                    case 5:
                        viewPatientDetails();
                        break;
                    case 0:
                        return;
                    default:
                        System.out.println("Invalid choice. Please try again.");
                }
            } catch (SQLException | IOException e) {
                System.err.println("Error: " + e.getMessage());
            }
        }
    }

    private static void addPatient() throws SQLException {
        System.out.println("\n--- Add New Patient ---");
        
        System.out.print("Enter first name: ");
        String firstName = scanner.nextLine();
        
        System.out.print("Enter last name: ");
        String lastName = scanner.nextLine();
        
        System.out.print("Enter age: ");
        int age = scanner.nextInt();
        scanner.nextLine();
        
        System.out.print("Enter disease: ");
        String disease = scanner.nextLine();
        
        System.out.print("Enter gender: ");
        String gender = scanner.nextLine();
        
        System.out.print("Enter phone number: ");
        String phoneNumber = scanner.nextLine();
        
        System.out.print("Enter patient ID: ");
        int patientId = scanner.nextInt();
        scanner.nextLine();

        String sql = "INSERT INTO patient(F_name, L_name, age, disease, gender, phone_number, patient_id) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, firstName);
            statement.setString(2, lastName);
            statement.setInt(3, age);
            statement.setString(4, disease);
            statement.setString(5, gender);
            statement.setString(6, phoneNumber);
            statement.setInt(7, patientId);

            int affectedRows = statement.executeUpdate();
            if (affectedRows > 0) {
                System.out.println("Patient added successfully!");
            } else {
                System.out.println("Failed to add patient.");
            }
        }
    }

    private static void deletePatient() throws SQLException {
        System.out.println("\n--- Delete Patient ---");
        System.out.print("Enter patient ID to delete: ");
        int patientId = scanner.nextInt();
        scanner.nextLine();

        String sql = "DELETE FROM patient WHERE patient_id = ?";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, patientId);

            int affectedRows = statement.executeUpdate();
            if (affectedRows > 0) {
                System.out.println("Patient deleted successfully!");
            } else {
                System.out.println("No patient found with ID: " + patientId);
            }
        }
    }

    private static void updatePatient() throws SQLException, IOException {
        System.out.println("\n--- Update Patient ---");
        System.out.print("Enter patient ID to update: ");
        int patientId = scanner.nextInt();
        scanner.nextLine();

        System.out.println("What do you want to update?");
        System.out.println("1. First name");
        System.out.println("2. Last name");
        System.out.println("3. Age");
        System.out.println("4. Disease");
        System.out.println("5. Phone number");
        System.out.print("Enter your choice: ");
        int choice = scanner.nextInt();
        scanner.nextLine();

        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        String columnName = "";
        String newValue = "";
        int newAge = 0;

        switch (choice) {
            case 1:
                System.out.print("Enter new first name: ");
                columnName = "F_name";
                newValue = br.readLine();
                break;
            case 2:
                System.out.print("Enter new last name: ");
                columnName = "L_name";
                newValue = br.readLine();
                break;
            case 3:
                System.out.print("Enter new age: ");
                columnName = "age";
                newAge = Integer.parseInt(br.readLine());
                break;
            case 4:
                System.out.print("Enter new disease: ");
                columnName = "disease";
                newValue = br.readLine();
                break;
            case 5:
                System.out.print("Enter new phone number: ");
                columnName = "phone_number";
                newValue = br.readLine();
                break;
            default:
                System.out.println("Invalid choice.");
                return;
        }

        String sql;
        if (choice == 3) {
            sql = "UPDATE patient SET " + columnName + " = ? WHERE patient_id = ?";
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setInt(1, newAge);
                statement.setInt(2, patientId);
                executeUpdate(statement);
            }
        } else {
            sql = "UPDATE patient SET " + columnName + " = ? WHERE patient_id = ?";
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setString(1, newValue);
                statement.setInt(2, patientId);
                executeUpdate(statement);
            }
        }
    }

    private static void viewAllPatients() throws SQLException {
        System.out.println("\n--- All Patients ---");
        String sql = "SELECT * FROM patient";

        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(sql)) {

            System.out.println("+------------+-----------+-----+----------------+--------+----------------+------------+");
            System.out.println("| First Name | Last Name | Age |    Disease     | Gender |  Phone Number  | Patient ID |");
            System.out.println("+------------+-----------+-----+----------------+--------+----------------+------------+");

            while (resultSet.next()) {
                String firstName = resultSet.getString("F_name");
                String lastName = resultSet.getString("L_name");
                int age = resultSet.getInt("age");
                String disease = resultSet.getString("disease");
                String gender = resultSet.getString("gender");
                String phoneNumber = resultSet.getString("phone_number");
                int patientId = resultSet.getInt("patient_id");

                System.out.printf("| %-10s | %-9s | %-3d | %-14s | %-6s | %-14s | %-10d |\n",
                        firstName, lastName, age, disease, gender, phoneNumber, patientId);
                System.out.println("+------------+-----------+-----+----------------+--------+----------------+------------+");
            }
        }
    }

    private static void viewPatientDetails() throws SQLException {
        System.out.println("\n--- Patient Details ---");
        System.out.print("Enter patient ID: ");
        int patientId = scanner.nextInt();
        scanner.nextLine();

        String sql = "SELECT * FROM patient WHERE patient_id = ?";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, patientId);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    System.out.println("\nPatient Details:");
                    System.out.println("+------------+-----------+-----+----------------+--------+----------------+------------+");
                    System.out.println("| First Name | Last Name | Age |    Disease     | Gender |  Phone Number  | Patient ID |");
                    System.out.println("+------------+-----------+-----+----------------+--------+----------------+------------+");

                    String firstName = resultSet.getString("F_name");
                    String lastName = resultSet.getString("L_name");
                    int age = resultSet.getInt("age");
                    String disease = resultSet.getString("disease");
                    String gender = resultSet.getString("gender");
                    String phoneNumber = resultSet.getString("phone_number");
                    int patId = resultSet.getInt("patient_id");

                    System.out.printf("| %-10s | %-9s | %-3d | %-14s | %-6s | %-14s | %-10d |\n",
                            firstName, lastName, age, disease, gender, phoneNumber, patId);
                    System.out.println("+------------+-----------+-----+----------------+--------+----------------+------------+");
                } else {
                    System.out.println("No patient found with ID: " + patientId);
                }
            }
        }
    }

    // Hospital Operations (similar structure)
    private static void hospitalOperations() {
        while (true) {
            System.out.println("\n=== Hospital Operations ===");
            System.out.println("1. Add Hospital");
            System.out.println("2. Delete Hospital");
            System.out.println("3. Update Hospital");
            System.out.println("4. View All Hospitals");
            System.out.println("5. View Hospital Details");
            System.out.println("0. Back to Main Menu");
            System.out.print("Choose an option: ");
            
            int choice = scanner.nextInt();
            scanner.nextLine();
            
            try {
                switch (choice) {
                    case 1:
                        addHospital();
                        break;
                    case 2:
                        deleteHospital();
                        break;
                    case 3:
                        updateHospital();
                        break;
                    case 4:
                        viewAllHospitals();
                        break;
                    case 5:
                        viewHospitalDetails();
                        break;
                    case 0:
                        return;
                    default:
                        System.out.println("Invalid choice. Please try again.");
                }
            } catch (SQLException | IOException e) {
                System.err.println("Error: " + e.getMessage());
            }
        }
    }

    private static void addHospital() throws SQLException {
        System.out.println("\n--- Add New Hospital ---");
        
        System.out.print("Enter hospital name: ");
        String hospitalName = scanner.nextLine();
        
        System.out.print("Enter state: ");
        String state = scanner.nextLine();
        
        System.out.print("Enter city: ");
        String city = scanner.nextLine();
        
        System.out.print("Enter address: ");
        String address = scanner.nextLine();
        
        System.out.print("Enter hospital ID: ");
        int hospitalId = scanner.nextInt();
        scanner.nextLine();

        String sql = "INSERT INTO hospitals(hospital_name, state, city, address, hospital_id) " +
                     "VALUES (?, ?, ?, ?, ?)";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, hospitalName);
            statement.setString(2, state);
            statement.setString(3, city);
            statement.setString(4, address);
            statement.setInt(5, hospitalId);

            int affectedRows = statement.executeUpdate();
            if (affectedRows > 0) {
                System.out.println("Hospital added successfully!");
            } else {
                System.out.println("Failed to add hospital.");
            }
        }
    }

    private static void deleteHospital() throws SQLException {
        System.out.println("\n--- Delete Hospital ---");
        System.out.print("Enter hospital ID to delete: ");
        int hospitalId = scanner.nextInt();
        scanner.nextLine();

        String sql = "DELETE FROM hospitals WHERE hospital_id = ?";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, hospitalId);

            int affectedRows = statement.executeUpdate();
            if (affectedRows > 0) {
                System.out.println("Hospital deleted successfully!");
            } else {
                System.out.println("No hospital found with ID: " + hospitalId);
            }
        }
    }

    private static void updateHospital() throws SQLException, IOException {
        System.out.println("\n--- Update Hospital ---");
        System.out.print("Enter hospital ID to update: ");
        int hospitalId = scanner.nextInt();
        scanner.nextLine();

        System.out.println("What do you want to update?");
        System.out.println("1. Hospital name");
        System.out.println("2. State");
        System.out.println("3. Address");
        System.out.print("Enter your choice: ");
        int choice = scanner.nextInt();
        scanner.nextLine();

        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        String columnName = "";
        String newValue = "";

        switch (choice) {
            case 1:
                System.out.print("Enter new hospital name: ");
                columnName = "hospital_name";
                newValue = br.readLine();
                break;
            case 2:
                System.out.print("Enter new state: ");
                columnName = "state";
                newValue = br.readLine();
                break;
            case 3:
                System.out.print("Enter new address: ");
                columnName = "address";
                newValue = br.readLine();
                break;
            default:
                System.out.println("Invalid choice.");
                return;
        }

        String sql = "UPDATE hospitals SET " + columnName + " = ? WHERE hospital_id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, newValue);
            statement.setInt(2, hospitalId);
            executeUpdate(statement);
        }
    }

    private static void viewAllHospitals() throws SQLException {
        System.out.println("\n--- All Hospitals ---");
        String sql = "SELECT * FROM hospitals";

        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(sql)) {

            System.out.println("+------------------+------------+------------+-----------------------------+--------------+");
            System.out.println("|  Hospital Name   |   State    |    City    |           Address          | Hospital ID  |");
            System.out.println("+------------------+------------+------------+-----------------------------+--------------+");

            while (resultSet.next()) {
                String hospitalName = resultSet.getString("hospital_name");
                String state = resultSet.getString("state");
                String city = resultSet.getString("city");
                String address = resultSet.getString("address");
                int hospitalId = resultSet.getInt("hospital_id");

                System.out.printf("| %-16s | %-10s | %-10s | %-27s | %-12d |\n",
                        hospitalName, state, city, address, hospitalId);
                System.out.println("+------------------+------------+------------+-----------------------------+--------------+");
            }
        }
    }

    private static void viewHospitalDetails() throws SQLException {
        System.out.println("\n--- Hospital Details ---");
        System.out.print("Enter hospital ID: ");
        int hospitalId = scanner.nextInt();
        scanner.nextLine();

        String sql = "SELECT * FROM hospitals WHERE hospital_id = ?";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, hospitalId);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    System.out.println("\nHospital Details:");
                    System.out.println("+------------------+------------+------------+-----------------------------+--------------+");
                    System.out.println("|  Hospital Name   |   State    |    City    |           Address          | Hospital ID  |");
                    System.out.println("+------------------+------------+------------+-----------------------------+--------------+");

                    String hospitalName = resultSet.getString("hospital_name");
                    String state = resultSet.getString("state");
                    String city = resultSet.getString("city");
                    String address = resultSet.getString("address");
                    int hospId = resultSet.getInt("hospital_id");

                    System.out.printf("| %-16s | %-10s | %-10s | %-27s | %-12d |\n",
                            hospitalName, state, city, address, hospId);
                    System.out.println("+------------------+------------+------------+-----------------------------+--------------+");
                } else {
                    System.out.println("No hospital found with ID: " + hospitalId);
                }
            }
        }
    }

    // Medical Record Operations (similar structure)
    private static void recordOperations() {
        while (true) {
            System.out.println("\n=== Medical Record Operations ===");
            System.out.println("1. Add Medical Record");
            System.out.println("2. Delete Medical Record");
            System.out.println("3. Update Medical Record");
            System.out.println("4. View All Medical Records");
            System.out.println("5. View Medical Record Details");
            System.out.println("0. Back to Main Menu");
            System.out.print("Choose an option: ");
            
            int choice = scanner.nextInt();
            scanner.nextLine();
            
            try {
                switch (choice) {
                    case 1:
                        addMedicalRecord();
                        break;
                    case 2:
                        deleteMedicalRecord();
                        break;
                    case 3:
                        updateMedicalRecord();
                        break;
                    case 4:
                        viewAllMedicalRecords();
                        break;
                    case 5:
                        viewMedicalRecordDetails();
                        break;
                    case 0:
                        return;
                    default:
                        System.out.println("Invalid choice. Please try again.");
                }
            } catch (SQLException | IOException e) {
                System.err.println("Error: " + e.getMessage());
            }
        }
    }

    private static void addMedicalRecord() throws SQLException {
        System.out.println("\n--- Add New Medical Record ---");
        
        System.out.print("Enter record ID: ");
        int recordId = scanner.nextInt();
        scanner.nextLine();
        
        // Use current date for examination date
        String examinationDate = currentDate.toString();
        
        System.out.print("Enter problem diagnosed: ");
        String problemFound = scanner.nextLine();
        
        System.out.print("Enter doctor ID: ");
        int doctorId = scanner.nextInt();
        
        System.out.print("Enter patient ID: ");
        int patientId = scanner.nextInt();
        
        System.out.print("Enter hospital ID: ");
        int hospitalId = scanner.nextInt();
        scanner.nextLine();

        String sql = "INSERT INTO medical_record(record_id, examination_date, problem_found, doctor_id, patient_id, hospital_id) " +
                     "VALUES (?, ?, ?, ?, ?, ?)";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, recordId);
            statement.setString(2, examinationDate);
            statement.setString(3, problemFound);
            statement.setInt(4, doctorId);
            statement.setInt(5, patientId);
            statement.setInt(6, hospitalId);

            int affectedRows = statement.executeUpdate();
            if (affectedRows > 0) {
                System.out.println("Medical record added successfully!");
            } else {
                System.out.println("Failed to add medical record.");
            }
        }
    }

    private static void deleteMedicalRecord() throws SQLException {
        System.out.println("\n--- Delete Medical Record ---");
        System.out.print("Enter record ID to delete: ");
        int recordId = scanner.nextInt();
        scanner.nextLine();

        String sql = "DELETE FROM medical_record WHERE record_id = ?";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, recordId);

            int affectedRows = statement.executeUpdate();
            if (affectedRows > 0) {
                System.out.println("Medical record deleted successfully!");
            } else {
                System.out.println("No medical record found with ID: " + recordId);
            }
        }
    }

    private static void updateMedicalRecord() throws SQLException, IOException {
        System.out.println("\n--- Update Medical Record ---");
        System.out.print("Enter record ID to update: ");
        int recordId = scanner.nextInt();
        scanner.nextLine();

        System.out.println("What do you want to update?");
        System.out.println("1. Problem diagnosed");
        System.out.println("2. Doctor ID");
        System.out.println("3. Hospital ID");
        System.out.print("Enter your choice: ");
        int choice = scanner.nextInt();
        scanner.nextLine();

        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        String columnName = "";
        String newProblem = "";
        int newId = 0;

        switch (choice) {
            case 1:
                System.out.print("Enter new problem diagnosed: ");
                columnName = "problem_found";
                newProblem = br.readLine();
                break;
            case 2:
                System.out.print("Enter new doctor ID: ");
                columnName = "doctor_id";
                newId = Integer.parseInt(br.readLine());
                break;
            case 3:
                System.out.print("Enter new hospital ID: ");
                columnName = "hospital_id";
                newId = Integer.parseInt(br.readLine());
                break;
            default:
                System.out.println("Invalid choice.");
                return;
        }

        String sql;
        if (choice == 1) {
            sql = "UPDATE medical_record SET " + columnName + " = ? WHERE record_id = ?";
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setString(1, newProblem);
                statement.setInt(2, recordId);
                executeUpdate(statement);
            }
        } else {
            sql = "UPDATE medical_record SET " + columnName + " = ? WHERE record_id = ?";
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setInt(1, newId);
                statement.setInt(2, recordId);
                executeUpdate(statement);
            }
        }
    }

    private static void viewAllMedicalRecords() throws SQLException {
        System.out.println("\n--- All Medical Records ---");
        String sql = "SELECT * FROM medical_record";

        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(sql)) {

            System.out.println("+-----------+------------------+-------------------+-----------+------------+--------------+");
            System.out.println("| Record ID | Examination Date | Problem Diagnosed | Doctor ID | Patient ID | Hospital ID  |");
            System.out.println("+-----------+------------------+-------------------+-----------+------------+--------------+");

            while (resultSet.next()) {
                int recordId = resultSet.getInt("record_id");
                String examinationDate = resultSet.getString("examination_date");
                String problemFound = resultSet.getString("problem_found");
                int doctorId = resultSet.getInt("doctor_id");
                int patientId = resultSet.getInt("patient_id");
                int hospitalId = resultSet.getInt("hospital_id");

                System.out.printf("| %-9d | %-16s | %-17s | %-9d | %-10d | %-12d |\n",
                        recordId, examinationDate, problemFound, doctorId, patientId, hospitalId);
                System.out.println("+-----------+------------------+-------------------+-----------+------------+--------------+");
            }
        }
    }

    private static void viewMedicalRecordDetails() throws SQLException {
        System.out.println("\n--- Medical Record Details ---");
        System.out.print("Enter record ID: ");
        int recordId = scanner.nextInt();
        scanner.nextLine();

        String sql = "SELECT * FROM medical_record WHERE record_id = ?";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, recordId);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    System.out.println("\nMedical Record Details:");
                    System.out.println("+-----------+------------------+-------------------+-----------+------------+--------------+");
                    System.out.println("| Record ID | Examination Date | Problem Diagnosed | Doctor ID | Patient ID | Hospital ID  |");
                    System.out.println("+-----------+------------------+-------------------+-----------+------------+--------------+");

                    int recId = resultSet.getInt("record_id");
                    String examinationDate = resultSet.getString("examination_date");
                    String problemFound = resultSet.getString("problem_found");
                    int doctorId = resultSet.getInt("doctor_id");
                    int patientId = resultSet.getInt("patient_id");
                    int hospitalId = resultSet.getInt("hospital_id");

                    System.out.printf("| %-9d | %-16s | %-17s | %-9d | %-10d | %-12d |\n",
                            recId, examinationDate, problemFound, doctorId, patientId, hospitalId);
                    System.out.println("+-----------+------------------+-------------------+-----------+------------+--------------+");
                } else {
                    System.out.println("No medical record found with ID: " + recordId);
                }
            }
        }
    }

    // Relative Operations (similar structure)
    private static void relativeOperations() {
        while (true) {
            System.out.println("\n=== Relative Operations ===");
            System.out.println("1. Add Relative");
            System.out.println("2. Delete Relative");
            System.out.println("3. Update Relative");
            System.out.println("4. View All Relatives");
            System.out.println("5. View Relative Details");
            System.out.println("0. Back to Main Menu");
            System.out.print("Choose an option: ");
            
            int choice = scanner.nextInt();
            scanner.nextLine();
            
            try {
                switch (choice) {
                    case 1:
                        addRelative();
                        break;
                    case 2:
                        deleteRelative();
                        break;
                    case 3:
                        updateRelative();
                        break;
                    case 4:
                        viewAllRelatives();
                        break;
                    case 5:
                        viewRelativeDetails();
                        break;
                    case 0:
                        return;
                    default:
                        System.out.println("Invalid choice. Please try again.");
                }
            } catch (SQLException | IOException e) {
                System.err.println("Error: " + e.getMessage());
            }
        }
    }

    private static void addRelative() throws SQLException {
        System.out.println("\n--- Add New Relative ---");
        
        System.out.print("Enter relative name: ");
        String relativeName = scanner.nextLine();
        
        System.out.print("Enter patient name: ");
        String patientName = scanner.nextLine();
        
        System.out.print("Enter relation with patient: ");
        String relation = scanner.nextLine();
        
        System.out.print("Enter patient ID: ");
        int patientId = scanner.nextInt();
        
        System.out.print("Enter relative ID: ");
        int relativeId = scanner.nextInt();
        scanner.nextLine();

        String sql = "INSERT INTO relative(relative_name, patient_name, relation, patient_id, relative_id) " +
                     "VALUES (?, ?, ?, ?, ?)";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, relativeName);
            statement.setString(2, patientName);
            statement.setString(3, relation);
            statement.setInt(4, patientId);
            statement.setInt(5, relativeId);

            int affectedRows = statement.executeUpdate();
            if (affectedRows > 0) {
                System.out.println("Relative added successfully!");
            } else {
                System.out.println("Failed to add relative.");
            }
        }
    }

    private static void deleteRelative() throws SQLException {
        System.out.println("\n--- Delete Relative ---");
        System.out.print("Enter relative ID to delete: ");
        int relativeId = scanner.nextInt();
        scanner.nextLine();

        String sql = "DELETE FROM relative WHERE relative_id = ?";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, relativeId);

            int affectedRows = statement.executeUpdate();
            if (affectedRows > 0) {
                System.out.println("Relative deleted successfully!");
            } else {
                System.out.println("No relative found with ID: " + relativeId);
            }
        }
    }

    private static void updateRelative() throws SQLException, IOException {
        System.out.println("\n--- Update Relative ---");
        System.out.print("Enter relative ID to update: ");
        int relativeId = scanner.nextInt();
        scanner.nextLine();

        System.out.println("What do you want to update?");
        System.out.println("1. Relative name");
        System.out.println("2. Relation");
        System.out.print("Enter your choice: ");
        int choice = scanner.nextInt();
        scanner.nextLine();

        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        String columnName = "";
        String newValue = "";

        switch (choice) {
            case 1:
                System.out.print("Enter new relative name: ");
                columnName = "relative_name";
                newValue = br.readLine();
                break;
            case 2:
                System.out.print("Enter new relation: ");
                columnName = "relation";
                newValue = br.readLine();
                break;
            default:
                System.out.println("Invalid choice.");
                return;
        }

        String sql = "UPDATE relative SET " + columnName + " = ? WHERE relative_id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, newValue);
            statement.setInt(2, relativeId);
            executeUpdate(statement);
        }
    }

    private static void viewAllRelatives() throws SQLException {
        System.out.println("\n--- All Relatives ---");
        String sql = "SELECT * FROM relative";

        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(sql)) {

            System.out.println("+---------------+--------------+----------+------------+--------------+");
            System.out.println("| Relative Name | Patient Name | Relation | Patient ID | Relative ID  |");
            System.out.println("+---------------+--------------+----------+------------+--------------+");

            while (resultSet.next()) {
                String relativeName = resultSet.getString("relative_name");
                String patientName = resultSet.getString("patient_name");
                String relation = resultSet.getString("relation");
                int patientId = resultSet.getInt("patient_id");
                int relativeId = resultSet.getInt("relative_id");

                System.out.printf("| %-13s | %-12s | %-8s | %-10d | %-12d |\n",
                        relativeName, patientName, relation, patientId, relativeId);
                System.out.println("+---------------+--------------+----------+------------+--------------+");
            }
        }
    }

    private static void viewRelativeDetails() throws SQLException {
        System.out.println("\n--- Relative Details ---");
        System.out.print("Enter relative ID: ");
        int relativeId = scanner.nextInt();
        scanner.nextLine();

        String sql = "SELECT * FROM relative WHERE relative_id = ?";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, relativeId);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    System.out.println("\nRelative Details:");
                    System.out.println("+---------------+--------------+----------+------------+--------------+");
                    System.out.println("| Relative Name | Patient Name | Relation | Patient ID | Relative ID  |");
                    System.out.println("+---------------+--------------+----------+------------+--------------+");

                    String relativeName = resultSet.getString("relative_name");
                    String patientName = resultSet.getString("patient_name");
                    String relation = resultSet.getString("relation");
                    int patientId = resultSet.getInt("patient_id");
                    int relId = resultSet.getInt("relative_id");

                    System.out.printf("| %-13s | %-12s | %-8s | %-10d | %-12d |\n",
                            relativeName, patientName, relation, patientId, relId);
                    System.out.println("+---------------+--------------+----------+------------+--------------+");
                } else {
                    System.out.println("No relative found with ID: " + relativeId);
                }
            }
        }
    }

    // Helper method to execute updates
    private static void executeUpdate(PreparedStatement statement) throws SQLException {
        int affectedRows = statement.executeUpdate();
        if (affectedRows > 0) {
            System.out.println("Update successful!");
        } else {
            System.out.println("No records were updated.");
        }
    }

    private static void exitSystem() {
        System.out.println("\nExiting Hospital Management System...");
        try {
            for (int i = 0; i < 5; i++) {
                System.out.print(".");
                Thread.sleep(450);
            }
            System.out.println("\nThank you for using Hospital Management System!");
        } catch (InterruptedException e) {
            System.err.println("Error during exit: " + e.getMessage());
        }
    }
}
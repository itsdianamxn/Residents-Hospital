package db;

import classes.Hospital;
import classes.Resident;
import classes.Specialization;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class Database {
    private static Connection connection = null;
    private static Database instance = null;

    private Database() {
        try {
            Class.forName("org.postgresql.Driver");
            connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/hr_db", "student", "student");
            if(connection != null)
                System.out.println("Connected to the database!");
        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
    public static Database getInstance()
    {
        if (instance == null)
        {
            instance = new Database();
        }
        return instance;
    }

    public static int addResident(Resident resident, String specializations) {
        try ( PreparedStatement ps1 = getInstance().getConnection().prepareStatement(
                "SELECT add_resident(?, ?, ?) ")) {
            ps1.setString(1, resident.getName());
            ps1.setInt(2, resident.getGrade());
            ps1.setString(3, specializations);
            ResultSet resultSet = ps1.executeQuery();
            if (resultSet.next())
            {
                return resultSet.getInt(1);
            }
            throw new IllegalStateException("SQL did not return a value");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static int addHospital(Hospital hospital, String specializations) {
        try ( PreparedStatement ps1 = getInstance().getConnection().prepareStatement(
                "SELECT add_hospital(?, ?, ?, ?) ")) {
            ps1.setString(1, hospital.getName());
            ps1.setInt(2, hospital.getCapacity());
            ps1.setInt(3, hospital.getGrade());
            ps1.setString(4, specializations);
            ResultSet resultSet = ps1.executeQuery();
            if (resultSet.next())
            {
                return resultSet.getInt(1);
            }
            throw new IllegalStateException("SQL did not return a value");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    public static void makePairings() {
        try ( CallableStatement ps1 = getInstance().getConnection().prepareCall(
                "CALL make_pairings() ")) {
            ps1.execute();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static String getPairingR(int resident_id) {
        try ( PreparedStatement ps1 = getInstance().getConnection().prepareStatement(
                "SELECT h.name AS hospital_name, r.name AS resident_name\n" +
                        "        FROM matchings m\n" +
                        "        JOIN hospitals h ON m.hospital_id = h.hospital_id\n" +
                        "        JOIN residents r ON m.resident_id = r.resident_id " +
                        "where r.resident_id = ?")) {
            ps1.setInt(1, resident_id);
            ResultSet resultSet = ps1.executeQuery();
            if (resultSet.next())
            {
                return(resultSet.getString("resident_name") + " is assigned to " + resultSet.getString("hospital_name") + ".");
            }
            return(resident_id + " was not assigned.");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    public static String getPairingH(int hospital_id) {
        StringBuilder sb = new StringBuilder();
        try ( PreparedStatement ps1 = getInstance().getConnection().prepareStatement(
                "SELECT r.name AS resident_name , h.name AS hospital_name\n" +
                        "        FROM matchings m\n" +
                        "        JOIN residents r ON m.resident_id = r.resident_id " +
                        "        JOIN hospitals h ON m.hospital_id = h.hospital_id\n" +
                        "where h.hospital_id = ?")) {
            ps1.setInt(1, hospital_id);
            ResultSet resultSet = ps1.executeQuery();
            while (resultSet.next())
            {
                sb.append(resultSet.getString("resident_name")).append(" is assigned to ").append(resultSet.getString("hospital_name")).append(".\n");
            }
            if (sb.isEmpty())
                return(hospital_id + " has no residents.");
            else return sb.toString();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


    public Connection getConnection() {
        return connection;
    }

    public void closeConnection() {
        try {
            connection.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void deleteAllData() {
        try {
            connection.createStatement().execute("DELETE FROM residents_specialization");
            connection.createStatement().execute("DELETE FROM residents");
            connection.createStatement().execute("DELETE FROM specializations");
            connection.createStatement().execute("DELETE FROM hospital_specialization");
            connection.createStatement().execute("DELETE FROM hospitals");
            connection.createStatement().execute("DELETE FROM matchings");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static List<Hospital> getAllHospitals() {
        String query = "select * from hospital_specialization hs join specializations s on hs.specialization_id = s.specialization_id where hs.hospital_id = ?";
        List<Hospital> hospitals = new ArrayList<>();
        try (PreparedStatement pstmt = getInstance().getConnection().prepareStatement("select * from hospitals")) {
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                Hospital hospital = new Hospital(rs.getInt("hospital_id"), rs.getString("name"),
                        rs.getInt("capacity"), rs.getInt("grade"));

                PreparedStatement stmt = getInstance().getConnection().prepareStatement(query);
                stmt.setInt(1, hospital.getHospital_id());
                ResultSet specializations = stmt.executeQuery();
                while (specializations.next())
                {
                    Specialization s = new Specialization(specializations.getInt("specialization_id"), specializations.getString("name"));
                    hospital.getSpecialization().add(s);
                }
                specializations.close();
                stmt.close();
                hospitals.add(hospital);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return hospitals;
    }
    public static List<Resident> getAllResidents() {
        String query = "select * from specializations s join residents_specialization rs on rs.specialization_id = s.specialization_id where rs.resident_id = ?";
        List<Resident> residents = new ArrayList<>();
        try (PreparedStatement pstmt = getInstance().getConnection().prepareStatement("select * from residents")) {
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                Resident resident = new Resident(rs.getInt("resident_id"), rs.getString("name"), rs.getBoolean("assigned"), rs.getInt("grade"));
                PreparedStatement stmt = getInstance().getConnection().prepareStatement(query);
                stmt.setInt(1, resident.getResident_id());
                ResultSet specializations = stmt.executeQuery();
                while (specializations.next())
                {
                    Specialization s = new Specialization(specializations.getInt("specialization_id"), specializations.getString("name"));
                    resident.getSpecialization().add(s);
                }
                specializations.close();
                stmt.close();
                residents.add(resident);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return residents;
    }

    public static String getAllMatchings() {
        StringBuilder sb = new StringBuilder();
        try ( PreparedStatement ps1 = getInstance().getConnection().prepareStatement(
                "SELECT h.name AS hospital_name, r.name AS resident_name\n" +
                        "        FROM matchings m\n" +
                        "        JOIN hospitals h ON m.hospital_id = h.hospital_id\n" +
                        "        JOIN residents r ON m.resident_id = r.resident_id ")) {
            ResultSet resultSet = ps1.executeQuery();
            while (resultSet.next())
            {
                sb.append(resultSet.getString("resident_name")).append(" is assigned to ").append(resultSet.getString("hospital_name")).append(".\n");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return sb.toString();
    }
    public static void deleteMatchings(){
        try {
            connection.createStatement().execute("DELETE FROM matchings");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
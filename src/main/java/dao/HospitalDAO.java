package dao;

import classes.Hospital;

import classes.Specialization;
import db.Database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class HospitalDAO {

    private static final Connection con = Database.getInstance().getConnection();
    private static int getMaxHospitalId() {
        int max_id = 100;
        String sql = "SELECT MAX(hospital_id) FROM hospitals";

        try (PreparedStatement pstmt = con.prepareStatement(sql)) {
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                max_id = rs.getInt(1);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return max_id;
    }

        public static void insert(Hospital hospital) throws SQLException {
            String sql = "INSERT INTO hospitals (name, capacity, grade) VALUES (?, ?, ?)";

            try (PreparedStatement pstmt = con.prepareStatement(sql)) {

                pstmt.setString(1, hospital.getName());
                pstmt.setInt(2, hospital.getCapacity());
                pstmt.setInt(3, hospital.getGrade());
                pstmt.executeUpdate();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }

            Hospital hospital1 = HospitalDAO.findByName(hospital.getName());
            hospital1.setSpecialization(hospital.getSpecialization());
            hospital1.setResidentList(hospital.getResidentList());

            hospital = hospital1;
            for (Specialization specialization : hospital.getSpecialization()) {
                Specialization specialization1 = SpecializationDAO.findByName(specialization.getName());
                if (specialization1 == null) {
                    SpecializationDAO.insert(specialization);
                    specialization1 = SpecializationDAO.findByName(specialization.getName());
                }
                if (HospitalSpecializationDAO.findHospitalSpecialization(hospital, specialization1) == null)
                    HospitalSpecializationDAO.insert(hospital, specialization1);
            }
        }

    public static Hospital findById(int id) {
        String sql_statement = "SELECT * FROM hospitals WHERE hospital_id = ?";
        try (PreparedStatement pstmt = con.prepareStatement(sql_statement)) {
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return new Hospital(id, rs.getString("name"), rs.getInt("capacity"), rs.getInt("grade"));
            }
            else
                return null;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Hospital findByName(String name){
        String sql_statement = "SELECT * FROM hospitals WHERE name = ?";
        try (PreparedStatement pstmt = con.prepareStatement(sql_statement)) {
            pstmt.setString(1, name);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return new Hospital(rs.getInt("hospital_id"), name, rs.getInt("capacity"), rs.getInt("grade"));
            }
            else
                return null;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<Hospital> getAllHospitals() {
        String query = "select * from hospital_specialization hs join specializations s on hs.specialization_id = s.specialization_id where hs.hospital_id = ?";

        ArrayList<Hospital> hospitals = new ArrayList<>();
        try (PreparedStatement pstmt = con.prepareStatement("select * from hospitals")) {
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                Hospital hospital = new Hospital(rs.getInt("hospital_id"), rs.getString("name"),
                        rs.getInt("capacity"), rs.getInt("grade"));
                hospitals.add(hospital);
                //hospitals.add(HospitalDAO.findById(rs.getInt("hospital_id")));
                PreparedStatement stmt = con.prepareStatement(query);
                stmt.setInt(1, hospital.getHospital_id());
                ResultSet specializations = stmt.executeQuery();
                while (specializations.next())
                {
                    Specialization s = new Specialization(specializations.getInt("specialization_id"), specializations.getString("name"));
                    hospital.getSpecialization().add(s);
                }
                specializations.close();
                stmt.close();

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return hospitals;
    }
}
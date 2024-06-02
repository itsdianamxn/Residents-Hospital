package dao;

import classes.Hospital;
import classes.Matching;
import classes.Resident;
import db.Database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class MatchingDAO {
   /* private static final Connection con = Database.getInstance().getConnection();
    public static void insert(Matching matching){
        String sql = "INSERT INTO matchings (hospital_id, resident_id) VALUES (?, ?)";
        Matching matching1 = MatchingDAO.findMatch(matching.getHospital(), matching.getResident());
        if(matching1 != null){
            //System.out.println("Match already exists!");
            return;
        }
        try(PreparedStatement pstmt = con.prepareStatement(sql)){
            pstmt.setInt(1, matching.getHospital().getHospital_id());
            pstmt.setInt(2, matching.getResident().getResident_id());
            pstmt.executeUpdate();
        } catch (SQLException e){
            System.err.println("Error inserting matching: " + e);
        }
    }

    public static Matching findMatch(Hospital hospital, Resident resident){
        String sql = "SELECT * FROM matchings WHERE hospital_id = ? AND resident_id = ?";
        try(PreparedStatement pstmt = con.prepareStatement(sql)){
            pstmt.setInt(1, hospital.getHospital_id());
            pstmt.setInt(2, resident.getResident_id());
            ResultSet rs = pstmt.executeQuery();
            if(rs.next()){
                return new Matching(HospitalDAO.findById(rs.getInt("hospital_id")), ResidentDAO.findById(rs.getInt("resident_id")));
            }
        } catch (SQLException e){
            System.err.println("Error finding matching: " + e);
        }
        return null;
    }

    public static List<Matching> getAllMatchings() {
        List<Matching> matchingList = new ArrayList<>();
        String sql = "SELECT * FROM matchings";
        try (PreparedStatement pstmt = con.prepareStatement(sql)) {
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                Hospital hospital = HospitalDAO.findById(rs.getInt("hospital_id"));
                Resident resident = ResidentDAO.findById(rs.getInt("resident_id"));
                if(hospital!=null && resident!=null)
                    matchingList.add(new Matching(hospital, resident));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return matchingList;
    }

    public static List<Resident> getHospitalResidents(Hospital hospital){
        if(HospitalDAO.findById(hospital.getHospital_id()) == null)
            return null;
        List<Resident> residents = new ArrayList<>();
        String sql = "SELECT * FROM matchings WHERE hospital_id = ?";
        try(PreparedStatement pstmt = con.prepareStatement(sql)){
            pstmt.setInt(1, hospital.getHospital_id());
            ResultSet rs = pstmt.executeQuery();
            while(rs.next()){
                Resident resident = ResidentDAO.findById(rs.getInt("resident_id"));
                if(resident != null)
                    residents.add(resident);
            }
        } catch (SQLException e){
            System.err.println("Error getting hospital residents: " + e);
        }
        return residents;
    }

    public static Hospital getResidentAssignment(Resident resident){
        if(ResidentDAO.findById(resident.getResident_id()) == null)
            return null;
        String sql = "SELECT * FROM matchings WHERE resident_id = ?";
        try(PreparedStatement pstmt = con.prepareStatement(sql)){
            pstmt.setInt(1, resident.getResident_id());
            ResultSet rs = pstmt.executeQuery();
            if(rs.next()){
                return HospitalDAO.findById(rs.getInt("hospital_id"));
            }
        } catch (SQLException e){
            System.err.println("Error getting resident assignment: " + e);
        }
        return null;
    }

    public static List<String> getMatches() {
        List<String> matches = new ArrayList<>();
        String sql = "SELECT r.name AS resident_name, h.name AS hospital_name, s.name AS specialization_name " +
                "FROM residents r " +
                "JOIN residents_specialization rs ON r.resident_id = rs.resident_id " +
                "JOIN specializations s ON rs.specialization_id = s.specialization_id " +
                "JOIN hospital_specialization hs ON s.specialization_id = hs.specialization_id " +
                "JOIN hospitals h ON hs.hospital_id = h.hospital_id";

             try(PreparedStatement stmt = con.prepareStatement(sql)){
             ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                String residentName = rs.getString("resident_name");
                String hospitalName = rs.getString("hospital_name");
                String specializationName = rs.getString("specialization_name");

                String match = "Resident: " + residentName + ", Hospital: " + hospitalName + ", Specialization: " + specializationName;
                matches.add(match);
            }
             } catch (SQLException e){
                 System.err.println("Error getting resident assignment: " + e);
             }
        return matches;
    }*/
}
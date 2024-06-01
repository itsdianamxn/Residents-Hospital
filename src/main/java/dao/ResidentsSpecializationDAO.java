package dao;

import classes.Resident;
import classes.Specialization;
import db.Database;
import org.jgrapht.alg.util.Pair;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ResidentsSpecializationDAO {
    private static final Connection con = Database.getInstance().getConnection();
    public static void insert(Resident resident, Specialization specialization) {
        String sql = "INSERT INTO residents_specialization (resident_id, specialization_id) VALUES (?, ?)";

        try (PreparedStatement pstmt = con.prepareStatement(sql)) {
            pstmt.setInt(1, resident.getResident_id());
            pstmt.setInt(2, specialization.getSpecialization_id());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static Pair<Resident, Specialization> findResidentSpecialization(Resident resident, Specialization specialization) throws SQLException {
        String sql = "SELECT * from residents_specialization WHERE resident_id = ? AND specialization_id = ?";
        System.out.println(resident.getResident_id() + " " + specialization.getSpecialization_id());
        try (PreparedStatement pstmt = con.prepareStatement(sql)) {
            pstmt.setInt(1, resident.getResident_id());
            pstmt.setInt(2, specialization.getSpecialization_id());
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return new Pair<>(ResidentDAO.findById(rs.getInt("resident_id")),
                        SpecializationDAO.findById(rs.getInt("specialization_id")));
            }
            else
                return null;
        }catch (SQLException e){
            System.err.println(e.getMessage());
        }
        return null;
    }

    public void delete(Resident resident, Specialization specialization) {
        String sql = "DELETE FROM residents_specialization WHERE resident_id = ? AND specialization_id = ?";
        try (PreparedStatement pstmt = con.prepareStatement(sql)) {
            pstmt.setInt(1, resident.getResident_id());
            pstmt.setInt(2, specialization.getSpecialization_id());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Specialization> getResidentSpecializations(Resident resident) {
        String sql = "SELECT * from residents_specialization WHERE resident_id = ?";
        try (PreparedStatement pstmt = con.prepareStatement(sql)) {
            pstmt.setInt(1, resident.getResident_id());
            ResultSet rs = pstmt.executeQuery();
            List<Specialization> specializations = new ArrayList<>();
            while (rs.next()) {
                specializations.add(SpecializationDAO.findById(rs.getInt("specialization_id")));
            }
            return specializations;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
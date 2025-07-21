package DAO;

import Model.Account;
import Util.ConnectionUtil;

import java.sql.*;

public class AccountDAO {

    public Account getAccountByUsername(String username) {
        try (Connection conn = ConnectionUtil.getConnection()) {
            String sql = "SELECT * FROM account WHERE username = ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, username);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return new Account(
                    rs.getInt("account_id"),
                    rs.getString("username"),
                    rs.getString("password")
                );
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public Account createAccount(Account account) {
        try (Connection conn = ConnectionUtil.getConnection()) {
            String sql = "INSERT INTO account (username, password) VALUES (?, ?)";
            PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, account.getUsername());
            ps.setString(2, account.getPassword());

            int affectedRows = ps.executeUpdate();
            if (affectedRows == 1) { 
                // We don'nt want to affect multiple rows. 
                ResultSet rs = ps.getGeneratedKeys();
                if (rs.next()) {
                    account.setAccount_id(rs.getInt(1));
                    return account;
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}

package sample;

import java.sql.*;
import java.util.ArrayList;

class DataBaseConnection {

    private String sql;
    private PreparedStatement pStm;
    private Statement stm;
    private Connection c;

    DataBaseConnection() throws SQLException, ClassNotFoundException {
        c = DriverManager.getConnection("jdbc:postgresql://80.93.182.221:5432/kurs", "postgres", "kurs123");
        Class.forName("org.postgresql.Driver");
    }


    int pasteFileName(String filename) throws SQLException {
        if (checkFileName(filename) == 0) {
            sql = "Insert into filename(id, naml) values(nextval('fileid'), ?)";
            pStm = c.prepareStatement(sql);
            pStm.setString(1, filename);
            pStm.executeUpdate();
            pStm.close();
        }
        return checkFileName(filename);
    }

    int checkFileName(String filename) throws SQLException {
        sql = "select * from filename";
        stm = c.createStatement();
        ResultSet rs = stm.executeQuery(sql);
        while (rs.next()) {
            if (rs.getString("naml").equals(filename)) {
                return rs.getInt("id");
            }
        }
        stm.close();
        return 0;
    }

    void pasteLines(LineModel line) throws SQLException, ClassNotFoundException {
        sql = "insert into linel(id, lineid, speed, headvay, filename) values(nextval('lnid'), ?, ?, ?, ?)";
        pStm = c.prepareStatement(sql);
        pStm.setInt(1, line.getLineId());
        pStm.setDouble(2, line.getSpead());
        pStm.setDouble(3, line.getHeadvay());
        pStm.setInt(4, line.getFilename());
        pStm.executeUpdate();
        pStm.close();
    }

    ArrayList<LineModel> getLine(int id) throws SQLException, ClassNotFoundException {
        sql = "select * from linel where filename = " + id;
        stm = c.createStatement();
        ArrayList<LineModel> lines = new ArrayList<>();
        ResultSet rs = stm.executeQuery(sql);
        while (rs.next()) {
            lines.add(new LineModel(
                    rs.getFloat("speed"),
                    rs.getFloat("headvay"),
                    rs.getInt("lineid"),
                    rs.getInt("filename")));
        }
        stm.close();
        return lines;
    }

}

package net.fallara.db;

import java.io.Serializable;
import java.sql.*;

public class DBManager implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 1L;
    Connection cn = null;
    ServerConnectionBehavior scb = null;

    public DBManager() {

    }

    public DBManager(ServerConnectionBehavior conBehavior) {
        scb = conBehavior;
    }

    public boolean setConnectionBehavior(ServerConnectionBehavior value) {
        if (value == null) {
            throw new IllegalArgumentException("Please use a valid connection behavior");
        }
        scb = value;
        return true;
    }

    public boolean openConnection() {
        try {
            if (scb == null) {
                throw new IllegalArgumentException("Define "
                        + "a connection behavior");
            }
            if (cn != null) {
                closeConnection(false);
            }
            cn = scb.getConnection();
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return false;
        }

        return cn != null;
    }

    public boolean closeConnection(boolean keepAlive) {
        try {
            if (cn != null) {
                if (!cn.isClosed()) {
                    cn.close();
                }
            }
            if (!keepAlive) {
                cn = null;
            }
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            return false;
        }
        return true;
    }

    public boolean isConnected() {
        return cn != null;
    }

    public void validateConnected() {
        //connect to the db and open the connection
        if (!this.isConnected()) {
            if (!this.openConnection()) {
                //massive failure, log it
                System.out.println("Could not connect to the database...");
            }
        }
    }

    public boolean ExecuteNonQuery(String query) throws Exception {
        this.validateConnected();
        try {
            try (Statement st = cn.createStatement()) {
                int i = st.executeUpdate(query);
                if (i == -1) {
                    System.out.println("ERROR occured during SQL EXECUTE");
                    return false;
                } else {
                    System.out.println("Executed qry on " + i + " records.");
                }
            }
        } catch (Exception e) {
            throw e;
            //e.printStackTrace();
            //return false;
        }
        return true;
    }
    
    public int ExecuteUpdate(String query) throws Exception {
        this.validateConnected();
        try {
            try (Statement st = cn.createStatement()) {
                int i = st.executeUpdate(query);
                if (i == -1) {
                    System.out.println("ERROR occured during SQL EXECUTE");
                    return i;
                } else {
                    System.out.println("Executed qry on " + i + " records.");
                }
		return i;
            }
        } catch (Exception e) {
            throw e;
            //e.printStackTrace();
            //return false;
        }
    }

    public ResultSet ExecuteNonQueryWithKeys(String query) throws Exception {
        this.validateConnected();
        try {
            Statement st = cn.createStatement();
            int i = st.executeUpdate(query, Statement.RETURN_GENERATED_KEYS);
            if (i == -1) {
                //log it
                return null;
            }
            ResultSet rs = st.getGeneratedKeys();
            //st.close();
            return rs;
        } catch (Exception e) {
            throw e;
        }
    }

    public ResultSet ExecuteResultSet(String query) throws SQLException {
        this.validateConnected();

        PreparedStatement st = cn.prepareStatement(query);
        ResultSet rs = st.executeQuery();
        return rs;
    }

    public ResultSet ExecuteUpdateableResultSet(String query) throws SQLException {
        this.validateConnected();
        PreparedStatement st = cn.prepareStatement(query, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);

        ResultSet rs = st.executeQuery();
        return rs;
    }

    public Connection getConnection() {
        return cn;
    }

    public String getConnectionURL() {
        return scb.getConnectionURL();
    }

    public String getTablesSchemaQuery() {
        return scb.getTablesSchemaQuery();
    }
}

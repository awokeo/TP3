import java.sql.*;
import java.util.Date;


public class Main {
    public static void main(String[] args) {

        /* Load JDBC Driver. */
        try {
            Class.forName( "org.postgresql.Driver" );
        } catch ( ClassNotFoundException e ) {
            e.printStackTrace();
        }


        String url = "jdbc:postgresql://localhost/postgres";
        String user = "postgres";
        String pass = "aobinna";
        Connection connexion = null;

        try {
            connexion = DriverManager.getConnection( url, user, pass );
            Statement statement = connexion.createStatement();

            /* Requests to bdd will be here */
            System.out.println("Bdd Connected");
            //displayDepartment(connexion);
            //moveDepartment(connexion, 7499, 10);
            //displayTable(connexion, "emp");

            //DeptDAO Test
//            DAO<Dept> departmentDao = new DeptDAO(connexion);
//            Dept dept20 = departmentDao.find(20);
//            System.out.println(dept20);

            //EmpDAO Test
//            DAO<Emp> empDao = new EmpDAO(connexion);
//            Emp emp = empDao.find(7369);
//            System.out.println(emp);

            DAOFactory factory = new DAOFactory( connexion );

            DAO<Dept> departmentDao = factory.getDeptDAO();
            Dept dept20 = departmentDao.find(20);
            System.out.println(dept20);

        } catch ( SQLException e ) {
            e.printStackTrace();
        } finally {
            if ( connexion != null )
                try {
                    connexion.close();
                    //System.out.println("Bdd Disconnected");
                } catch ( SQLException ignore ) {
                    ignore.printStackTrace();
                }
        }

    }
    public static void displayDepartment(Connection connexion) throws SQLException {
        Statement statement = connexion.createStatement();
        ResultSet resultat = statement.
                executeQuery( "SELECT deptno, dname, loc FROM dept" );

        while ( resultat.next() ) {
            int deptno = resultat.getInt( "deptno");
            String dname = resultat.getString( "dname" );
            String loc = resultat.getString( "loc" );

            System.out.println("Department " + deptno + " is for "
                    + dname + " and located in " + loc);
        }
        resultat.close();
        statement.close();
    }

    public static void moveDepartment(Connection connexion, int empno, int newDeptno) throws SQLException {
        String query = "UPDATE emp SET deptno = ? WHERE empno = ?";
        PreparedStatement preparedStatement = connexion.prepareStatement(query);
        preparedStatement.setInt(1, newDeptno);
        preparedStatement.setInt(2, empno);
        int rowsAffected = preparedStatement.executeUpdate();
        if ( rowsAffected > 0 ) {
            System.out.println("Employee " + empno + " moved to department " + newDeptno);
        }
        else {
            System.out.println("No employee found with empno " + empno);
        }
        preparedStatement.close();
    }

    public static void displayTable(Connection connexion, String tableName) throws SQLException {
        Statement statement = connexion.createStatement();
        ResultSet resultat = statement.executeQuery("SELECT * FROM " + tableName);
        ResultSetMetaData rsmd = resultat.getMetaData();
        int columnsNumber = rsmd.getColumnCount();

        // Print column names
        for (int i = 1; i <= columnsNumber; i++) {
            System.out.print(rsmd.getColumnName(i) + " | ");
        }
        System.out.println();

        // Print rows
        while (resultat.next()) {
            for (int i = 1; i <= columnsNumber; i++) {
                System.out.print(resultat.getString(i) + " | ");
            }
            System.out.println();
        }
        resultat.close();
        statement.close();
    }

}

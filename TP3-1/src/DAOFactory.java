import java.sql.Connection;

public class DAOFactory {
    private Connection connect;

    public DAOFactory(Connection connect) { this.connect = connect; }

    public DAO<Dept> getDeptDAO() { return new DeptDAO(connect); }
    public DAO<Emp> getEmpDAO() { return new EmpDAO(connect); }
}
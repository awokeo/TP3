import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Set;
import java.util.HashSet;


public class EmpDAO extends DAO<Emp> {
    public EmpDAO(Connection connect) {
        super(connect);
    }

    @Override
    public Emp find(int id) {
        return find(id, new HashSet<>());
    }

    private Emp find(int id, Set<Integer> visited) {
        if (visited.contains(id)) {
            System.out.println("Circular manager detected for empno " + id);
            return null;
        }

        visited.add(id);
        Emp emp = null;

        try {
            PreparedStatement ps = connect.prepareStatement("SELECT * FROM emp WHERE empno = ?");
            ps.setInt(1, id);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                emp = new Emp();
                emp.setEmpNo(rs.getLong("empno"));
                emp.setEname(rs.getString("ename"));
                emp.setEfirst(rs.getString("efirst"));
                emp.setJob(rs.getString("job"));
                emp.setHireDate(rs.getDate("hiredate"));
                emp.setSal(rs.getInt("sal"));
                emp.setComm(rs.getInt("comm"));
                emp.setTel(rs.getInt("tel"));

                int mgrId = rs.getInt("mgr");
                if (!rs.wasNull() && mgrId != id) {
                    Emp manager = this.find(mgrId, visited);
                    emp.setMgr(manager);
                }

                int deptno = rs.getInt("deptno");
                DeptDAO deptDAO = new DeptDAO(connect);
                Dept dept = deptDAO.find(deptno);
                emp.setDepartment(dept);
            }

            rs.close();
            ps.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return emp;
    }

    @Override public boolean create(Emp object) { return false; }
    @Override public boolean update(Emp object) { return false; }
    @Override public boolean delete(Emp object) { return false; }
}
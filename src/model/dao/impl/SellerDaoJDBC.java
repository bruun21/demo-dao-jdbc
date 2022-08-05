package model.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import db.DB;
import db.DbException;
import model.dao.SellerDao;
import model.entities.Department;
import model.entities.Seller;

public class SellerDaoJDBC implements SellerDao {

	private Connection conn;

	public SellerDaoJDBC(Connection conn) {
		this.conn = conn;
	}

	@Override
	public void insert(Seller obj) {
		PreparedStatement st = null;

		try {
			st = conn.prepareStatement("INSERT INTO seller " + "(Name, Email, BirthDate, BaseSalary, DepartmentId) "
					+ "VALUES " + "(?, ?, ?, ?, ?)", Statement.RETURN_GENERATED_KEYS);

			st.setString(1, obj.getName());
			st.setString(2, obj.getEmail());
			st.setDate(3, new java.sql.Date(obj.getBirthDate().getTime()));
			st.setDouble(4, obj.getBaseSalary());
			st.setInt(5, obj.getDepartment().getId());
			int rouwsAffected = st.executeUpdate();

			if (rouwsAffected > 0) {
				ResultSet rs = st.getGeneratedKeys();
				if (rs.next()) {
					int id = rs.getInt(rouwsAffected);
					obj.setId(id);
				}
				DB.closeResultSet(rs);
			} else {
				throw new DbException("Unexpected error! No arrows affected!");
			}

		} catch (SQLException e) {
			throw new DbException(e.getMessage());
		} finally {
			DB.closeStatement(st);
		}
	}

	@Override
	public void update(Seller obj) {
		PreparedStatement st = null;

		try {
			st = conn.prepareStatement(
					"UPDATE seller "
					+ "SET Name = ?, Email = ?, BirthDate = ?, BaseSalary = ?, DepartmentId = ? "
					+ "WHERE Id = ?");

			st.setString(1, obj.getName());
			st.setString(2, obj.getEmail());
			st.setDate(3, new java.sql.Date(obj.getBirthDate().getTime()));
			st.setDouble(4, obj.getBaseSalary());
			st.setInt(5, obj.getDepartment().getId());
			st.setInt(6, obj.getId());
			
			st.executeUpdate();

		} catch (SQLException e) {
			throw new DbException(e.getMessage());
		} finally {
			DB.closeStatement(st);
		}

	}

	@Override
	public void deleteById(Integer id) {
		PreparedStatement st = null;
		
		try {
			st = conn.prepareStatement("DELETE FROM seller WHERE Id = ?");
			st.setInt(1,id);
			int rows = st.executeUpdate();
			if (rows == 0) {
				throw new DbException("Id not found");
			}	
		}
		catch (SQLException e) {
			throw new DbException(e.getMessage());
		}
		finally {
			DB.closeStatement(st);
		}

	}

	@Override
	public Seller findById(Integer id) {
		PreparedStatement st = null;
		ResultSet rs = null;
		
		try {
			st = conn.prepareStatement(
			"SELECT seller.*,department.Name as DepName "
			+ "FROM seller INNER JOIN department "
			+ "ON seller.DepartmentId = department.Id "
			+ "WHERE seller.Id = ?");
			
			st.setInt(1, id);
			rs = st.executeQuery();
			
			if (rs.next()) {
				Department dep = new Department();
				dep.setId(rs.getInt("DepartmentID"));
				dep.setName(rs.getNString("Depname"));

				Seller sell = new Seller();
				sell.setId(rs.getInt("Id"));
				sell.setName(rs.getString("Name"));
				sell.setEmail(rs.getString("Email"));
				sell.setBirthDate(rs.getDate("BirthDate"));
				sell.setBaseSalary(rs.getDouble("BaseSalary"));
				sell.setDepartment(dep);
				return sell;

			} else
				return null;
			
		} catch (SQLException e) {
			throw new DbException(e.getMessage());
		} finally {
			DB.closeStatement(st);
			DB.closeResultSet(rs);
		}
	}

	private Seller instantiateSeller(ResultSet rs, Department dep) throws SQLException {
		Seller sell = new Seller();
		sell.setId(rs.getInt("Id"));
		sell.setName(rs.getString("Name"));
		sell.setEmail(rs.getString("Email"));
		sell.setBirthDate(rs.getDate("BirthDate"));
		sell.setBaseSalary(rs.getDouble("BaseSalary"));
		sell.setDepartment(dep);
		return sell;

	}

	private Department instantiateDepartment(ResultSet rs) throws SQLException {
		Department dep = new Department();
		dep.setId(rs.getInt("DepartmentID"));
		dep.setName(rs.getNString("Depname"));
		return null;
	}

	@Override
	public List<Seller> findAll() {
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			st = conn.prepareStatement(
					"SELECT seller.*,department.Name as DepName " + "FROM seller INNER JOIN department "
							+ "ON seller.DepartmentId = department.Id " + "ORDER BY Name");

			rs = st.executeQuery();

			List<Seller> listSeller = new ArrayList<>();
			Map<Integer, Department> map = new HashMap<>();

			while (rs.next()) {

				Department dep = map.get(rs.getInt("DepartmentID"));

				if (dep == null) {
					dep = instantiateDepartment(rs);
					map.put(rs.getInt("DepartmentID"), dep);
				}

				Seller sell = instantiateSeller(rs, dep);
				listSeller.add(sell);

			}
			return listSeller;

		} catch (SQLException e) {
			throw new DbException(e.getMessage());
		} finally {
			DB.closeStatement(st);
			DB.closeResultSet(rs);
		}
	}

	@Override
	public List<Seller> findByDepartment(Department department) {
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			st = conn.prepareStatement(
					"SELECT seller.*,department.Name as DepName " + "FROM seller INNER JOIN department "
							+ "ON seller.DepartmentId = department.Id " + "WHERE DepartmentId = ? " + "ORDER BY Name");
			st.setInt(1, department.getId());
			rs = st.executeQuery();

			List<Seller> listSeller = new ArrayList<>();
			Map<Integer, Department> map = new HashMap<>();

			while (rs.next()) {

				Department dep = map.get(rs.getInt("DepartmentID"));

				if (dep == null) {
					dep = instantiateDepartment(rs);
					map.put(rs.getInt("DepartmentID"), dep);
				}

				Seller sell = instantiateSeller(rs, dep);
				listSeller.add(sell);

			}
			return listSeller;

		} catch (SQLException e) {
			throw new DbException(e.getMessage());
		} finally {
			DB.closeStatement(st);
			DB.closeResultSet(rs);
		}
	}

}

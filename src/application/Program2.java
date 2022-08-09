package application;

import java.util.Scanner;

import model.dao.DaoFactory;
import model.dao.DepartmentDao;
import model.entities.Department;

public class Program2 {

	public static void main(String[] args) {
		
		Scanner sc = new Scanner(System.in);
		
		DepartmentDao departmentDao = DaoFactory.createDepartmentDao();
		
		
		//System.out.println("\n==== Test 1: department insert ====");
		//Department department = new Department (null, "Camping");
		//departmentDao.insert(department);
		//System.out.println("Inserted! New id = " + department.getId());
		
		
		System.out.println("==== Test 2: seller findById ====");
		Department department = departmentDao.findById(2);
		System.out.println(department);
	
		sc.close();	
	}

}

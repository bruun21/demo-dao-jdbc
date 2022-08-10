package application;

import java.util.List;
import java.util.Scanner;

import model.dao.DaoFactory;
import model.dao.DepartmentDao;
import model.entities.Department;

public class Program2 {

	public static void main(String[] args) {
		
		Scanner sc = new Scanner(System.in);
		
		DepartmentDao departmentDao = DaoFactory.createDepartmentDao();
		
		
		System.out.println("\n==== Test 1: department insert ====");
		Department department = new Department (null, "Camping");
		departmentDao.insert(department);
		System.out.println("Inserted! New id = " + department.getId());
		
		
		System.out.println("==== Test 2: department findById ====");
		department = departmentDao.findById(2);
		System.out.println(department);
		
		
		System.out.println("\n==== Test 3: department update ====");
		department = departmentDao.findById(4);
		System.out.println(department);
		department.setName("Books");
		departmentDao.update(department);
		System.out.println("Update Completed");
		
		
		System.out.println("\n==== Test 4: department delete ====");
		System.out.println("Enter id for delete test");
		int id = sc.nextInt();
		departmentDao.deleteById(id);
		System.out.println("Delete completed");
		
		System.out.println("\n==== Test 5: department findAll ====");
		List <Department> listDepartment = departmentDao.findAll();
		for(Department obj: listDepartment)
		System.out.println(obj);

	
		sc.close();	
	}

}

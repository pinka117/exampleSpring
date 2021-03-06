package com.example.controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.example.model.Employee;
import com.example.service.EmployeeService;

@RestController
public class EmployeeRestController {
@Autowired
private EmployeeService employeeService;
@GetMapping("/api/employees")
public List<Employee> allEmployees() {
return employeeService.getAllEmployees();
}

@GetMapping("/api/employees/{id}")
public Employee oneEmployee(@PathVariable long id) {
return employeeService.getEmployeeById(id);
}

}

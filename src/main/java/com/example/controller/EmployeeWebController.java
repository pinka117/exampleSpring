package com.example.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import com.example.model.Employee;
import com.example.service.EmployeeService;
@Controller
public class EmployeeWebController {
	private EmployeeService employeeService;
	@Autowired
	public EmployeeWebController(EmployeeService employeeService) {
	this.employeeService = employeeService;
	}
	@GetMapping("/")
	public String index(Model model) {
		List<Employee> allEmployees = employeeService.getAllEmployees();
		model.addAttribute("employees", allEmployees);
		model.addAttribute("message",
		allEmployees.isEmpty() ? "No employee" : "");
		return "index";
	}
	@GetMapping("/edit/{id}")
	public String editEmployee(@PathVariable long id, Model model) {
	Employee employeeById = employeeService.getEmployeeById(id);
	model.addAttribute("employee", employeeById);
	model.addAttribute("message",
	employeeById == null ? "No employee found with id: " + id : "");
	return "edit";
	}
	@PostMapping("/save")
	public String saveEmployee(Employee employee) {
	employeeService.saveEmployee(employee);
	return "redirect:/";
	}
	@GetMapping("/new")
	public String newEmployee(Model model) {
	Employee employee = new Employee();
	model.addAttribute("employee", employee);
	model.addAttribute("message", "");
	return "edit";
	}
}

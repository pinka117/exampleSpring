package com.example.controller;

import static org.hamcrest.CoreMatchers.nullValue;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.ModelAndViewAssert;
import org.springframework.test.web.servlet.MockMvc;

import com.example.model.Employee;
import com.example.service.EmployeeService;

@RunWith(SpringRunner.class)
@WebMvcTest(controllers = EmployeeWebController.class)
public class EmployeeWebControllerTest {
	@Autowired
	private MockMvc mvc;
	@MockBean
	private EmployeeService employeeService;

	@Test
	public void testStatus200() throws Exception {
		mvc.perform(get("/")).andExpect(status().is2xxSuccessful());
	}

	@Test
	public void testReturnHomeView() throws Exception {
		ModelAndViewAssert.assertViewName(mvc.perform(get("/")).andReturn().getModelAndView(), "index");
	}

	@Test
	public void testEmptyEmployeeList() throws Exception {
		mvc.perform(get("/")).andExpect(view().name("index"))
				.andExpect(model().attribute("employees", new ArrayList<Employee>()))
				.andExpect(model().attribute("message", "No employee"));
		verify(employeeService).getAllEmployees();
	}

	@Test
	public void testNotEmptyEmployeeList() throws Exception {
		List<Employee> employees = Arrays.asList(new Employee(1L, "test", 1000));
		when(employeeService.getAllEmployees()).thenReturn(employees);
		mvc.perform(get("/")).andExpect(view().name("index")).andExpect(model().attribute("employees", employees))
				.andExpect(model().attribute("message", ""));
		verify(employeeService).getAllEmployees();
	}

	@Test
	public void testSingleEmployee() throws Exception {
		Employee employee = new Employee(1L, "test", 1000);
		when(employeeService.getEmployeeById(1)).thenReturn(employee);
		mvc.perform(get("/edit/1")).andExpect(view().name("edit")).andExpect(model().attribute("employee", employee))
				.andExpect(model().attribute("message", ""));
		verify(employeeService).getEmployeeById(1);
	}

	@Test
	public void testSingleEmployeeNotFound() throws Exception {
		mvc.perform(get("/edit/1")).andExpect(view().name("edit")).andExpect(model().attribute("employee", nullValue()))
				.andExpect(model().attribute("message", "No employee found with id: 1"));
		verify(employeeService).getEmployeeById(1);
	}

	@Test
	public void testPostEmployee() throws Exception {
		Employee employee = new Employee(null, "created", 1000);
		mvc.perform(post("/save").param("name", employee.getName()).param("salary", "" + employee.getSalary()))
				.andExpect(view().name("redirect:/")); // go back to the main page
		verify(employeeService).saveEmployee(employee);
	}

	@Test
	public void testPostEmployeeWithoutId() throws Exception {
		Employee employee = new Employee(null, "created", 1000);
		mvc.perform(post("/save").param("name", employee.getName()).param("salary", "" + employee.getSalary()))
				.andExpect(view().name("redirect:/")); // go back to the main page
		verify(employeeService).saveEmployee(employee);
	}

	@Test
	public void testPostEmployeeWithId() throws Exception {
		Employee employee = new Employee(1L, "created", 1000);
		mvc.perform(post("/save").param("id", employee.getId().toString()).param("name", employee.getName())
				.param("salary", "" + employee.getSalary())).andExpect(view().name("redirect:/")); // go back to the
																									// main page
		verify(employeeService).saveEmployee(employee);
	}
	@Test
	public void testNewEmployee() throws Exception {
	mvc.perform(get("/new"))
	.andExpect(view().name("edit"))
	.andExpect(model().attribute("employee", new Employee()))
	.andExpect(model().attribute("message", ""));
	verifyZeroInteractions(employeeService);
	}
}

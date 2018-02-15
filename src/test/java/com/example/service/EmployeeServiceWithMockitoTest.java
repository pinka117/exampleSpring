package com.example.service;

import static org.junit.Assert.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.*;
import java.util.Arrays;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import com.example.model.Employee;
import com.example.repository.EmployeeRepository;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = EmployeeService.class)
public class EmployeeServiceWithMockitoTest {
	@MockBean
	private EmployeeRepository employeeRepository;
	@Autowired
	private EmployeeService employeeService;

	@Test
	public void test_getMaxSalariedEmployee_withNoEmployees() {
		assertNull(employeeService.getMaxSalariedEmployee());
		verify(employeeRepository).findAll();
	}

	@Test
	public void test_getMaxSalariedEmployee_withEmployees() {
		given(employeeRepository.findAll()).willReturn(Arrays.asList(new Employee(1L, "first", 1000),
				new Employee(2L, "second", 5000), new Employee(3L, "third", 2000)));
		assertThat(employeeService.getMaxSalariedEmployee().getName()).isEqualTo("second");
		verify(employeeRepository).findAll();
	}

	@Test
	public void test_getAllEmployees_withEmployees() {
		Employee employee1 = new Employee(1L, "first", 1000);
		Employee employee2 = new Employee(2L, "second", 5000);
		given(employeeRepository.findAll()).willReturn(Arrays.asList(employee1, employee2));
		assertThat(employeeService.getAllEmployees()).containsExactly(employee1, employee2);
		verify(employeeRepository).findAll();
	}

	@Test
	public void test_getAllEmployees_empty() {
		assertThat(employeeService.getAllEmployees()).isEmpty();
		;
		verify(employeeRepository).findAll();
	}

	@Test
	public void test_getEmployeeById_found() {
		Employee employee = new Employee(1L, "employee", 1000);
		given(employeeRepository.findOne((long) 1))
		.willReturn(employee);
		assertThat(employeeService.getEmployeeById(1))
		.isSameAs(employee);
		verify(employeeRepository).findOne((long) 1);
	}



	@Test
	public void test_getEmployeeById_notFound() {
		assertThat(employeeService.getEmployeeById(1)).isNull();
		verify(employeeRepository).findOne((long) 1);
	}
}
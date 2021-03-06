package com.example.controller;

import static org.hamcrest.CoreMatchers.*;
import static org.mockito.BDDMockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import java.util.Arrays;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import com.example.model.Employee;
import com.example.service.EmployeeService;

@RunWith(SpringRunner.class)
@WebMvcTest(controllers = EmployeeRestController.class)
public class EmployeeRestControllerTest {
@Autowired
private MockMvc mvc;
@MockBean
private EmployeeService employeeService;
@Test
public void testAllEmployees() throws Exception {
given(employeeService.getAllEmployees()).
willReturn(Arrays.asList(
new Employee(1L, "first", 1000),
new Employee(2L, "second", 5000)
));
this.mvc.perform(get("/api/employees")
.accept(MediaType.APPLICATION_JSON))
.andExpect(status().isOk())
.andExpect(jsonPath("$[0].id", is(1)))
.andExpect(jsonPath("$[0].name", is("first")))
.andExpect(jsonPath("$[0].salary", is(1000)))
.andExpect(jsonPath("$[1].id", is(2)))
.andExpect(jsonPath("$[1].name", is("second")))
.andExpect(jsonPath("$[1].salary", is(5000)));
verify(employeeService, times(1)).getAllEmployees();
}
@Test
public void testAllEmployeesEmpty() throws Exception {
this.mvc.perform(get("/api/employees")
.accept(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
.andExpect(content().json("[]"));
// the above checks that the content is an empty JSON list
verify(employeeService, times(1)).getAllEmployees();
}
@Test
public void testFindByIdWithExistingEmployee() throws Exception {
given(employeeService.getEmployeeById(1)).
willReturn(new Employee(1L, "first", 1000));
this.mvc.perform(get("/api/employees/1")
.accept(MediaType.APPLICATION_JSON))
.andExpect(status().isOk())
.andExpect(jsonPath("$.id", is(1)))
.andExpect(jsonPath("$.name", is("first")))
.andExpect(jsonPath("$.salary", is(1000)));
verify(employeeService, times(1)).getEmployeeById(1);
}
@Test
public void testFindByIdWithNotFoundEmployee() throws Exception {
this.mvc.perform(get("/api/employees/1")
.accept(MediaType.APPLICATION_JSON))
.andExpect(status().isOk())
.andExpect(content().string(""));
// the above checks that the content is empty
// which is different from an empty JSON list
verify(employeeService, times(1)).getEmployeeById(1);
}

}

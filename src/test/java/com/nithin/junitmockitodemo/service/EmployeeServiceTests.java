package com.nithin.junitmockitodemo.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import com.nithin.junitmockitodemo.exception.ResourceNotFoundException;
import com.nithin.junitmockitodemo.model.Employee;
import com.nithin.junitmockitodemo.repository.EmployeeRepository;
import com.nithin.junitmockitodemo.service.impl.EmployeeServiceImpl;

@ExtendWith(MockitoExtension.class)
public class EmployeeServiceTests {

	@Mock
	private EmployeeRepository employeeRepository;

	@InjectMocks
	private EmployeeServiceImpl employeeService;

	private Employee employee;

	@BeforeEach
	public void setup() {
		// employeeRepository = Mockito.mock(EmployeeRepository.class);
		// employeeService = new EmployeeServiceImpl(employeeRepository);
		employee = Employee.builder().id(1L).firstName("Ramesh").lastName("Fadatare").email("ramesh@gmail.com").build();
	}

	// JUnit test for saveEmployee method
	@DisplayName("JUnit test for saveEmployee method")
	@Test
	public void givenEmployeeObject_whenSaveEmployee_thenReturnEmployeeObject() {
		// given - precondition or setup
		Mockito.when(employeeRepository.findByEmail(employee.getEmail())).thenReturn(Optional.empty());
		Mockito.when(employeeRepository.save(employee)).thenReturn(employee);

		System.out.println(employeeRepository);
		System.out.println(employeeService);

		// when - action or the behaviour that we are going test
		Employee savedEmployee = employeeService.saveEmployee(employee);

		System.out.println(savedEmployee);
		// then - verify the output
		assertThat(savedEmployee).isNotNull();
	}
	
    // JUnit test for saveEmployee method
    @DisplayName("JUnit test for saveEmployee method which throws exception")
    @Test
    public void givenExistingEmail_whenSaveEmployee_thenThrowsException(){
        // given - precondition or setup
        Mockito.when(employeeRepository.findByEmail(employee.getEmail())).thenReturn(Optional.of(employee));

        System.out.println(employeeRepository);
        System.out.println(employeeService);

        // when -  action or the behaviour that we are going test
        assertThrows(ResourceNotFoundException.class, () -> {
            employeeService.saveEmployee(employee);
        });

        // then
        verify(employeeRepository, never()).save(any(Employee.class));
    }
    
    // JUnit test for getAllEmployees method
    @DisplayName("JUnit test for getAllEmployees method")
    @Test
    public void givenEmployeesList_whenGetAllEmployees_thenReturnEmployeesList(){
        // given - precondition or setup

        Employee employee1 = Employee.builder()
                .id(2L)
                .firstName("Tony")
                .lastName("Stark")
                .email("tony@gmail.com")
                .build();

        when(employeeRepository.findAll()).thenReturn(List.of(employee,employee1));

        // when -  action or the behaviour that we are going test
        List<Employee> employeeList = employeeService.getAllEmployees();

        // then - verify the output
        assertThat(employeeList).isNotNull();
        assertThat(employeeList.size()).isEqualTo(2);
    }
	
    // JUnit test for getAllEmployees method
    @DisplayName("JUnit test for getAllEmployees method (negative scenario)")
    @Test
    public void givenEmptyEmployeesList_whenGetAllEmployees_thenReturnEmptyEmployeesList(){
        // given - precondition or setup

        Employee employee1 = Employee.builder()
                .id(2L)
                .firstName("Tony")
                .lastName("Stark")
                .email("tony@gmail.com")
                .build();

        when(employeeRepository.findAll()).thenReturn(Collections.emptyList());

        // when -  action or the behaviour that we are going test
        List<Employee> employeeList = employeeService.getAllEmployees();

        // then - verify the output
        assertThat(employeeList).isEmpty();
        assertThat(employeeList.size()).isEqualTo(0);
    }
    
    // JUnit test for updateEmployee method
    @DisplayName("JUnit test for updateEmployee method")
    @Test
    public void givenEmployeeObject_whenUpdateEmployee_thenReturnUpdatedEmployee(){
        // given - precondition or setup
        when(employeeRepository.save(employee)).thenReturn(employee);
        employee.setEmail("ram@gmail.com");
        employee.setFirstName("Ram");
        // when -  action or the behaviour that we are going test
        Employee updatedEmployee = employeeService.updateEmployee(employee);

        // then - verify the output
        assertThat(updatedEmployee.getEmail()).isEqualTo("ram@gmail.com");
        assertThat(updatedEmployee.getFirstName()).isEqualTo("Ram");
    }
    
    // JUnit test for deleteEmployee method
    @DisplayName("JUnit test for deleteEmployee method")
    @Test
    public void givenEmployeeId_whenDeleteEmployee_thenNothing(){
        // given - precondition or setup
        long employeeId = 1L;

        doNothing().when(employeeRepository).deleteById(employeeId);

        // when -  action or the behaviour that we are going test
        employeeService.deleteEmployee(employeeId);

        // then - verify the output
        verify(employeeRepository, times(1)).deleteById(employeeId);
    }

    
    
}

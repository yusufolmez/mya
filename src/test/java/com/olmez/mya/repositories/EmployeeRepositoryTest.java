package com.olmez.mya.repositories;

import com.olmez.mya.MyaApplicationTest;
import com.olmez.mya.model.Employee;
import com.olmez.mya.services.TestRepoCleanerService;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import com.olmez.mya.utility.TestSource;

/**
 * Test classes use test database!
 */
@SpringBootTest(classes = MyaApplicationTest.class)
@TestPropertySource(TestSource.TEST_PROP_SOURCE)
@ActiveProfiles(TestSource.AC_PROFILE)
class EmployeeRepositoryTest {

	@Autowired
	private EmployeeRepository repository;
	@Autowired
	private TestRepoCleanerService cleanerService;

	@BeforeEach
	public void setup() {
		cleanerService.clear();
	}

	@Test
	void testFindByName() {
		// arrange
		var emp = new Employee("Emp1name", "emp@email.com");
		emp = repository.save(emp);
		var emp2 = new Employee("Emp2name", "emp2@email.com");
		emp2 = repository.save(emp2);

		// act
		var list = repository.findByName(emp.getName());

		// assert
		assertThat(list).hasSize(1);
		assertThat(list.get(0)).isEqualTo(emp);
	}

}
package com.example.demo.data;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.model.Employee;

/**
 * @author <a href="mailto:rodrigo.moncada@live.com">Rodrigo Moncada</a>
 *
 */
@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Integer>
{
	public List<Employee> findAllByStatus(boolean status);
	
	public Optional<Employee> findByIdAndStatus(Integer id, boolean status);
}

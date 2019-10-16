package com.example.demo.controller.helper;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

import org.springframework.hateoas.Resource;
import org.springframework.hateoas.ResourceAssembler;
import org.springframework.stereotype.Component;

import com.example.demo.controller.EmployeeController;
import com.example.demo.model.Employee;
import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * @author <a href="mailto:rodrigo.moncada@ibm.com">Rodrigo Moncada</a>
 *
 */
@Component
@JsonInclude(JsonInclude.Include.NON_NULL)
public class EmployeeResourceAssembler implements ResourceAssembler<Employee, Resource<Employee>> {

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.springframework.hateoas.ResourceAssembler#toResource(java.lang.Object)
	 */
	public Resource<Employee> toResource(Employee employee) {
		Resource<Employee> resource = new Resource<Employee>(employee);
		resource.add(linkTo(methodOn(EmployeeController.class).retrieveEmployeeById(employee.getId())).withSelfRel());
		resource.add(linkTo(methodOn(EmployeeController.class).retrieveEmployees()).withRel("employees"));
	    
	    return resource;
    }
}

package com.example.demo.model;

import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;
import javax.validation.constraints.PastOrPresent;

import org.hibernate.annotations.GenericGenerator;
import org.springframework.format.annotation.DateTimeFormat;

import com.example.demo.model.component.Adult;
import com.google.gson.GsonBuilder;

/**
 * @author <a href="mailto:rodrigo.moncada@live.com">Rodrigo Moncada</a>
 *
 */
@Entity
public class Employee {
	
	@Id
	@Column
	//@NotNull
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "id_native")
	@GenericGenerator(name = "id_native", strategy = "native")
	private Integer id;
	
	@Column
	@NotNull
	@NotEmpty(message = "Please provide a name")
    private String firstName;
	
	@Column
	@NotNull
	@NotEmpty(message = "Please provide a middle initial")
    private String middleInitial;
	
	@Column
	@NotNull
	@NotEmpty(message = "Please provide a last name")
    private String lastName;
	
	//@Column(name="date_birth")
	@DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
	@Column
	@Past
	//@NotNull
	@Adult(message = "must be a higher 18 years old")
	//@Pattern(regexp="^\\d{4}([\\-/.])(0?[1-9]|1[1-2])\\1(3[01]|[12][0-9]|0?[1-9])$", 
	//	message="Please add a date valid format: yyyy-mm-dd")
	//@NotEmpty(message = "Please provide a date of birth")
	//@NotEmpty(message = "Please provide a birthday")
    private LocalDate dateOfBirth;
	
	//@Column(name="date_employment")
	@Column
	//@DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
	@PastOrPresent
	//@Pattern(regexp="^\\d{4}([\\-/.])(0?[1-9]|1[1-2])\\1(3[01]|[12][0-9]|0?[1-9])$", 
	//	message="Please add a date valid format: yyyy-mm-dd")
    private LocalDate dateOfEmployment;
	
	@Column
	@NotNull
    private Boolean status;
    
	/**
	 * Constructor.
	 */
	public Employee() {
		super();
	}
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	public String getMiddleInitial() {
		return middleInitial;
	}
	public void setMiddleInitial(String middleInitial) {
		this.middleInitial = middleInitial;
	}
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	public LocalDate getDateOfBirth() {
		return dateOfBirth;
	}
	public void setDateOfBirth(LocalDate dateOfBirth) {
		this.dateOfBirth = dateOfBirth;
	}
	public LocalDate getDateOfEmployment() {
		return dateOfEmployment;
	}
	public void setDateOfEmployment(LocalDate dateOfEmployment) {
		this.dateOfEmployment = dateOfEmployment;
	}
	public Boolean isStatus() {
		return status;
	}
	public void setStatus(Boolean status) {
		this.status = status;
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return new GsonBuilder().create().toJson(this);
	}
}

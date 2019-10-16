package com.example.demo.testsuite;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import com.example.demo.controller.EmployeeControllerTest;
import com.example.demo.controller.UserControllerTest;

/**
 * @author <a href="mailto:moncada@ibm.com">Rodrigo Moncada</a>
 *
 */
@RunWith(Suite.class)

@SuiteClasses({
	EmployeeControllerTest.class,
	UserControllerTest.class
})

//Class only for Test Suite only
public class DemoTestSuite {
	//no needed code here
}
package com.example.demo.config;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Type;
import java.time.LocalDate;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.ResourceUtils;

import com.example.demo.data.EmployeeRepository;
import com.example.demo.model.Employee;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

/**
 * @author <a href="mailto:rodrigo.moncada@ibm.com">Rodrigo Moncada</a>
 *
 */
@Configuration
public class DataLoader {

	/**
	 * Logger instance
	 * 
	 * @see Logger
	 * @see LogManager
	 */
	private static final Logger logger = LogManager.getLogger(DataLoader.class);
	
	 /**
     * This method pre-loads the embebed data base with a set of pre made
     * employees.
     * 
     * @param repository
     * @return
     */
    @Bean
    public CommandLineRunner loadData(EmployeeRepository repository) {
        return args -> {
            Gson gson = new GsonBuilder().registerTypeAdapter(LocalDate.class, new JsonDeserializer<LocalDate>() {
                @Override
                public LocalDate deserialize(JsonElement json, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
                    return LocalDate.parse(json.getAsJsonPrimitive().getAsString());
                }
            }).create();
            
            File file = ResourceUtils.getFile("classpath:employees.json");
            try (FileReader reader = new FileReader(file)) {
            	JsonArray emp = gson.fromJson(reader, JsonArray.class);
            	emp.forEach(e -> repository.save(gson.fromJson(e.getAsJsonObject().get("employee"), Employee.class)));
            	
            } catch (FileNotFoundException e) {
            	logger.error("Ocurred an error trying to Load Initial Data: " + e.getMessage());
            } catch (IOException e) {
            	logger.error("Ocurred an error trying to Load Initial Data: " + e.getMessage());
            }
        };
    }
}

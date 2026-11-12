package com.vishal;

import com.jayway.jsonpath.JsonPath;
import com.vishal.repo.ProductRepo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(properties = {
		"spring.datasource.url=jdbc:h2:mem:rds_demo_test;DB_CLOSE_DELAY=-1",
		"spring.datasource.driver-class-name=org.h2.Driver",
		"spring.datasource.username=sa",
		"spring.datasource.password=",
		"spring.jpa.hibernate.ddl-auto=create-drop",
		"spring.jpa.database-platform=org.hibernate.dialect.H2Dialect"
})

@AutoConfigureMockMvc
class RdsDemoApplicationTests {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ProductRepo productRepo;

	@BeforeEach
	void clearProducts() {
		productRepo.deleteAll();
	}

	@Test
	void contextLoads() {
	}

	@Test
	void createsAndListsProducts() throws Exception {
		mockMvc.perform(post("/products")
					.contentType(MediaType.APPLICATION_JSON)
					.content("{\"name\":\"Keyboard\",\"price\":49.99,\"quantity\":12}"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.id").isNumber())
				.andExpect(jsonPath("$.name").value("Keyboard"))
				.andExpect(jsonPath("$.price").value(49.99))
				.andExpect(jsonPath("$.quantity").value(12));

		mockMvc.perform(get("/products"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$", hasSize(1)))
				.andExpect(jsonPath("$[0].name").value("Keyboard"));
	}

	@Test
	void readsUpdatesAndDeletesProduct() throws Exception {
		MvcResult createResult = mockMvc.perform(post("/products")
					.contentType(MediaType.APPLICATION_JSON)
					.content("{\"name\":\"Mouse\",\"price\":19.5,\"quantity\":8}"))
				.andExpect(status().isOk())
				.andReturn();
		String id = JsonPath.read(createResult.getResponse().getContentAsString(), "$.id").toString();

		mockMvc.perform(get("/products/{id}", id))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.name").value("Mouse"));

		mockMvc.perform(put("/products/{id}", id)
					.contentType(MediaType.APPLICATION_JSON)
					.content("{\"name\":\"Wireless Mouse\",\"price\":24.0,\"quantity\":5}"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.name").value("Wireless Mouse"))
				.andExpect(jsonPath("$.price").value(24.0))
				.andExpect(jsonPath("$.quantity").value(5));

		mockMvc.perform(delete("/products/{id}", id))
				.andExpect(status().isOk());

		mockMvc.perform(get("/products"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$", hasSize(0)));
	}
}

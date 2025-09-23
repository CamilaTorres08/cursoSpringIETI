package edu.eci.ieti.integrador;

import edu.eci.ieti.integrador.controller.WeatherReportController;
import edu.eci.ieti.integrador.data.WeatherReport;
import edu.eci.ieti.integrador.service.WeatherReportService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class WeatherApplicationTests {

	@Mock
	WeatherReportService weatherReportService;

	MockMvc mvc;
	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);
		WeatherReportController controller = new WeatherReportController(weatherReportService);
		mvc = MockMvcBuilders.standaloneSetup(controller).build();
	}
	@Test
	void getWeatherReport_ok() throws Exception {
		WeatherReport report = new WeatherReport();
		report.setTemperature(18.2);
		report.setHumidity(80);

		double lat = 37.8267;
		double lon = -122.4233;

		when(weatherReportService.getWeatherReport(lat, lon)).thenReturn(report);

		mvc.perform(
						get("/v1/api/weather-report")
								.param("latitude", String.valueOf(lat))
								.param("longitude", String.valueOf(lon))
								.accept(MediaType.APPLICATION_JSON)
				)
				.andExpect(status().isOk())
				.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.temperature").value(18.2))
				.andExpect(jsonPath("$.humidity").value(80));
	}

}

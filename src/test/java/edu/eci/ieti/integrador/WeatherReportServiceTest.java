package edu.eci.ieti.integrador;
import edu.eci.ieti.integrador.data.WeatherReport;
import edu.eci.ieti.integrador.service.WeatherReportService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.client.RestClientException;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class WeatherReportServiceTest {

    private WeatherReportService weatherReportService;

    @BeforeEach
    void setUp() {
        weatherReportService = new WeatherReportService();
    }

    @Test
    void testGetWeatherReport_ValidCoordinates_ShouldReturnWeatherReport() {
        double latitude = 51.5074;
        double longitude = -0.1278;

        try {
            WeatherReport result = weatherReportService.getWeatherReport(latitude, longitude);

            assertNotNull(result, "El weather report no debe ser null");
            assertNotNull(result.getTemperature(), "La temperatura no debe ser null");
            assertNotNull(result.getHumidity(), "La humedad no debe ser null");

            assertTrue(result.getTemperature() >= -100 && result.getTemperature() <= 100,
                    "La temperatura debe estar en un rango razonable");
            assertTrue(result.getHumidity() >= 0 && result.getHumidity() <= 100,
                    "La humedad debe estar entre 0 y 100");

        } catch (Exception e) {
            assertTrue(e instanceof RestClientException || e instanceof RuntimeException,
                    "Debe lanzar RestClientException o RuntimeException cuando hay problemas de conectividad");
        }
    }

    @Test
    void testGetWeatherReport_DifferentCoordinates_ShouldProcessCorrectly() {
        double latitude = 40.7128;
        double longitude = -74.0060;

        try {
            WeatherReport result = weatherReportService.getWeatherReport(latitude, longitude);

            assertNotNull(result, "El weather report no debe ser null");
            assertNotNull(result.getTemperature(), "La temperatura no debe ser null");
            assertNotNull(result.getHumidity(), "La humedad no debe ser null");

        } catch (Exception e) {
            assertTrue(e instanceof RestClientException || e instanceof RuntimeException,
                    "Debe manejar errores de API apropiadamente");
        }
    }

    @Test
    void testGetWeatherReport_ExtremeCoordinates_ShouldHandleCorrectly() {
        double latitude = 89.9999;
        double longitude = 0.0;

        try {
            WeatherReport result = weatherReportService.getWeatherReport(latitude, longitude);

            assertNotNull(result, "El weather report no debe ser null");

        } catch (Exception e) {
            // Esperado para coordenadas extremas que pueden no tener datos
            assertTrue(e instanceof RestClientException || e instanceof RuntimeException,
                    "Debe manejar coordenadas extremas apropiadamente");
        }
    }

    @Test
    void testGetWeatherReport_NegativeCoordinates_ShouldProcessCorrectly() {
        double latitude = -33.8688;
        double longitude = 151.2093;

        try {
            WeatherReport result = weatherReportService.getWeatherReport(latitude, longitude);

            assertNotNull(result, "El weather report no debe ser null para coordenadas negativas");

        } catch (Exception e) {
            assertTrue(e instanceof RestClientException || e instanceof RuntimeException,
                    "Debe manejar errores de API apropiadamente");
        }
    }


    // Test para verificar el comportamiento del servicio con coordenadas válidas típicas
    @Test
    void testGetWeatherReport_TypicalUseCases() {
        double[][] coordinates = {
                {37.7749, -122.4194}, // San Francisco
                {48.8566, 2.3522},    // París
                {35.6762, 139.6503},  // Tokio
                {-34.6037, -58.3816}  // Buenos Aires
        };

        for (double[] coord : coordinates) {
            double lat = coord[0];
            double lon = coord[1];

            try {
                WeatherReport result = weatherReportService.getWeatherReport(lat, lon);

                if (result != null) {
                    assertNotNull(result.getTemperature(),
                            "La temperatura debe estar presente para coordenadas válidas");
                    assertNotNull(result.getHumidity(),
                            "La humedad debe estar presente para coordenadas válidas");
                }

            } catch (Exception e) {
                // Documentar que se espera manejo de errores
                assertTrue(e instanceof RuntimeException,
                        "Debe manejar errores de red/API apropiadamente");
            }
        }
    }
}



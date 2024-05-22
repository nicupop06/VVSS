package tasks.blackbox;

import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import tasks.services.DateService;
import tasks.services.TasksService;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class DateServiceTest {
    private DateService dateService;
    @BeforeEach
    public void setUp() {
        TasksService mockTasksService = mock(TasksService.class);
        dateService = new DateService(mockTasksService);
    }

    @AfterEach
    public void tearDown() {
    }

    @Order(1)
    @Test
    @DisplayName("getDateMergedWithTimeValid time and date representatives")
    @Tag("Valid")
    @Tag("ECP")
    public void getDateMergedWithTimeECPValidInputs() {
        // arrange
        String time = "10:00";
        Date noTimeDate = new GregorianCalendar(2021, Calendar.MARCH, 12).getTime();
        Date expectedDate = new GregorianCalendar(2021, Calendar.MARCH, 12, 10, 0).getTime();

        // act
        Date date = dateService.getDateMergedWithTime(time, noTimeDate);

        // assert
        assertEquals(expectedDate, date);
    }

    @Order(2)
    @Test
    @DisplayName("getDateMergedWithTimeInvalid invalid hour")
    @Tag("Invalid")
    @Tag("ECP")
    public void getDateMergedWithTimeECPInvalidHour() {
        // arrange
        String time = "-1:15";
        Date noTimeDate = new GregorianCalendar(2024, Calendar.MARCH, 28).getTime();

        // act
        // assert
        Exception ex = assertThrows(IllegalArgumentException.class, () -> dateService.getDateMergedWithTime(time, noTimeDate));
    }
    @Order(3)
    @Test
    @DisplayName("getDateMergedWithTimeInvalid invalid time format")
    @Tag("Invalid")
    @Tag("ECP")
    public void getDateMergedWithTimeECPInvalidTimeThrows() {
        // arrange
        String time = "";
        Date noTimeDate = new GregorianCalendar(2024, Calendar.JANUARY, 1).getTime();

        // act
        // assert
        Exception ex = assertThrows(IllegalArgumentException.class, () -> dateService.getDateMergedWithTime(time, noTimeDate));
    }

    @Order(4)
    @Test
    @DisplayName("getDateMergedWithTimeValid lower boundary")
    @Tag("Valid")
    @Tag("BVA")
    public void getDateMergedWithTimeBVAValidLower() {
        // arrange
        String time = "0:0";
        Date noTimeDate = new GregorianCalendar(1970, Calendar.JANUARY, 1).getTime();
        Date expectedDate = new GregorianCalendar(1970, Calendar.JANUARY, 1, 0, 0).getTime();

        // act
        Date date = dateService.getDateMergedWithTime(time, noTimeDate);

        // assert
        assertEquals(expectedDate, date);
    }

    @Order(5)
    @Test
    @DisplayName("getDateMergedWithTimeValid upper boundary")
    @Tag("Valid")
    @Tag("BVA")
    public void getDateMergedWithTimeBVAValidUpper() {
        // arrange
        String time = "23:59";
        Date noTimeDate = new GregorianCalendar(2024, Calendar.APRIL, 4).getTime();
        Date expectedDate = new GregorianCalendar(2024, Calendar.APRIL, 4, 23, 59).getTime();

        // act
        Date date = dateService.getDateMergedWithTime(time, noTimeDate);

        // assert
        assertEquals(expectedDate, date);
    }

    @Order(6)
    @Test
    @DisplayName("getDateMergedWithTimeInvalid invalid lower boundary")
    @Tag("Invalid")
    @Tag("BVA")
    public void getDateMergedWithTimeBVAInvalidLower() {
        // arrange
        String time = "-1:15";
        Date noTimeDate = new GregorianCalendar(2023, Calendar.APRIL, 4).getTime();

        // act
        // assert
        Exception ex = assertThrows(IllegalArgumentException.class, () -> dateService.getDateMergedWithTime(time, noTimeDate));
    }
    @Order(7)
    @Test
    @DisplayName("getDateMergedWithTimeInvalid invalid upper boundary")
    @Tag("Invalid")
    @Tag("BVA")
    @RepeatedTest(2)
    public void getDateMergedWithTimeBVAInvalidUpper() {
        // arrange
        String time = "24:60";
        Date noTimeDate = new GregorianCalendar(1970, Calendar.JANUARY, 2).getTime();

        // act
        // assert
        Exception ex = assertThrows(IllegalArgumentException.class, () -> dateService.getDateMergedWithTime(time, noTimeDate));
    }
}

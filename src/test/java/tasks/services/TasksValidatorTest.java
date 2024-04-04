package tasks.services;

import org.junit.jupiter.api.*;

import java.text.SimpleDateFormat;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("TasksValidatorTests")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class TasksValidatorTest {

    private static int interval;
    private static Date startDate, endDate;
    private static String title;
    private static TaskValidator taskValidator;

    private static Date getDateFromString(String dateString) throws Exception {
        return new SimpleDateFormat("dd-MM-yyyy HH:mm:ss").parse(dateString);
    }

    @BeforeAll
    static void initTaskValidator(){
        taskValidator = new TaskValidator();
    }

    @BeforeEach
    void initDefaultParameters(){
        interval = 10;
        title = "sample_task";
        try {
            startDate = getDateFromString("07-07-2020 08:00:00");
            endDate = getDateFromString("07-07-2020 10:00:00");
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    // ECP tests
    @Test
    @Order(1)
    @Tag("ECP")
    @Tag("Title")
    @Tag("Exception")
    void testEmptyTaskTitle() {
        title = "";
        IllegalArgumentException thrown = assertThrows(
                IllegalArgumentException.class,
                () -> taskValidator.validate(title, startDate, endDate, interval),
                "Expected validate() to throw IllegalArgumentException, but it didn't"
        );

        assertTrue(thrown.getMessage().contains("Title must not be empty"));
    }

    @Test
    @Order(2)
    @Tag("ECP")
    @Tag("Title")
    void testValidTaskTitle(){
        taskValidator.validate(title, startDate, endDate, interval);
    }

    @Test
    @Order(3)
    @Tag("ECP")
    @Tag("Date")
    void testValidDates(){
        taskValidator.validate(title, startDate, endDate, interval);
    }

    @Test
    @Order(4)
    @Tag("ECP")
    @Tag("Date")
    @Tag("Exception")
    void testInvalidDates(){
        try {
            startDate = getDateFromString("07-07-2020 10:00:00");
            endDate = getDateFromString("07-07-2020 08:00:00");
            IllegalArgumentException thrown = assertThrows(
                    IllegalArgumentException.class,
                    () -> taskValidator.validate(title, startDate, endDate, interval),
                    "Expected validate() to throw IllegalArgumentException, but it didn't"
            );

            assertTrue(thrown.getMessage().contains("Start date should be before end"));
        } catch (Exception e) {
            fail();
        }
    }

    // BVA tests
    @Test
    @Order(5)
    @Tag("BVA")
    @Tag("Interval")
    @Tag("Exception")
    void testLeftMinIntervalValue(){
        interval = 0;
        IllegalArgumentException thrown = assertThrows(
                IllegalArgumentException.class,
                () -> taskValidator.validate(title, startDate, endDate, interval),
                "Expected validate() to throw IllegalArgumentException, but it didn't"
        );

        assertTrue(thrown.getMessage().contains("Interval can not be lower than 1"));
    }

    @Test
    @Order(6)
    @Tag("BVA")
    @Tag("Interval")
    void testMinIntervalValue(){
        interval = 1;
        taskValidator.validate(title, startDate, endDate, interval);
    }

    @Test
    @Order(7)
    @Tag("BVA")
    @Tag("Interval")
    void testRightMinIntervalValue(){
        interval = 2;
        taskValidator.validate(title, startDate, endDate, interval);
    }

    @Test
    @Order(8)
    @Tag("BVA")
    @Tag("Interval")
    @Tag("Exception")
    @Disabled
    void testInvalidLeftIntervalValue(){
        interval = -1;
        IllegalArgumentException thrown = assertThrows(
                IllegalArgumentException.class,
                () -> taskValidator.validate(title, startDate, endDate, interval),
                "Expected validate() to throw IllegalArgumentException, but it didn't"
        );

        assertTrue(thrown.getMessage().contains("Interval can not be lower than 1"));
    }
}
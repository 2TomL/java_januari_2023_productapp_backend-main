package be.intecbrussel.productapp;

import be.intecbrussel.productapp.logger.FileLogger;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.util.AssertionErrors.assertNotNull;

@SpringBootTest
public class ProductappApplicationTests {

    @Test
    void contextLoads() {
        assertNotNull("Application context should not be null", ProductappApplication.class);
    }


    class FileLoggerTest {

        @InjectMocks
        private FileLogger fileLogger;

        @Mock
        private Path path;

        @Mock
        private Files files;

        @Mock
        private BufferedWriter bufferedWriter;

        @BeforeEach
        void setUp() {
            MockitoAnnotations.openMocks(this);
        }

        @Test
        void testLog() throws IOException {
            String log = "Test log";
            String expectedLogLine = "[2023-10-16 14:30] : " + log;
            
            when(path.getParent()).thenReturn(path);
            when(path.toFile()).thenReturn(new File("src/test/resources/logs/testLog.txt"));
            when(files.exists(path)).thenReturn(false);
            when(files.createDirectories(path)).thenReturn(path);
            when(files.createFile(path)).thenReturn(path);
            when(new FileWriter(any(File.class), any(Boolean.class))).thenReturn(new FileWriter("src/test/resources/logs/testLog.txt"));
            when(new BufferedWriter(any(FileWriter.class))).thenReturn(bufferedWriter);
            
            String result = fileLogger.log(log);
            
            assertEquals(expectedLogLine, result);
        }

        @Test
        void testLogException() throws IOException {
            String expectedExceptionLogLine = "[2023-10-16 14:30] : EXCEPTION: Test Exception\n" +
                    "[2023-10-16 14:30] : someMethod(SomeClass.java:10)\n" +
                    "[2023-10-16 14:30] : someOtherMethod(SomeOtherClass.java:20)\n";

            Exception testException = new Exception("Test Exception");
            StackTraceElement[] stackTrace = new StackTraceElement[]{
                    new StackTraceElement("SomeClass", "someMethod", "SomeClass.java", 10),
                    new StackTraceElement("SomeOtherClass", "someOtherMethod", "SomeOtherClass.java", 20)
            };
            testException.setStackTrace(stackTrace);

            when(path.getParent()).thenReturn(path);
            when(path.toFile()).thenReturn(new File("src/test/resources/logs/testExceptionLog.txt"));
            when(files.exists(path)).thenReturn(false);
            when(files.createDirectories(path)).thenReturn(path);
            when(files.createFile(path)).thenReturn(path);
            when(new FileWriter(any(File.class), any(Boolean.class))).thenReturn(new FileWriter("src/test/resources/logs/testExceptionLog.txt"));
            when(new BufferedWriter(any(FileWriter.class))).thenReturn(bufferedWriter);

            String result = fileLogger.logException(testException);
            
            assertEquals(expectedExceptionLogLine, result);
        }

    }
}

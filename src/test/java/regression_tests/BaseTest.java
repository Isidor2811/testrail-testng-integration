package regression_tests;

import com.codepine.api.testrail.TestRail;
import com.codepine.api.testrail.model.Result;
import com.codepine.api.testrail.model.ResultField;
import com.codepine.api.testrail.model.Run;
import framework.TestRailId;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterSuite;

import java.lang.reflect.Method;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class BaseTest {

    private static final String TEST_RAIL_LINK = "https://***.testrail.io/";
    private static final String USER_NAME = "***.com";
    private static final String USER_PASSWORD = "****";
    private static final String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";

    private static TestRail testRail;
    private static Run run;

    static {
        //создание инстанса TestRail
        testRail = TestRail.builder(TEST_RAIL_LINK, USER_NAME, USER_PASSWORD)
                .build();

        // создания рана
        Run newRun = new Run();
        newRun.setName("Daily regression: " + LocalDateTime.now().format(DateTimeFormatter.ofPattern(DATE_FORMAT)));
        run = testRail.runs()
                .add(1, newRun)
                .execute();
    }

    @AfterMethod
    public void afterMethod(ITestResult result, Method method) {
        setTestStatusForTestRailCases(result, method);
    }

    @AfterSuite
    public void afterSuite() {
        closeTestRailRun(testRail);
    }

    private void closeTestRailRun(TestRail rail) {
        rail.runs().close(run.getId()).execute();
    }

    private void setTestStatusForTestRailCases(ITestResult result, Method method) {
        if (method.isAnnotationPresent(TestRailId.class)) {
            TestRailId annotation = method.getAnnotation(TestRailId.class);
            List<ResultField> customResultFields = testRail.resultFields().list().execute();

            int updatedStatus;
            switch (result.getStatus()) {
                case 1 -> updatedStatus = 1; //1 - PASSED in Testng and PASSED in TestRail
                case 2 -> updatedStatus = 5; //2 - FAILED in Testng and 5 - FAILED in TesRail
                default -> throw new IllegalStateException("Unexpected value: " + result.getStatus());
            }
            testRail.results().addForCase(run.getId(), Integer.parseInt(annotation.id()),
                    new Result().setStatusId(updatedStatus), customResultFields).execute();
        }
    }

}

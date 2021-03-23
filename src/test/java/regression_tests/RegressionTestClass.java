package regression_tests;

import framework.TestRailId;
import org.testng.annotations.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;

public class RegressionTestClass extends BaseTest {

    @TestRailId(id = "1")
    @Test
    public void additionTest() {
        assertThat(4 + 8, is(equalTo(12)));
    }

    @TestRailId(id = "2")
    @Test
    public void subtractionTest() {
        assertThat(10 - 14, is(equalTo(-5)));
    }
}

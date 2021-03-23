package regression_tests;

import framework.TestRailId;
import org.testng.annotations.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;

public class RegressionTestClassPartTwo extends BaseTest {

    @TestRailId(id = "9")
    @Test
    public void multiplicationTest() {
        assertThat(4 * 8, is(equalTo(32)));
    }

    @TestRailId(id = "10")
    @Test
    public void divisionTest() {
        assertThat(10 / 2, is(equalTo(5)));
    }
}

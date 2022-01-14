package runners;
import httpURL.TestCreateBooking;
import httpURL.TestGetBookingCall;
import org.junit.experimental.categories.Categories;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import restAssured.TestBookingGetCalls;
import restAssured.TestBookingcreate;
import testcases.TestCreateCall;
import testcases.TestGetCalls;

@RunWith(Categories.class)
@Categories.IncludeCategory(Functional.class)
@Suite.SuiteClasses({TestCreateBooking.class, TestGetBookingCall.class,
                     TestBookingcreate.class,TestBookingGetCalls.class,
                     TestCreateCall.class,TestGetCalls.class})

public class JunitRunner {
}

/**
 * This is the test class that runs all our test cases
 * 
 * @author jamesarama
 * @author gabriela
 */
package charlie.advisor;

import charlie.advisor.section1.*;
import charlie.advisor.section2.*;
import charlie.advisor.section3.*;
import charlie.advisor.section4.*;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({
    // Section 1
    TestAdvice00_12_2.class,
    TestAdvice00_12_7.class,
    TestAdvice01_12_2.class,
    TestAdvice01_12_7.class,
    
    // Section 2
    TestAdvice00_5_2.class,
    TestAdvice00_5_7.class,
    TestAdvice01_5_2.class,
    TestAdvice01_5_7.class,
    
    // Section 3
    TestAdvice00_A2_6.class,
    TestAdvice00_A2_7.class,
    TestAdvice01_A2_6.class,
    TestAdvice01_A2_7.class,
    
    // Section 4
    TestAdvice00_22_2.class,
    TestAdvice00_22_7.class,
    TestAdvice01_22_2.class,
    TestAdvice01_22_7.class
})
/**
 *
 * @author jamesarama
 */
public class AdvisorTest {

    public AdvisorTest() {
    }

    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    /**
     * Test of advise method, of class Advisor.
     */
    @Test
    public void testAdvise() {
    }

}

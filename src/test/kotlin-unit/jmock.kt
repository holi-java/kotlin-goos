import org.jmock.Expectations
import org.jmock.integration.junit4.JUnit4Mockery

inline fun JUnit4Mockery.checking(block: Expectations.() -> Unit) = checking(Expectations().apply(block))
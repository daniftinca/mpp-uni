import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class SomeClass {

    final static Logger logger = LogManager.getLogger(SomeClass.class);

    public static boolean isPar(int n) {
        logger.info("a test info message");
        logger.error("a test error message");
        return n % 2 == 0;

    }
}

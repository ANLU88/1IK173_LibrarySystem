
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;


public class LogTest {

        private static Logger logger = LogManager.getLogger(LogTest.class.getName());

        public static void main(String[] args) throws InterruptedException {


            logger.info("Funkar den här skiten nu?");
            logger.debug("Test");


            while (true) {
                Thread.sleep(1000);
                //logger.trace("ttttttttt");
                logger.debug("ddddddddd");
                logger.error("eeeeeeeee");
                //logger.info( "iiiiiiiii");
                //logger.warn( "wwwwwwwww");
                //logger.fatal("fffffffff");
            }

        }


}

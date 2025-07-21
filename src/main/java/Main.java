import Controller.SocialMediaController;
import io.javalin.Javalin;

/**
 * This class is provided with a main method to allow you to manually run and test your application. This class will not
 * affect your program in any way and you may write whatever code you like here.
 */

public class Main {
    
    public static void main(String[] args) {
        
        String DEBUG_MODE = "DELETE_DB_ON_LAUNCH";

        if ("DELETE_DB_ON_LAUNCH".equals(DEBUG_MODE)){
            Util.ConnectionUtil.resetTestDatabase();
        }
        SocialMediaController controller = new SocialMediaController();
        Javalin app = controller.startAPI();
        app.start(8080);
    }
}

package utilities;

public class IdHelper {
    private static IdHelper instance = null;
    private static int current;

    private IdHelper() {
        current = 0;
    }

    public static IdHelper getInstance(){
        if (instance == null){
            instance = new IdHelper();
        }
        return instance;
    }

    public int getNewId(){
        return current++;
    }
}

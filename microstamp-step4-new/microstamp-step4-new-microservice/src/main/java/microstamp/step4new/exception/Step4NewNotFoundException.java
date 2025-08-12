package microstamp.step4new.exception;

public class Step4NewNotFoundException extends RuntimeException {

    public Step4NewNotFoundException(String resource, String key){
        super(resource + " not found with key " + key);
    }
}

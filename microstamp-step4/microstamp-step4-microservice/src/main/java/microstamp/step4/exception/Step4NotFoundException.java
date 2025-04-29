package microstamp.step4.exception;

public class Step4NotFoundException extends RuntimeException {

    public Step4NotFoundException(String resource, String key){
        super(resource + " not found with key " + key);
    }

}

package microstamp.step2.exception;

public class Step2NotFoundException extends RuntimeException {

    public Step2NotFoundException(String resource, String key){
        super(resource + " not found with key " + key);
    }

}

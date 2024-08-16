package microstamp.step1.exception;

public class Step1NotFoundException extends RuntimeException {

    public Step1NotFoundException(String resource, String key){
        super(resource + " not found with key " + key);
    }

}

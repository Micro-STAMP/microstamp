package step3.infra.exceptions.response.feignclient;

public record FeignError(String type, String key, String message) {

}

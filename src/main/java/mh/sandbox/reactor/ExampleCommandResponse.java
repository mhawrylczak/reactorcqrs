package mh.sandbox.reactor;


public class ExampleCommandResponse implements CommandResponse {
    private final String response;

    public ExampleCommandResponse(String response) {
        this.response = response;
    }

    public String getResponse() {
        return response;
    }
}

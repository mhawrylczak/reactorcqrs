package mh.sandbox.reactor;


public class ExampleCommand implements Command{
    private final String msg;

    public ExampleCommand(String msg) {
        this.msg = msg;
    }

    public String getMsg() {
        return msg;
    }
}

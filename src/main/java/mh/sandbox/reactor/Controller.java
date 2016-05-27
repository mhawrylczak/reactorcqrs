package mh.sandbox.reactor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.async.DeferredResult;
import reactor.bus.Event;
import reactor.bus.EventBus;
import reactor.spring.context.annotation.ReplyTo;
import reactor.spring.context.annotation.Selector;
import reactor.spring.context.annotation.SelectorType;

import javax.annotation.PostConstruct;

import java.util.function.Consumer;

import static reactor.bus.selector.Selectors.$;
import static reactor.spring.context.annotation.SelectorType.TYPE;

@RestController
@reactor.spring.context.annotation.Consumer
public class Controller extends AbstractCommandHandler<ExampleCommand, ExampleCommandResponse> {


    @Autowired
    protected Controller(@Qualifier("commandBus") EventBus bus) {
        super(bus);
    }

    @RequestMapping(path = "/test")
    public DeferredResult<ExampleCommandResponse> test(){

        DeferredResult<ExampleCommandResponse> result = new DeferredResult<>();

        System.out.printf("new request on thread %s%n", Thread.currentThread());

        Consumer<ExampleCommandResponse> eventConsumer = data -> {
            System.out.printf("got command's response on thread %s%n", Thread.currentThread());
            result.setResult(data);
        };
        sendCommand(new ExampleCommand("OK"), eventConsumer);
        return result;
    }

    @Override
    public ExampleCommandResponse handleCommand(ExampleCommand command) throws Exception {
        Thread.sleep(100);
        System.out.printf("receive command on thread %s%n", Thread.currentThread());
        return new ExampleCommandResponse("is " + command.getMsg() + "!");
    }


    // sync part
    @RequestMapping(path = "/test2")
    public ExampleCommandResponse test2() throws InterruptedException {
        return testCommand(new ExampleCommand("OK"));
    }

    public ExampleCommandResponse testCommand(ExampleCommand command) throws InterruptedException {
        Thread.sleep(100);
        System.out.printf("receive command on thread %s%n", Thread.currentThread());
        return new ExampleCommandResponse("is " + command.getMsg() + "!");
    }

}

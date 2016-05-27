package mh.sandbox.reactor;

import reactor.bus.Event;
import reactor.bus.EventBus;
import reactor.spring.context.annotation.ReplyTo;
import reactor.spring.context.annotation.Selector;

import java.util.function.Consumer;

public abstract class AbstractCommandHandler<C extends Command, R extends CommandResponse> {

    protected final EventBus bus;

    protected AbstractCommandHandler(EventBus bus) {
        this.bus = bus;
    }

    @Selector(value = "T(org.springframework.core.ResolvableType).forInstance(#root).superType.getGeneric().type", eventBus = "bus")
    @ReplyTo()
    public R handlerMethod(Event<C> event){
        try{
            return handleCommand(event.getData());
        } catch (Exception ex){
            event.consumeError(ex);
            return null;
        }
    }

    public abstract R handleCommand(C command) throws Exception;


    public void sendCommand(C command, Consumer<R> consumer){
        final reactor.fn.Consumer<Event<R>> c = rEvent -> consumer.accept(rEvent.getData());
        sendCommandInner(command, c);
    }

    private void sendCommandInner(C command, reactor.fn.Consumer<Event<R>> consumer){
        bus.sendAndReceive(command.getClass(), Event.wrap(command), consumer);
    }
}

package mh.sandbox.reactor;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import reactor.Environment;
import reactor.bus.EventBus;
import reactor.core.config.DispatcherType;
import reactor.spring.context.config.EnableReactor;

import static reactor.Environment.THREAD_POOL;

@Configuration
@EnableReactor
public class Config {


    @Bean
    public EventBus commandBus(Environment en){
        return EventBus.create(en.newDispatcher("responseDispatcher", 2048, 100, DispatcherType.WORK_QUEUE)); }

}

package com.phlammariont.tangela;

import com.google.gson.Gson;
import com.phlammariont.tangela.service.MainService;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageListener;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.util.ErrorHandler;

/**
 * Worker for receiving and processing operations asynchronously.
 */

public class Worker {
    public static void main(String[] args) {
        final MainService mainService = new MainService();

        final ApplicationContext rabbitConfig = new AnnotationConfigApplicationContext(RabbitConfiguration.class);
        final ConnectionFactory rabbitConnectionFactory = rabbitConfig.getBean(ConnectionFactory.class);
        final Queue rabbitQueue = rabbitConfig.getBean(Queue.class);
        final MessageConverter jsonMessageConverter = rabbitConfig.getBean(MessageConverter.class);

        // create a listener container, which is required for asynchronous message consumption.
        // AmqpTemplate cannot be used in this case
        final SimpleMessageListenerContainer listenerContainer = new SimpleMessageListenerContainer();
        listenerContainer.setConnectionFactory(rabbitConnectionFactory);
        listenerContainer.setQueueNames(rabbitQueue.getName());
        listenerContainer.setMessageConverter(jsonMessageConverter);

        // set the callback for message handling
        listenerContainer.setMessageListener(new MessageListener() {
            public void onMessage(Message message) {
                try {
                    System.out.println(new String(message.getBody()));
                    String JSON = new String(message.getBody());
                    BigOperation data = new Gson().fromJson(JSON, BigOperation.class);
                    System.out.println(data.getName());
                    mainService.startPlanner(data);
                } catch (Exception e) {

                    System.out.println(e);

                }
            }
        });

        // set a simple error handler
        listenerContainer.setErrorHandler(new ErrorHandler() {
            public void handleError(Throwable t) {
                t.printStackTrace();
            }
        });

        // register a shutdown hook with the JVM
        Runtime.getRuntime().addShutdownHook(new Thread() {
            @Override
            public void run() {
                System.out.println("Shutting down BigOperationWorker");
                listenerContainer.shutdown();
            }
        });

        // start up the listener. this will block until JVM is killed.
        listenerContainer.start();
        System.out.println("BigOperationWorker started");
    }
}

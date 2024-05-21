package com.example.HelloWorld;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.StatusRuntimeException;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

public class HelloWorldClient {

  private static final Logger logger = Logger.getLogger(HelloWorldClient.class.getName());

  private final GreeterGrpc.GreeterBlockingStub blockingStub;

  public HelloWorldClient(ManagedChannel channel) {
    blockingStub = GreeterGrpc.newBlockingStub(channel);
  }

  public void greet(String name, String prenom, String cin) {
    HelloRequest request = HelloRequest.newBuilder()
        .setName(name)
        .setPrenom(prenom)
        .setCin(cin)
        .build();
    HelloReply response;
    try {
      response = blockingStub.sayHello(request);
      System.out.println("Greeting: " + response.getMessage());
      response = blockingStub.sayHelloAgain(request);
      System.out.println("Greeting: " + response.getMessage());
    } catch (StatusRuntimeException e) {
      logger.warning("RPC failed: " + e.getStatus());
    }
  }

  public static void main(String[] args) throws InterruptedException {
    ManagedChannel channel = ManagedChannelBuilder.forAddress("localhost", 50051)
        .usePlaintext()
        .build();
    try {
      HelloWorldClient client = new HelloWorldClient(channel);
      client.greet("YourName", "YourPrenom", "YourCIN");
    } finally {
      channel.shutdownNow().awaitTermination(5, TimeUnit.SECONDS);
    }
  }
}

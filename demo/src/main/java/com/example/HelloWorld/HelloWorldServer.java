package com.example.HelloWorld;

import io.grpc.Server;
import io.grpc.ServerBuilder;
import io.grpc.stub.StreamObserver;
import java.io.IOException;

public class HelloWorldServer {

  private Server server;

  private void start() throws IOException {
    int port = 50051;
    server = ServerBuilder.forPort(port)
        .addService(new GreeterImpl())
        .build()
        .start();
    System.out.println("Server started, listening on " + port);
  }

  private void stop() throws InterruptedException {
    if (server != null) {
      server.shutdown().awaitTermination();
    }
  }

  public static void main(String[] args) throws IOException, InterruptedException {
    final HelloWorldServer server = new HelloWorldServer();
    server.start();
    server.blockUntilShutdown();
  }

  static class GreeterImpl extends GreeterGrpc.GreeterImplBase {

    @Override
    public void sayHello(HelloRequest req, StreamObserver<HelloReply> responseObserver) {
      String greeting = "Hello " + req.getName() + " " + req.getPrenom() + " with CIN " + req.getCin();
      HelloReply reply = HelloReply.newBuilder().setMessage(greeting).build();
      responseObserver.onNext(reply);
      responseObserver.onCompleted();
    }

    @Override
    public void sayHelloAgain(HelloRequest req, StreamObserver<HelloReply> responseObserver) {
      String greeting = "Hello again, " + req.getName() + " " + req.getPrenom() + "!";
      HelloReply reply = HelloReply.newBuilder().setMessage(greeting).build();
      responseObserver.onNext(reply);
      responseObserver.onCompleted();
    }
  }
}

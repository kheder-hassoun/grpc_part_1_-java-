package calculator;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import java.util.Scanner;

import io.grpc.StatusRuntimeException;
import proto.generated.*;

public class Main {

    public static void main(String[] args) {
        // Create channels to connect to the second and third services
        ManagedChannel addChannel = ManagedChannelBuilder.forAddress("localhost", 50051).usePlaintext().build();
        ManagedChannel multiplyChannel = ManagedChannelBuilder.forAddress("localhost", 50052).usePlaintext().build();

        // Create blocking stubs for both channels
        CalculatorServiceGrpc.CalculatorServiceBlockingStub addServiceStub = CalculatorServiceGrpc.newBlockingStub(addChannel);
        CalculatorServiceGrpc.CalculatorServiceBlockingStub multiplyServiceStub = CalculatorServiceGrpc.newBlockingStub(multiplyChannel);

        // Input from the user
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter the first number: ");
        int num1 = scanner.nextInt();
        System.out.println("Enter the second number: ");
        int num2 = scanner.nextInt();

        // Prepare the request object
        TwoNumbers request = TwoNumbers.newBuilder().setNum1(num1).setNum2(num2).build();

        try {
            // Call the Add service
            CalculationResult addResponse = addServiceStub.add(request);
            System.out.println("Addition result: " + addResponse.getResult());

            // Call the Multiply service
            CalculationResult multiplyResponse = multiplyServiceStub.multiply(request);
            System.out.println("Multiplication result: " + multiplyResponse.getResult());
        } catch (StatusRuntimeException e) {
            System.err.println("RPC failed: " + e.getStatus());
        } finally {
            // Shutdown channels
            addChannel.shutdown();
            multiplyChannel.shutdown();
        }
    }






}
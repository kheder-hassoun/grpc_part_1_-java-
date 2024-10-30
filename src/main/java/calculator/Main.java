package calculator;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.StatusRuntimeException;
import proto.generated.CalculatorServiceGrpc;
import proto.generated.CalculationResult;
import proto.generated.TwoNumbers;

import java.util.Scanner;

public class Main {

    // Console colors for styling output
    private static final String ANSI_RESET = "\u001B[0m";
    private static final String ANSI_YELLOW = "\u001B[33m";
    private static final String ANSI_GREEN = "\u001B[32m";
    private static final String ANSI_RED = "\u001B[31m";
    private static final String ANSI_CYAN = "\u001B[36m";

    public static void main(String[] args) {
        // Create channels to connect to the second and third services
        ManagedChannel addChannel = ManagedChannelBuilder.forAddress("localhost", 50051).usePlaintext().build();
        ManagedChannel multiplyChannel = ManagedChannelBuilder.forAddress("localhost", 5000).usePlaintext().build();

        // Create blocking stubs for both channels
        CalculatorServiceGrpc.CalculatorServiceBlockingStub addServiceStub = CalculatorServiceGrpc.newBlockingStub(addChannel);
        CalculatorServiceGrpc.CalculatorServiceBlockingStub multiplyServiceStub = CalculatorServiceGrpc.newBlockingStub(multiplyChannel);

        Scanner scanner = new Scanner(System.in);
        int num1, num2;
        boolean keepRunning = true;

        System.out.println(ANSI_CYAN + "\tWelcome to the Interactive gRPC Calculator Console App!" + ANSI_RESET);
        // Prompt user for numbers
        System.out.println(ANSI_YELLOW + "\n\tEnter the first number:" + ANSI_RESET);
        num1 = scanner.nextInt();
        System.out.println(ANSI_YELLOW + "\tEnter the second number:" + ANSI_RESET);
        num2 = scanner.nextInt();
        // Main loop for user interaction
        while (keepRunning) {


            // Prepare the request object
            TwoNumbers request = TwoNumbers.newBuilder().setNum1(num1).setNum2(num2).build();

            // Display options
            System.out.println(ANSI_CYAN + "\n\tSelect an option:" + ANSI_RESET);
            System.out.println("\t1 - Perform Addition");
            System.out.println("\t2 - Perform Multiplication");
            System.out.println("\t3 - Enter/Change Numbers");
            System.out.println("\t4 - Clear Screen");
            System.out.println("\t5 - Exit");

            int choice = scanner.nextInt();
            switch (choice) {
                case 1:
                    System.out.println(ANSI_GREEN + "\nCalling Add Service on localhost:50051..." + ANSI_RESET);
                    try {
                        CalculationResult addResponse = addServiceStub.add(request);
                        System.out.println(ANSI_YELLOW + "Addition result: " + ANSI_RESET + addResponse.getResult());
                    } catch (StatusRuntimeException e) {
                        System.out.println(ANSI_RED + "Error calling Add service: " + e.getStatus() + ANSI_RESET);
                    }
                    break;

                case 2:
                    System.out.println(ANSI_GREEN + "\nCalling Multiply Service on localhost:5000..." + ANSI_RESET);
                    try {
                        CalculationResult multiplyResponse = multiplyServiceStub.multiply(request);
                        System.out.println(ANSI_YELLOW + "Multiplication result: " + ANSI_RESET + multiplyResponse.getResult());
                    } catch (StatusRuntimeException e) {
                        System.out.println(ANSI_RED + "Error calling Multiply service: " + e.getStatus() + ANSI_RESET);
                    }
                    break;

                case 3:
                    System.out.println(ANSI_CYAN + "\nYou chose to change the numbers." + ANSI_RESET);
                    // Prompt user for numbers
                    System.out.println(ANSI_YELLOW + "\nEnter the first number:" + ANSI_RESET);
                    num1 = scanner.nextInt();
                    System.out.println(ANSI_YELLOW + "Enter the second number:" + ANSI_RESET);
                    num2 = scanner.nextInt();
                    break; // Loop will restart, asking for numbers again
                case 4:
                    System.out.print("\033[H\033[2J");
                    System.out.flush();
                    break;
                case 5:
                    keepRunning = false;
                    System.out.println(ANSI_GREEN + "Exiting the Calculator Console App. Goodbye!" + ANSI_RESET);
                    break;

                default:
                    System.out.println(ANSI_RED + "Invalid option. Please try again." + ANSI_RESET);
            }
        }

        // Shutdown channels
        addChannel.shutdown();
        multiplyChannel.shutdown();
        System.out.println(ANSI_CYAN + "Channels shut down successfully." + ANSI_RESET);
    }
}

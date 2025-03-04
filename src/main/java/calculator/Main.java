package calculator;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.StatusRuntimeException;
import proto.generated.*;

import java.util.Scanner;

public class Main {

    // Console colors for styling output
    private static final String ANSI_RESET = "\u001B[0m";
    private static final String ANSI_YELLOW = "\u001B[33m";
    private static final String ANSI_GREEN = "\u001B[32m";
    private static final String ANSI_RED = "\u001B[31m";
    private static final String ANSI_CYAN = "\u001B[36m";

    public static void main(String[] args) {
        // Create channels to connect to the add and multiply services
        ManagedChannel addChannel = ManagedChannelBuilder.forAddress("localhost", 50051).usePlaintext().build();
        ManagedChannel multiplyChannel = ManagedChannelBuilder.forAddress("localhost", 50052).usePlaintext().build();

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
            System.out.println("\t3 - View History");
            System.out.println("\t4 - Enter/Change Numbers");
            System.out.println("\t5 - Clear Screen");
            System.out.println("\t6 - Exit");

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
                    System.out.println(ANSI_GREEN + "\nCalling Multiply Service on localhost:50052..." + ANSI_RESET);
                    try {
                        CalculationResult multiplyResponse = multiplyServiceStub.multiply(request);
                        System.out.println(ANSI_YELLOW + "Multiplication result: " + ANSI_RESET + multiplyResponse.getResult());
                    } catch (StatusRuntimeException e) {
                        System.out.println(ANSI_RED + "Error calling Multiply service: " + e.getStatus() + ANSI_RESET);
                    }
                    break;

                case 3:
                    System.out.println(ANSI_GREEN + "\nFetching Operation History from Add Service..." + ANSI_RESET);
                    try {
                        HistoryRequest historyRequest = HistoryRequest.newBuilder().build();
                        addServiceStub.getHistory(historyRequest).forEachRemaining(historyEntry -> {
                            System.out.println(ANSI_YELLOW + "Operation: " + historyEntry.getOperation() +
                                    ", Numbers: (" + historyEntry.getNum1() + ", " + historyEntry.getNum2() +
                                    "), Result: " + historyEntry.getResult() + ANSI_RESET);
                        });
                    } catch (StatusRuntimeException e) {
                        System.out.println(ANSI_RED + "Error fetching history: " + e.getStatus() + ANSI_RESET);
                    }
                    break;

                case 4:
                    System.out.println(ANSI_CYAN + "\nYou chose to change the numbers." + ANSI_RESET);
                    // Prompt user for numbers
                    System.out.println(ANSI_YELLOW + "\nEnter the first number:" + ANSI_RESET);
                    num1 = scanner.nextInt();
                    System.out.println(ANSI_YELLOW + "Enter the second number:" + ANSI_RESET);
                    num2 = scanner.nextInt();
                    break;

                case 5:
                    System.out.print("\033[H\033[2J");
                    System.out.flush();
                    break;

                case 6:
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

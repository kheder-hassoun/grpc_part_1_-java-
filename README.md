# CalculatorService - Java gRPC Service

This project provides a gRPC-based CalculatorService in Java that offers two operations: addition and multiplication. The service receives two numbers and returns the result of the specified operation.

## Project Structure

```
CalculatorService/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   ├── calculator/          # Java implementation for gRPC CalculatorService
│   │   │   ├── generated/           # Generated classes from .proto file (auto-generated)
│   │   │   └── CalculatorServiceApp.java # Main entry point for the server
│   │   └── resources/
│   └── test/                         # Optional test files
├── proto/                            # .proto files for gRPC
│   └── calculator.proto
├── build.gradle                      # Gradle build file
└── README.md                         # This file
```

## Prerequisites

- **Java 17** or higher
- **gRPC Java library** (Managed through Gradle dependencies)
- **Gradle** (for building the project)
- **Protocol Buffers Compiler (protoc)** with the gRPC Java plugin


### Install the gRPC Java Plugin for protoc

If you are using `protoc` locally, add the `protoc-gen-grpc-java` plugin:
```bash
# Example command to install the plugin
$ export PATH="$PATH:/path/to/protoc-gen-grpc-java"
```

## Generating Java Classes from .proto File

After setting up `protoc`, generate the Java files from `calculator.proto`:

```bash
# Run the following command from the project maven:
mvn clean install 
```

## Running the Calculator Service

1. **Run the Server**:
 - you can start the server by running the `CalculatorServiceApp` main class in your IDE.

2. **Interacting with the Server**:
   - The server will listen on port `50051` by default. To interact with it, use a gRPC client configured to call the `CalculatorService` methods.

### Example Methods

- **Add**: Sends two numbers to the server and receives the sum.
- **Multiply**: Sends two numbers to the server and receives the product.


## Project Dependencies

The following dependencies are defined in `build.gradle`:

- `grpc-netty` for network transport
- `grpc-protobuf` for protobuf serialization
- `grpc-stub` for client-side support

## Notes

- The server is designed to handle concurrent requests. You can configure and extend it as needed for production.
- You may also implement additional logging or error handling in `CalculatorServiceImpl.java` for better monitoring and debugging.

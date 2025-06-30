package org.aws.s3.infrastructure.exception;

public class LambdaInvocationException extends RuntimeException{

    public LambdaInvocationException(String message, Throwable cause) {
        super(message, cause);
    }
}

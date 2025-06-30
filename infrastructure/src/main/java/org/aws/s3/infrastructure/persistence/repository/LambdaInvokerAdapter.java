package org.aws.s3.infrastructure.persistence.repository;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.aws.s3.core.domain.port.out.LambdaInvokerPort;
import org.aws.s3.infrastructure.exception.LambdaInvocationException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.core.SdkBytes;
import software.amazon.awssdk.services.lambda.LambdaClient;
import software.amazon.awssdk.services.lambda.model.InvokeRequest;

import java.util.Map;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class LambdaInvokerAdapter implements LambdaInvokerPort {

    private final LambdaClient lambdaClient;
    private final ObjectMapper mapper = new ObjectMapper();

    @Value("${app.aws.lambda.functionName}")
    private String functionName;


    @Override
    public void invokeFileUploded(UUID fileId, String location) {
        try {
            String payload = mapper.writeValueAsString(Map.of(
                    "fileId", fileId.toString(),
                    "location", location
            ));
            InvokeRequest rep = InvokeRequest.builder()
                    .functionName(functionName)
                    .payload(SdkBytes.fromUtf8String(payload))
                    .build();
            lambdaClient.invoke(rep);
        } catch (Exception e) {
            throw new LambdaInvocationException("Error al invocar la función Lambda", e);
        }
    }
}

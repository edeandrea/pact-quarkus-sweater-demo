package org.wookie.recorder;

import io.quarkus.mongodb.panache.reactive.ReactivePanacheMongoEntity;

public class Interaction extends ReactivePanacheMongoEntity {
    String methodName;
    String owningComponent;
    String payload;
    String correlationId;
    long timestamp;
    Type type;

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public String getOwningComponent() {
        return owningComponent;
    }

    public void setOwningComponent(String owningComponent) {
        this.owningComponent = owningComponent;
    }

    public String getPayload() {
        return payload;
    }

    public void setPayload(String payload) {
        this.payload = payload;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public String getCorrelationId() {
        return correlationId;
    }

    public void setCorrelationId(String correlationId) {
        this.correlationId = correlationId;
    }
}

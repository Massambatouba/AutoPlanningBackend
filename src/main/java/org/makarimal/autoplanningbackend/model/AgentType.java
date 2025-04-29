package org.makarimal.autoplanningbackend.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum AgentType {
    ADS,
    SSIAP1,
    SSIAP2,
    SSIAP3,
    CHEF_DE_POSTE,
    CHEF_DE_EQUIPE,
    RONDE,
    ASTRAINTE,
    FORMATION;


    @JsonCreator
    public static AgentType fromString(String value) {
        for (AgentType type : AgentType.values()) {
            if (type.name().equalsIgnoreCase(value)) {
                return type;
            }
        }
        throw new IllegalArgumentException("Invalid AgentType: " + value);
    }

    @JsonValue
    public String toValue() {
        return this.name();
    }
}

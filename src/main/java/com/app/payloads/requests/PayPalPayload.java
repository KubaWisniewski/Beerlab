package com.app.payloads.requests;

import com.app.payloads.domain.Transactions;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PayPalPayload {
    @JsonProperty("transactions")
    Transactions[] transactions;
    @JsonProperty("status")
    String status;
}

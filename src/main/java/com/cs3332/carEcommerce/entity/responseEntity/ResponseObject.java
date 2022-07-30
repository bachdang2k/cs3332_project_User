package com.cs3332.carEcommerce.entity.responseEntity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResponseObject {

    private String status;
    private String massage;
    private Object data;

    @Override
    public String toString() {
        return "ResponseObject{" +
                "status='" + status + '\'' +
                ", massage='" + massage + '\'' +
                ", data=" + data +
                '}';
    }
}

package com.gapsi.suppliers.model;

import lombok.*;

import java.io.Serializable;

@Data
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ResponseCompose implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    private boolean status;
    private String msg;
    private Object objeto;
}

package com.visa.innovation.paymentservice.vdp.model.sale;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.visa.innovation.paymentservice.vdp.model.BaseRetrieveByIdResponseVDP;

@JsonInclude(Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class RetrieveSaleByIdResponseVDP extends BaseRetrieveByIdResponseVDP {

}

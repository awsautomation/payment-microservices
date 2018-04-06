package com.visa.innovation.paymentservice.vdp.model.capture;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.visa.innovation.paymentservice.vdp.model.BaseRetrieveByIdResponseVDP;

@JsonInclude(Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class RetrieveCaptureByIdResponseVDP extends BaseRetrieveByIdResponseVDP {

	
}

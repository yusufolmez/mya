package com.olmez.mya.currency.parser;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import com.olmez.mya.model.enums.CurrencyCode;

import lombok.Getter;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonAutoDetect(fieldVisibility = Visibility.ANY)
@Getter
public abstract class CurrencyDetail {

    @JsonProperty("currency_name")
    protected String name;
    protected String rate;
    @JsonIgnore
    protected CurrencyCode code;

    protected CurrencyDetail(CurrencyCode code) {
        this.code = code;
    }

}

package com.qlbds.dto.acc;

public class VerifyOtpDTO {
    private String otpCode;

    public VerifyOtpDTO() {}

    public VerifyOtpDTO(String otpCode) {
        this.otpCode = otpCode;
    }

    public String getOtpCode() { return otpCode; }
    public void setOtpCode(String otpCode) { this.otpCode = otpCode; }
}
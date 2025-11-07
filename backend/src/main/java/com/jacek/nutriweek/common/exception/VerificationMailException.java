package com.jacek.nutriweek.common.exception;

import org.springframework.mail.MailException;

public class VerificationMailException extends MailException {
    public VerificationMailException(String message) {
        super(message);
    }
}

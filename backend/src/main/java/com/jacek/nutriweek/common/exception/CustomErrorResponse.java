package com.jacek.nutriweek.common.exception;

import java.time.Instant;

public record CustomErrorResponse(int status, String message, Instant timestamp) {
}

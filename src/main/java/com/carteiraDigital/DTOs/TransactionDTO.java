package com.carteiraDigital.DTOs;

import java.math.BigDecimal;

public record TransactionDTO(
        BigDecimal value,
        Long senderId,
        Long receiverId
) { }

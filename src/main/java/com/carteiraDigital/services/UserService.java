package com.carteiraDigital.services;

import com.carteiraDigital.exceptions.NotFoundException;
import com.carteiraDigital.exceptions.UnauthorizedException;
import com.carteiraDigital.models.UserModel;
import com.carteiraDigital.repositories.UserRepository;
import com.carteiraDigital.types.UserType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository repository;

    // Validar transação
    public void validateTransaction(UserModel sender, BigDecimal amount) {
        if (sender.getUserType() == UserType.MERCHANT) {
            throw new UnauthorizedException("Usuário do tipo lojista não está autorizado a fazer transações");
        }

        if(sender.getBalance().compareTo(amount) < 0) {
            throw new UnauthorizedException("Saldo insuficiente");
        }
    }

    // Encontrar usuário
    public UserModel findUserById(Long id) {
        return this.repository.findUserById(id).orElseThrow(() -> new NotFoundException("Usuário não encontrado"));
    }

    // Registrar usuário
    public void registerUser(UserModel user) {
        this.repository.save(user);
    }
}

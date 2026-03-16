package com.carteiraDigital.services;

import com.carteiraDigital.DTOs.UserDTO;
import com.carteiraDigital.models.UserModel;
import com.carteiraDigital.repositories.UserRepository;
import com.carteiraDigital.types.UserType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository repository;

    // Validar transação
    public void validateTransaction(UserModel sender, BigDecimal amount) throws Exception {
        if (sender.getUserType() == UserType.MERCHANT) {
            throw new Exception("Usuário do tipo lojista não está autorizado a fazer transações");
        }

        if(sender.getBalance().compareTo(amount) < 0) {
            throw new Exception("Saldo insuficiente");
        }
    }

    // Encontrar usuário
    public UserModel findUserById(Long id) throws Exception {
        return this.repository.findUserById(id).orElseThrow(() -> new Exception("Usuário não encontrado"));
    }

    // Resgatar todos os usuários
    public List<UserModel> getAllUsers() {
        return this.repository.findAll();
    }

    // Registrar usuário
    public UserModel createUser(UserDTO data) {
        UserModel newUser = new UserModel(data);
        this.saveUser(newUser);
        return newUser;
    }

    // Atualizar usuário
    public void saveUser(UserModel user) {
        this.repository.save(user);
    }
}

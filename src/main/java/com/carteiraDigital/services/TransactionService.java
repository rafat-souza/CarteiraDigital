package com.carteiraDigital.services;

import com.carteiraDigital.DTOs.TransactionDTO;
import com.carteiraDigital.exceptions.UnauthorizedException;
import com.carteiraDigital.models.TransactionModel;
import com.carteiraDigital.models.UserModel;
import com.carteiraDigital.repositories.TransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class TransactionService {

    private final TransactionRepository repository;

    private final UserService userService;

    private final RestTemplate restTemplate;

    private final NotificationService notificationService;

    public TransactionModel createTransaction(TransactionDTO transaction) throws Exception {
        UserModel sender = this.userService.findUserById(transaction.senderId());
        UserModel receiver = this.userService.findUserById(transaction.receiverId());

        userService.validateTransaction(sender, transaction.value());

        boolean isAuthorized = this.authorizeTransaction(sender, transaction.value());
        if(!this.authorizeTransaction(sender, transaction.value())){
            throw new UnauthorizedException("Transação não autorizada");
        }

        TransactionModel newTransaction = new TransactionModel();
        newTransaction.setAmount(transaction.value());
        newTransaction.setSender(sender);
        newTransaction.setReceiver(receiver);
        newTransaction.setTimestamp(LocalDateTime.now());

        sender.setBalance(sender.getBalance().subtract(transaction.value()));
        receiver.setBalance(receiver.getBalance().add(transaction.value()));

        this.repository.save(newTransaction);
        this.userService.saveUser(sender);
        this.userService.saveUser(receiver);

        this.notificationService.sendNotification(sender, "Transação realizada com sucesso");
        this.notificationService.sendNotification(receiver, "Transação recebida com sucesso");

        return newTransaction;
    }

    public boolean authorizeTransaction(UserModel sender, BigDecimal value) {
       ResponseEntity<Map> authorizationResponse = restTemplate
               .getForEntity("https://run.mocky.io/v3/8fafdd68-a090-496f-8c9a-3442cf30dae6", Map.class);

       if(authorizationResponse.getStatusCode() == HttpStatus.OK) {
           String message = (String) authorizationResponse.getBody().get("message");
           return "Autorizado".equalsIgnoreCase(message);
       } else return false;
    }

}

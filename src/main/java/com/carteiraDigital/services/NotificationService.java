package com.carteiraDigital.services;

import com.carteiraDigital.DTOs.NotificationDTO;
import com.carteiraDigital.models.UserModel;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
public class NotificationService {

    private final RestTemplate restTemplate;

    public void sendNotification(UserModel user, String message) throws Exception {
        String email = user.getEmail();

        NotificationDTO notificationRequest = new NotificationDTO(email, message);

        ResponseEntity<String> notificationResponse = restTemplate
                .postForEntity("http://o4d9z.mocklab.io/notify", notificationRequest, String.class);

        if(!(notificationResponse.getStatusCode() == HttpStatus.OK)) {
            System.out.println("erro ao enviar notificação");
            throw new Exception("Serviço de notificação está fora do ar");
        }
    }
}

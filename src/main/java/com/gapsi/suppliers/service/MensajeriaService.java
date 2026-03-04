package com.gapsi.suppliers.service;

import com.sendgrid.Method;
import com.sendgrid.Request;
import com.sendgrid.Response;
import com.sendgrid.SendGrid;
import com.sendgrid.helpers.mail.Mail;
import com.sendgrid.helpers.mail.objects.Content;
import com.sendgrid.helpers.mail.objects.Email;
import org.springframework.stereotype.Service;

import java.io.IOException;


@Service
public class MensajeriaService implements IMensajeriaService {

private final String apiKey = "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";

    public void enviarCorreo(String para, String token) throws IOException {
        Email from = new Email("notificaciones@gapsi.com");
        Email to = new Email(para);
        String contenidoCorreo = "Haz clic en el siguiente enlace para continuar con el cambio de contraseña: https://www.gapsi.com/accounts/password-new?token=".concat(token);
        Content content = new Content("text/plain", contenidoCorreo);
        Mail mail = new Mail(from, "recupera tu contraseña", to, content);

        SendGrid sg = new SendGrid(apiKey);
        Request request = new Request();

        try {
            request.setMethod(Method.POST);
            request.setEndpoint("mail/send");
            request.setBody(mail.build());
            Response response = sg.api(request);

            System.out.println("Status: " + response.getStatusCode());
            System.out.println("Body: " + response.getBody());
            System.out.println("Headers: " + response.getHeaders());
        } catch (IOException ex) {
            throw ex;
        }
    }
}

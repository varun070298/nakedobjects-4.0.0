package org.nakedobjects.example.expenses.services;

import org.nakedobjects.applib.AbstractService;
import org.nakedobjects.applib.ApplicationException;

import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;


public class JavaMailSender extends AbstractService implements EmailSender {

    private static final String SMTP_HOST_NAME = "localhost";
    private static final String SMTP_AUTH_USER = "admin@example.com";
    private static final String SMTP_AUTH_PWD = "";
    private static final boolean authenticate = false;

    // TODO initialise from configuration

    private class SMTPAuthenticator extends javax.mail.Authenticator {
        @Override
        public PasswordAuthentication getPasswordAuthentication() {
            final String username = SMTP_AUTH_USER;
            final String password = SMTP_AUTH_PWD;
            return new PasswordAuthentication(username, password);
        }
    }

    public void sendTextEmail(final String toEmailAddress, final String text) {
        final InternetAddress fromAddress;
        try {
            fromAddress = new InternetAddress(SMTP_AUTH_USER);
        } catch (final AddressException e) {
            throw new ApplicationException("Invalid email address " + SMTP_AUTH_USER, e);
        }

        try {
            final Properties properties = new Properties();
            properties.put("mail.smtp.host", SMTP_HOST_NAME);
            properties.put("mail.smtp.auth", authenticate ? "true" : "false");
            final Authenticator authenticator = authenticate ? new SMTPAuthenticator() : null;
            final Session session = Session.getDefaultInstance(properties, authenticator);
            final Message message = new MimeMessage(session);
            final InternetAddress toAddress = new InternetAddress(toEmailAddress);
            message.setFrom(fromAddress);
            message.setRecipient(Message.RecipientType.TO, toAddress);
            message.setSubject("Expenses notification");
            message.setContent(text, "text/plain");
            Transport.send(message);
        } catch (final AddressException e) {
            throw new ApplicationException("Invalid email address " + toEmailAddress, e);
        } catch (final MessagingException e) {
            throw new ApplicationException("Problem sending email ", e);
        }
    }
}

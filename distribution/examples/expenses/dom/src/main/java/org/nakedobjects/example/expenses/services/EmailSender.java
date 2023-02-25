package org.nakedobjects.example.expenses.services;

public interface EmailSender {

    void sendTextEmail(final String toEmailAddress, final String text);
}

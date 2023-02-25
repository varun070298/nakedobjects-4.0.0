package org.nakedobjects.plugins.dnd.viewer;

import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.Label;
import java.awt.Panel;
import java.awt.TextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import org.apache.log4j.Logger;
import org.nakedobjects.metamodel.commons.lang.StringUtils;


public class LoginDialog extends Frame implements ActionListener, KeyListener {
    private static final long serialVersionUID = 1L;
    private static final Logger LOG = Logger.getLogger(LoginDialog.class);
    private final static int BORDER = 10;
    private TextField user;
    private TextField password;
    private Button cancel;
    private Button login;

    private static String CANCEL_LABEL = "Cancel";
    private static String LOGIN_LABEL = "Login";
    private boolean logIn = true;

    public LoginDialog(String message) {
        super("Naked Objects Login");

        AWTUtilities.addWindowIcon(this, "login-logo.png");

    	setLayout(new BorderLayout());

    	add(createLoginFieldsAndButtonsPanel(), BorderLayout.CENTER);
    	if (message != null) {
    		add(createFeedbackLabel(message), BorderLayout.SOUTH);
    	}

        setResizable(false);

        pack();
        final int width = getSize().width; // getWidth();
        final int height = getSize().height; // getHeight();
        final Dimension screen = getToolkit().getScreenSize();

        int x = (screen.width / 2) - (width / 2);

        if ((screen.width / screen.height) >= 2) {
            x = (screen.width / 4) - (width / 2);
        }

        final int y = (screen.height / 2) - (height / 2);
        setLocation(x, y);
        user.requestFocus();
    }

	private Panel createLoginFieldsAndButtonsPanel() {
		Panel panel = new Panel(new GridLayout(3, 2, 10, 10));

        panel.add(new Label("User name:", Label.LEFT));
        panel.add(user = new TextField());
        user.addKeyListener(this);

        panel.add(new Label("Password:", Label.LEFT));
        panel.add(password = new TextField());
        password.addKeyListener(this);
        password.setEchoChar('*');
        
        panel.add(cancel = new Button(CANCEL_LABEL));
        cancel.addActionListener(this);
        cancel.addKeyListener(this);

        panel.add(login = new Button(LOGIN_LABEL));
        login.addActionListener(this);
        login.addKeyListener(this);
		return panel;
	}

	private Label createFeedbackLabel(String message) {
		return new Label(message!=null?message:"");
	}

    @Override
    public Insets getInsets() {
        final Insets in = super.getInsets();
        in.top += BORDER;
        in.bottom += BORDER;
        in.left += BORDER;
        in.right += BORDER;
        return in;
    }

    public void actionPerformed(final ActionEvent evt) {
        action(evt.getSource());
    }

    public void keyPressed(final KeyEvent e) {
    // ignore
    }

    public void keyReleased(final KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_ENTER) {
            action(e.getComponent());
        }
        if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
            cancel(e.getComponent());
        }
    }

    public void keyTyped(final KeyEvent e) {
    // ignore
    }

    private synchronized void cancel(final Object widget) {
        logIn = false;
        notify();
    }

    private synchronized void action(final Object widget) {
        if (widget == cancel) {
            cancel(widget);
        } else if (widget == login || widget == password) {
            logIn = true;
            notify();
        } else if (widget == user) {
            password.requestFocus();
        }
    }

    @Override
    public void dispose() {
        LOG.debug("dispose...");
        super.dispose();
        LOG.debug("...disposed");

    }

    public String getUser() {
        return StringUtils.removeTabs(user.getText()).trim();
    }

    public String getPassword() {
        return StringUtils.removeTabs(password.getText()).trim();
    }

    public synchronized boolean login() {
        try {
            wait();
        } catch (final InterruptedException e) {}
        return logIn;
    }

}
// Copyright (c) Naked Objects Group Ltd.

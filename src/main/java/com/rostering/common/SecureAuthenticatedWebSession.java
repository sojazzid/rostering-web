package com.rostering.common;

import org.apache.wicket.authroles.authentication.AuthenticatedWebSession;
import org.apache.wicket.authroles.authorization.strategies.role.Roles;
import org.apache.wicket.request.Request;

public class SecureAuthenticatedWebSession extends AuthenticatedWebSession {

    public SecureAuthenticatedWebSession(Request request) {

        super(request);


    }

    @Override
    public boolean authenticate(String userName, String password) {

        return true;
    }

    @Override
    public Roles getRoles() {
        return new Roles(Roles.ADMIN);
    }
}

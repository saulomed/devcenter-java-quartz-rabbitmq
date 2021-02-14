package com.heroku.devcenter;

import javax.ejb.Singleton;
import javax.ejb.Startup;

@Startup
@Singleton
public class Parametros {

    public boolean flagEnviado = false;
}

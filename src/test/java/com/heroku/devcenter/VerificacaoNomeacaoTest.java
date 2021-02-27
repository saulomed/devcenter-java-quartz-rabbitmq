package com.heroku.devcenter;

import org.junit.Test;

import java.util.Calendar;

public class VerificacaoNomeacaoTest
{

    @Test
    public void verificacaoNomeacaoTest()
    {
//        VerificaNomeacao verificaNomeacao = new VerificaNomeacao();
//        verificaNomeacao.run();
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.DAY_OF_MONTH,11);
        cal.set(Calendar.HOUR_OF_DAY,20);
        cal.set(Calendar.MINUTE,30);

        System.out.println(cal.getTime());
    }
}

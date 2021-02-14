package com.heroku.devcenter;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public class VerificaNomeacao implements Runnable
{

    public static final String LORENA_GRACIELY_NEVES_TABLADA = "LORENA+GRACIELY+NEVES+TABLADA";
    public static final String ALAIN = "ALAIN+ESMERALDO+LOPES";
    public static final String BRUNO = "BRUNO+DE+CASTRO+FREITAS";
    private static String textoFalha = "Nenhum resultado foi encontrado para sua pesquisa.";
    public static boolean flagEnviado = false;
    public void run() {
        System.out.println("Verificacao executando");
        Calendar cal = Calendar.getInstance();
//        cal.set(Calendar.DAY_OF_MONTH,11);
        cal.set(Calendar.HOUR_OF_DAY,20);
        cal.set(Calendar.MINUTE,30);

        Date now = new Date();
        System.out.println("Agora: "+now);
        System.out.println("Hora Disparo: "+cal.getTime());
        System.out.println("Data de verificacao: "+getCurrentDay());
        System.out.println("flag: "+flagEnviado);

        if(!flagEnviado && now.after(cal.getTime()))
        {
            System.out.println("Realizar verificação");
            verificaNomeacao(LORENA_GRACIELY_NEVES_TABLADA, "Lorena");
            verificaNomeacao(ALAIN, "Alain");
            verificaNomeacao(BRUNO, "Bruno");
            flagEnviado = true;
            System.out.println("Verificação realizada com sucesso");
        }
        else if(flagEnviado && now.before(cal.getTime()))
        {
            System.out.println("Marcar flag false");
            flagEnviado = false;
        }
        else
        {

            System.out.println("Verificação já realizada ou flag já alterada. Nada a ser realizado");
        }

        System.out.println("Flag: "+flagEnviado);




    }

    private void verificaNomeacao(String nomeBusca, String nomeEmail) {
        URL url = null;
        String codigoPagina = null;
        try {
//            url = new URL("https://doem.org.br/pe/petrolina/pesquisar?keyword=LORENA+GRACIELY+NEVES+TABLADA&data_publicacao=2021-02-07");
            String endereco = "http://doem.org.br/pe/petrolina/pesquisar?keyword=%s&data_publicacao=%s";
//            String endereco = "http://doem.org.br/pe/petrolina/pesquisar?keyword=LORENA+GRACIELY+NEVES+TABLADA&data_publicacao=2021-02-09";
            endereco = String.format(endereco,nomeBusca,getCurrentDay());
            System.out.println(endereco);
            url = new URL(endereco);
            System.setProperty("http.agent", "Chrome");
            HttpURLConnection connection = (HttpURLConnection)url.openConnection();
            connection.setRequestMethod("GET");
            connection.connect();

            int code = connection.getResponseCode();
            System.out.println("Response code of the object is "+code);
            if (code==200)
            {
                System.out.println("OK");
                BufferedReader in = new BufferedReader(
                        new InputStreamReader(connection.getInputStream()));
                String inputLine;
                StringBuffer response = new StringBuffer();

                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();

                //print result
                codigoPagina = response.toString();
//                System.out.println("Pagina Resposta: "+codigoPagina);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


        Mail email = new Mail();
        nomeEmail = nomeEmail + " - "+getCurrentDay();
        if(codigoPagina != null && codigoPagina.contains(textoFalha))
        {
            email.enviaEmail(textoFalha,nomeEmail);
        }
        else if(codigoPagina != null && !codigoPagina.contains(textoFalha))
        {
            email.enviaEmail("Verifiquei o diario, possivel nomeação",nomeEmail);
        }

        System.out.println("fim Verificacao");
    }

    private String getCurrentDay(){
        //Create a Calendar Object
        Calendar calendar = Calendar.getInstance(TimeZone.getDefault());
        String padraoData = "YYYY-MM-dd";


        //Integer to String Conversion
        String todayStr = new SimpleDateFormat(padraoData).format(calendar.getTime());
        System.out.println("Today Str: " + todayStr + "\n");

        return todayStr;
    }
}

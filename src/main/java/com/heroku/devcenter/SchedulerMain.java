package com.heroku.devcenter;


import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.MessageProperties;
import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;

import static org.quartz.JobBuilder.newJob;
import static org.quartz.SimpleScheduleBuilder.*;
import static org.quartz.TriggerBuilder.newTrigger;
import static org.quartz.CronScheduleBuilder.*;

public class SchedulerMain {

    final static Logger logger = LoggerFactory.getLogger(SchedulerMain.class);
    final static ConnectionFactory factory = new ConnectionFactory();
    
    public static void main(String[] args) throws Exception {
//        factory.setUri(System.getenv("CLOUDAMQP_URL"));
        Scheduler scheduler = StdSchedulerFactory.getDefaultScheduler();

        scheduler.start();

        JobDetail jobDetail = newJob(HelloJob.class).build();
        
        Trigger trigger = newTrigger()
                .startNow()
                .withSchedule(repeatMinutelyForever(1))
//                .withSchedule(CronScheduleBuilder.cronSchedule("0 0/1 0 ? * * *"))
//                .withIdentity("trigger1","groupTest")
//                .withSchedule(dailyAtHourAndMinute(23,46))
                .forJob(jobDetail)
                .build();

        scheduler.scheduleJob(jobDetail, trigger);
    }

    public static class HelloJob implements Job {
        
        @Override
        public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
            
//            try {
//                logger.info("teste de execucao!!!");
//                Connection connection = factory.newConnection();
//                Channel channel = connection.createChannel();
//                String queueName = "work-queue-1";
//                Map<String, Object> params = new HashMap<String, Object>();
//                params.put("x-ha-policy", "all");
//                channel.queueDeclare(queueName, true, false, false, params);
//
//                String msg = "Sent at:" + System.currentTimeMillis();
//                byte[] body = msg.getBytes("UTF-8");
//                channel.basicPublish("", queueName, MessageProperties.PERSISTENT_TEXT_PLAIN, body);
//                logger.info("Message Sent: " + msg);
//                connection.close();
//            }
//            catch (Exception e) {
//                logger.error(e.getMessage(), e);
//            }

            VerificaNomeacao verificaNomeacao = new VerificaNomeacao();
            verificaNomeacao.run();

        }
        
    }

}

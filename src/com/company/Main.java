package com.company;

import org.eclipse.paho.client.mqttv3.*;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

import java.util.Random;
import java.util.UUID;

public class Main {



    public static void main(String[] args) throws MqttPersistenceException {
        Random r = new Random();
        while (true) {
            int n = r.nextInt(500);
            System.out.println("Topic?");
            String topic = In.readString();
            String content = "Message de test " + n;
            int qos = 2;
            String brokerUrl = "tcp://localhost:1883";
            String clientId = UUID.randomUUID().toString();
            MemoryPersistence persistence = new MemoryPersistence();

            try {
                //creation d'une instance  MqttClient
                MqttClient publisher = new MqttClient(brokerUrl, clientId, persistence);

                //connexion au broker (Server)
                MqttConnectOptions options = new MqttConnectOptions();
                options.setAutomaticReconnect(true);
                options.setCleanSession(true);
                options.setConnectionTimeout(10);
                System.out.println("Connexion au broker: " + brokerUrl);
                publisher.connect(options);
                System.out.println("Connecte");

                //envoyer un payload (bytes[])
                System.out.println("Publication du message: " + content);
                MqttMessage payload = new MqttMessage(content.getBytes());
                payload.setQos(qos);
                payload.setRetained(true);
                publisher.publish(topic, payload);
                System.out.println("ok");
                publisher.disconnect();
                System.out.println("Deconnecte");

            } catch (MqttException warning) {
                System.out.println("reason " + warning.getReasonCode());
                System.out.println("msg " + warning.getMessage());
                System.out.println("loc " + warning.getLocalizedMessage());
                System.out.println("cause " + warning.getCause());
                System.out.println("excep " + warning);
                warning.printStackTrace();
            }
        }
    }
}

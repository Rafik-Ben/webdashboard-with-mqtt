package de.dcsquare.paho.client.publisher;

import de.dcsquare.paho.client.util.Utils;
import org.eclipse.paho.client.mqttv3.*;

/**
 * @author Dominik Obermaier
 * @author Christian Götz
 */
public class Publisher {

    public static final String BROKER_URL = "tcp://localhost:1883";

    public static final String TOPIC_BRIGHTNESS = "home/brightness";
    public static final String TOPIC_TEMPERATURE = "home/temperature";

    private MqttClient client;


    public Publisher() {

        //We have to generate a unique Client id.
        String clientId = Utils.getMacAddress() + "-pub";


        try {

            client = new MqttClient(BROKER_URL, clientId);

        } catch (MqttException e) {
            e.printStackTrace();
            System.exit(1);
        }
    }

    private void start() {

        try {
            MqttConnectOptions options = new MqttConnectOptions();
            options.setCleanSession(false);
            options.setWill(client.getTopic("home/status"), "offline".getBytes(), 1, true);

            client.connect(options);

            client.publish("home/status","online".getBytes(),1,true);
            //Publish data forever
            while (true) {

                publishBrightness();

                Thread.sleep(500);

                publishTemperature();

                Thread.sleep(500);
            }
        } catch (MqttException e) {
            e.printStackTrace();
            System.exit(1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void publishTemperature() throws MqttException {
        final MqttTopic temperatureTopic = client.getTopic(TOPIC_TEMPERATURE);

        final int temperatureNumber = Utils.createRandomNumberBetween(20, 30);
        final String temperature = Integer.toString(temperatureNumber);

        temperatureTopic.publish(temperature.getBytes(),1,true);

        System.out.println("Published data. Topic: " + temperatureTopic.getName() + "  Message: " + temperature);
    }

    private void publishBrightness() throws MqttException {
        final MqttTopic brightnessTopic = client.getTopic(TOPIC_BRIGHTNESS);

        final int brightnessNumber = Utils.createRandomNumberBetween(0, 100);
        final String brigthness = Integer.toString(brightnessNumber);

        brightnessTopic.publish(brigthness.getBytes(),1,true);

        System.out.println("Published data. Topic: " + brightnessTopic.getName() + "   Message: " + brigthness);
    }

    public static void main(String... args) {
        final Publisher publisher = new Publisher();
        publisher.start();
    }
}

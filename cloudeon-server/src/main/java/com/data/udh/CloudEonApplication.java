package com.data.udh;

import akka.actor.ActorSystem;
import io.fabric8.kubernetes.client.KubernetesClient;
import io.fabric8.kubernetes.client.KubernetesClientBuilder;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class CloudEonApplication {

	public static void main(String[] args) {

		SpringApplication.run(CloudEonApplication.class, args);
	}

    @Bean("udhActorSystem")
    public ActorSystem udhActorSystem() {
        return ActorSystem.create("udhActorSystem");
    }

    @Bean("kubeClient")
    public KubernetesClient kubeClient() {
        KubernetesClient client = new KubernetesClientBuilder().build();
        return client;
    }
}

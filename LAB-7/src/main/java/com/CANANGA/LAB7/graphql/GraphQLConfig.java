package com.CANANGA.LAB7.graphql;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.graphql.execution.RuntimeWiringConfigurer;
import graphql.schema.idl.RuntimeWiring;
import java.util.Map;

@Configuration
public class GraphQLConfig {

    @Bean
    public RuntimeWiringConfigurer runtimeWiringConfigurer() {
        return builder -> {
            builder.type("Query", typeWiring -> typeWiring
                    .dataFetcher("hello", environment -> "Hello from GraphQL Controller!")
            );
        };
    }
}
package sml.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import sml.helperfiles.DefaultInstructionRegistrationLogger;
import sml.helperfiles.InstructionRegistrationLogger;

@Configuration
@ComponentScan(basePackages = "sml")
public class SmlConfig {

    @Bean
    @Primary
    public InstructionRegistrationLogger instructionRegistrationLogger() {
        return new DefaultInstructionRegistrationLogger();
    }
}
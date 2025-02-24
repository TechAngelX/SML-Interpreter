package sml.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import sml.helperfiles.DefaultInstructionRegistrationLogger;
import sml.helperfiles.InstructionRegistrationLogger;

@Configuration
@ComponentScan("sml")
public class SmlConfig {

    @Bean("instructionRegistrationLogger")
    public InstructionRegistrationLogger instructionRegistrationLogger() {
        return new DefaultInstructionRegistrationLogger();
    }
}
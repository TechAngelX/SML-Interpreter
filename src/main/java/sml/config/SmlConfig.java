package sml.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import sml.helperfiles.DefaultInstructionRegistrationLogger;
import sml.helperfiles.InstructionRegistrationLogger;

/**
 * Configuration class for the Simple Machine Language (SML) application.
 * <p>
 * This configuration provides Spring framework setup and bean definitions
 * for the SML application, enabling component scanning and defining
 * essential application beans.
 * </p>
 *
 * <h2>Key Configuration Features</h2>
 * <ul>
 *     <li>Enables component scanning across the entire SML project</li>
 *     <li>Configures the primary {@link InstructionRegistrationLogger} implementation</li>
 * </ul>
 *
 * <p>
 * The {@link #instructionRegistrationLogger()} method provides the primary
 * bean for tracking instruction registrations throughout the application.
 * </p>
 *
 * @author Ricki Angel
 * @version 1.0
 * @since 1.0
 */
@Configuration
@ComponentScan(basePackages = "sml")
public class SmlConfig {

    /**
     * Creates the primary {@link InstructionRegistrationLogger} bean.
     * <p>
     * This method instantiates the default implementation of the
     * instruction registration logger, which will be used as the
     * primary logging mechanism for instruction registrations.
     * </p>
     *
     * @return A {@link DefaultInstructionRegistrationLogger} instance
     */
    @Bean
    @Primary
    public InstructionRegistrationLogger instructionRegistrationLogger() {
        return new DefaultInstructionRegistrationLogger();
    }
}
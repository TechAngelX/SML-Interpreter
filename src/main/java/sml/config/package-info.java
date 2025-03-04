/**
 * Provides Spring configuration for the Simple Machine Language application.
 * <p>
 * This package contains configuration classes that set up the Spring application context:
 * <ul>
 *   <li>{@link sml.discovery.SmlConfig} - Primary configuration with component scanning and bean definitions</li>
 * </ul>
 * </p>
 * <p>
 * The configuration enables:
 * <ul>
 *   <li>Component scanning across the entire SML application</li>
 *   <li>Dependency injection for core components</li>
 *   <li>Service registration with appropriate scopes</li>
 *   <li>Logger configuration with primary bean definitions</li>
 * </ul>
 * </p>
 * <p>
 * This package supports both Spring-managed and manual dependency injection modes,
 * allowing the SML application to run with or without a Spring context.
 * </p>
 *
 * @author Ricki Angel
 * @see sml.helperfiles.InstructionRegistrationLogger
 * @see sml.helperfiles.DefaultInstructionRegistrationLogger
 */
package sml.config;

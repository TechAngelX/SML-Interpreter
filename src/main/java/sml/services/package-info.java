/**
 * Provides service components for file processing and resource management.
 * <p>
 * This package contains service classes that handle file operations for the SML system:
 * <ul>
 *   <li>{@link sml.services.FileService} - Manages file access with UTF-8 encoding support</li>
 * </ul>
 * </p>
 * <p>
 * The services in this package follow the Spring Framework's service pattern,
 * enabling dependency injection and separation of concerns. The file service
 * abstracts file system interactions, providing a clean interface for reading
 * program source files with consistent character encoding handling.
 * </p>
 * <p>
 * These services can be extended to support additional resource operations
 * like network access, database connections, or other external system integrations.
 * </p>
 *
 * @author Ricki Angel
 * @see sml.Translator
 */
package sml.services;

/**
 * Provides discovery mechanisms for dynamically loading SML instructions at runtime.
 *
 * <p>This package contains strategy implementations for discovering and registering 
 * instruction classes in the SML virtual machine. It enables extensibility through:</p>
 * <ul>
 *   <li>{@link sml.discovery.ConfigDiscovery} - Loads instructions from configuration files</li>
 *   <li>{@link sml.discovery.PackageScanDiscovery} - Discovers instructions by scanning package directories</li>
 *   <li>{@link sml.discovery.InstructionDiscoveryStrategy} - Common interface for discovery implementations</li>
 * </ul>
 *
 * <p>These discovery mechanisms allow the SML system to be extended with new instructions
 * without modifying the core codebase. New instructions can be added by:</p>
 * <ol>
 *   <li>Creating a new class that extends {@link sml.instructions.Instruction}</li>
 *   <li>Adding configuration entries or placing the class in the appropriate package</li>
 * </ol>
 *
 * <p>The discovery system supports dependency injection through the Spring Framework
 * and provides detailed logging of the discovery process.</p>
 *
 * @author Ricki Angel
 * @see sml.registry.InstructionRegistry
 * @see sml.instructions.Instruction
 *
 * <p><b>Video Tutorials:</b></p>
 * <table>
 *   <tr>
 *     <th align="center">Config Registration Process</th>
 *     <th align="center">Package Scan Process</th>
 *   </tr>
 *   <tr>
 *     <td align="center">
 *       <a href="https://youtu.be/4jjzWDJ21As" target="_blank">
 *          <img src="doc-files/config_process.jpg" alt="Configuration Registration Process Video" width="380">
 *       </a>
 *       <br>
 *       <em>Click to watch Config Registration Process</em>
 *     </td>
 *     <td align="center">
 *       <a href="https://youtu.be/PHcSX3sMAlU" target="_blank">
 *          <img src="doc-files/package_scan_process.jpg" alt="Configuration Registration Process Video" width="380">
 *       </a>
 *       <br>
 *       <em>Click to watch Package Scan Process</em>
 *     </td>
 *   </tr>
 * </table>
 */


package sml.discovery;

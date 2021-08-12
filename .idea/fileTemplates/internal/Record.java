#if (${PACKAGE_NAME} && ${PACKAGE_NAME} != "")package ${PACKAGE_NAME};#end
#parse("File Header.java")
/**
 * <p><b>Record {@link ${NAME}}</b></p>
 *
 * @author ${USER}
 * @version #parse("Version.txt")
 * @since #parse("Version.txt")
 */
public record ${NAME} {
}

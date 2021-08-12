#if (${PACKAGE_NAME} && ${PACKAGE_NAME} != "")package ${PACKAGE_NAME};#end

import java.lang.Exception;

#parse("File Header.java")
/**
 * <p><b>Exception {@link ${NAME}}</b></p>
 *
 * @author ${USER}
 * @version #parse("Version.txt")
 * @since #parse("Version.txt")
 */
public class ${NAME}Exception extends Exception {
}

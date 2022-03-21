package org.javaboyer.vhr.utils;

import org.javaboyer.vhr.model.Hr;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 * @author zhangfu.huang
 * @date 2022年03月21日 8:46
 */
public class HrInstanceUtil {
    public static Hr getHrInstance() {
        return ((Hr) SecurityContextHolder.getContext().getAuthentication().getPrincipal());
    }
}

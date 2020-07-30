package my.service.util;


import org.apache.commons.lang.time.DateFormatUtils;

import java.util.Date;

public class DateUtil {

    public static String getISO8601DateString(Date date) {
        String pattern = "yyyy-MM-dd'T'HH:mm:ss:SSSZZ";
        return DateFormatUtils.format(new Date(), pattern);
    }
}

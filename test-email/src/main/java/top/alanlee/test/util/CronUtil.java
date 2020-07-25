package top.alanlee.test.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class CronUtil {

    private static String cronDateFormat = "ss mm HH dd MM ?";

    /**
      *  
      * @param date 
      * @param dateFormat : e.g:yyyy-MM-dd HH:mm:ss 
      * @return 
      */
    public static String formatDateByPattern(Date date, String dateFormat) {
        SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
        String formatTimeStr = null;
        if (date != null) {
            formatTimeStr = sdf.format(date);
        }
        return formatTimeStr;
    }

    /*** 
      * convert Date to cron ,eg.  "0 06 10 15 1 ? 2014" 
      * @param date  : 时间点 
      * @return 
      */
    public static String getCron(java.util.Date date) {
        return formatDateByPattern(date, cronDateFormat);
    }

    public static String getStringDateToCron(String strDate) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            Date date = simpleDateFormat.parse(strDate);
            return formatDateByPattern(date, cronDateFormat);
        } catch (ParseException e) {
            e.printStackTrace();
            return "";
        }
    }
}


package com.ksptool.text;

import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class Str {

    /**
     * 私有构造函数，防止实例化工具类
     */
    private Str() {
        throw new UnsupportedOperationException("工具类不允许实例化");
    }

    // 手机号正则表达式(中国大陆11位手机号)
    private static final Pattern PHONE_PATTERN = Pattern.compile("^(13[0-9]|14[01456879]|15[0-35-9]|16[2567]|17[0-8]|18[0-9]|19[0-35-9])\\d{8}$");

    // 身份证号正则表达式(中国大陆18位身份证号)
    private static final Pattern IDCARD_PATTERN = Pattern.compile("^[1-9]\\d{5}(18|19|20)\\d{2}(0[1-9]|1[0-2])(0[1-9]|[12]\\d|3[01])\\d{3}[\\dXx]$");

    // 邮箱正则表达式
    private static final Pattern EMAIL_PATTERN = Pattern.compile(
        "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@" + 
        "(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$"
    );

    private static final String DEFAULT_DATE_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";
    private static final String DEFAULT_DATE_FORMAT = "yyyy-MM-dd";
    private static final String DEFAULT_TIME_FORMAT = "HH:mm:ss";

    
    /**
     * 校验是否为整数
     * @param str 整数字符串
     * @return 是否为整数
     */
    public static boolean isInteger(String str){
        if(isBlank(str)){
            return false;
        }
        //校验是否为整数
        try{
            Integer.parseInt(str);
            return true;
        }catch(NumberFormatException e){
            return false;
        }
    }
    
    /**
     * 校验是否为长整数
     * @param str 长整数字符串
     * @return 是否为长整数
     */
    public static boolean isLong(String str){
        if(isBlank(str)){
            return false;
        }
        //校验是否为整数
        try{
            Long.parseLong(str);
            return true;
        }catch(NumberFormatException e){
            return false;
        }
    }

    /**
     * 校验是否为浮点数
     * @param str 浮点数字符串
     * @return 是否为浮点数
     */
    public static boolean isDouble(String str){
        if(isBlank(str)){
            return false;
        }
        //校验是否为整数
        try{
            Double.parseDouble(str);
            return true;
        }catch(NumberFormatException e){
            return false;
        }
    }

    /**
     * 校验是否为日期
     * @param str 日期字符串
     * @return 是否为日期
     */
    public static boolean isDate(String str){
        return isDate(str,DEFAULT_DATE_FORMAT);
    }

    /**
     * 校验是否为日期时间
     * @param str 日期时间字符串
     * @return 是否为日期时间
     */
    public static boolean isDateTime(String str){
        return isDateTime(str,DEFAULT_DATE_TIME_FORMAT);
    }
    
    /**
     * 校验是否为时间
     * @param str 时间字符串
     * @return 是否为时间
     */
    public static boolean isTime(String str){
        return isTime(str,DEFAULT_TIME_FORMAT);
    }

    /**
     * 校验是否不为日期时间
     * @param str 日期时间字符串
     * @return 不是日期时间 true 是日期时间 false
     */
    public static boolean isNotDateTime(String str){
        return !isDateTime(str);
    }

    /**
     * 校验是否不为日期
     * @param str 日期字符串
     * @return 不是日期 true 是日期 false
     */
    public static boolean isNotDate(String str){
        return !isDate(str);
    }

    /**
     * 校验是否不为时间
     * @param str 时间字符串
     * @return 不是时间 true 是时间 false
     */
    public static boolean isNotTime(String str){
        return !isTime(str);
    }
    /**
     * 校验是否为日期时间
     * @param str 日期时间字符串
     * @return 是否为日期时间
     */
    public static boolean isDateTime(String str,String format){
        if(isBlank(str)){
            return false;
        }
        //校验是否为日期时间 LDT
        try{
            LocalDateTime.parse(str,DateTimeFormatter.ofPattern(format));
            return true;
        }catch(DateTimeException e){
            return false;
        }
    }

    /**
     * 校验是否为日期
     * @param str 日期字符串
     * @return 是否为日期
     */
    public static boolean isDate(String str,String format){
        if(isBlank(str)){
            return false;
        }
        //校验是否为日期
        try{
            LocalDate.parse(str,DateTimeFormatter.ofPattern(format));
            return true;
        }catch(DateTimeException e){
            return false;
        }
    }

    /**
     * 校验是否为时间
     * @param str 时间字符串
     * @return 是否为时间
     */
    public static boolean isTime(String str,String format){
        if(isBlank(str)){
            return false;
        }
        //校验是否为时间 使用LDT
        try{
            LocalTime.parse(str,DateTimeFormatter.ofPattern(format));
            return true;
        }catch(DateTimeException e){
            return false;
        }
    }

    /**
     * 校验是否不为日期时间
     * @param str 日期时间字符串
     * @return 不是日期时间 true 是日期时间 false
     */
    public static boolean isNotDateTime(String str,String format){
        return !isDateTime(str,format);
    }

    /**
     * 校验是否不为日期
     * @param str 日期字符串
     * @return 不是日期 true 是日期 false
     */
    public static boolean isNotDate(String str,String format){
        return !isDate(str,format);
    }

    /**
     * 校验是否不为时间
     * @param str 时间字符串
     * @return 不是时间 true 是时间 false
     */
    public static boolean isNotTime(String str,String format){
        return !isTime(str,format);
    }


    /**
     * 校验是否为指定值之一
     * @param str 字符串
     * @param arg 指定值(字符串)
     * @return 是否为指定值之一
     */
    public static boolean in(String str,String... arg){
        if(isBlank(str)){
            return false;
        }
        for(String s : arg){
            if(str.equals(s)){
                return true;
            }
        }
        return false;
    }

    /**
     * 校验是否为指定值之一
     * @param value 整数
     * @param arg 指定值(整数)
     * @return 是否为指定值之一
     */
    public static boolean in(Integer value,Integer... arg){
        if(value == null){
            return false;
        }
        for(Integer i : arg){
            if(Objects.equals(value, i)){
                return true;
            }
        }
        return false;
    }

    /**
     * 校验是否不在指定值之一
     * @param str 字符串
     * @param arg 指定值
     * @return 是否不在指定值之一
     */
    public static boolean notIn(String str,String... arg){
        return !in(str,arg);
    }

    /**
     * 校验是否不在指定值之一
     * @param value 整数
     * @param arg 指定值
     * @return 是否不在指定值之一
     */
    public static boolean notIn(Integer value,Integer... arg){
        return !in(value,arg);
    }

    /**
     * 校验是否不为整数
     * @param str 整数字符串
     * @return 是否不为整数
     */
    public static boolean isNotInteger(String str){
        return !isInteger(str);
    }

    /**
     * 校验是否不为长整数
     * @param str 长整数字符串
     * @return 是否不为长整数
     */
    public static boolean isNotLong(String str){
        return !isLong(str);
    }

    /**
     * 校验是否不为浮点数
     * @param str 浮点数字符串
     * @return 是否不为浮点数
     */
    public static boolean isNotDouble(String str){
        return !isDouble(str);
    }

    /**
     * 校验手机号是否合法
     */
    public static boolean isPhoneNumberValid(String phoneNumber) {
        if (phoneNumber == null || phoneNumber.isEmpty()) {
            return false;
        }
        Matcher matcher = PHONE_PATTERN.matcher(phoneNumber);
        return matcher.matches();
    }
    /**
     * 校验身份证号是否合法
     */
    public static boolean isIdCardValid(String idCardNumber) {
        if (idCardNumber == null || idCardNumber.isEmpty()) {
            return false;
        }
        Matcher matcher = IDCARD_PATTERN.matcher(idCardNumber);
        return matcher.matches();
    }

    /**
     * 校验邮箱地址是否合法
     */
    public static boolean isEmailValid(String email) {
        if (email == null || email.isEmpty()) {
            return false;
        }
        Matcher matcher = EMAIL_PATTERN.matcher(email);
        return matcher.matches();
    }

    public static boolean isBlank(final CharSequence cs) {
        final int strLen = length(cs);
        if (strLen == 0) {
            return true;
        }
        for (int i = 0; i < strLen; i++) {
            if (!Character.isWhitespace(cs.charAt(i))) {
                return false;
            }
        }
        return true;
    }

    public static int length(final CharSequence cs) {
        return cs == null ? 0 : cs.length();
    }

    public static boolean isNotBlank(final CharSequence cs) {
        return !isBlank(cs);
    }

    // ==================== 危险转换方法（会抛异常） ====================

    /**
     * 解析字符串为整数，转换失败抛出异常
     * @param str 整数字符串
     * @return 转换后的整数
     * @throws NumberFormatException 如果字符串格式不正确或为空
     */
    public static Integer parseInteger(String str){
        if(isBlank(str)){
            throw new NumberFormatException("Cannot parse blank string to Integer");
        }
        return Integer.parseInt(str);
    }

    /**
     * 解析字符串为长整数，转换失败抛出异常
     * @param str 长整数字符串
     * @return 转换后的长整数
     * @throws NumberFormatException 如果字符串格式不正确或为空
     */
    public static Long parseLong(String str){
        if(isBlank(str)){
            throw new NumberFormatException("Cannot parse blank string to Long");
        }
        return Long.parseLong(str);
    }

    /**
     * 解析字符串为双精度浮点数，转换失败抛出异常
     * @param str 浮点数字符串
     * @return 转换后的双精度浮点数
     * @throws NumberFormatException 如果字符串格式不正确或为空
     */
    public static Double parseDouble(String str){
        if(isBlank(str)){
            throw new NumberFormatException("Cannot parse blank string to Double");
        }
        return Double.parseDouble(str);
    }

    /**
     * 解析字符串为单精度浮点数，转换失败抛出异常
     * @param str 浮点数字符串
     * @return 转换后的单精度浮点数
     * @throws NumberFormatException 如果字符串格式不正确或为空
     */
    public static Float parseFloat(String str){
        if(isBlank(str)){
            throw new NumberFormatException("Cannot parse blank string to Float");
        }
        return Float.parseFloat(str);
    }

    /**
     * 解析字符串为短整数，转换失败抛出异常
     * @param str 短整数字符串
     * @return 转换后的短整数
     * @throws NumberFormatException 如果字符串格式不正确或为空
     */
    public static Short parseShort(String str){
        if(isBlank(str)){
            throw new NumberFormatException("Cannot parse blank string to Short");
        }
        return Short.parseShort(str);
    }

    /**
     * 解析字符串为字节，转换失败抛出异常
     * @param str 字节字符串
     * @return 转换后的字节
     * @throws NumberFormatException 如果字符串格式不正确或为空
     */
    public static Byte parseByte(String str){
        if(isBlank(str)){
            throw new NumberFormatException("Cannot parse blank string to Byte");
        }
        return Byte.parseByte(str);
    }

    /**
     * 解析字符串为布尔值，转换失败抛出异常
     * 支持 "true"/"false"（不区分大小写）、"1"/"0"、"yes"/"no"（不区分大小写）
     * @param str 布尔字符串
     * @return 转换后的布尔值
     * @throws IllegalArgumentException 如果字符串格式不正确或为空
     */
    public static Boolean parseBoolean(String str){
        if(isBlank(str)){
            throw new IllegalArgumentException("Cannot parse blank string to Boolean");
        }
        String lowerStr = str.toLowerCase().trim();
        if("true".equals(lowerStr) || "1".equals(lowerStr) || "yes".equals(lowerStr)){
            return true;
        }
        if("false".equals(lowerStr) || "0".equals(lowerStr) || "no".equals(lowerStr)){
            return false;
        }
        throw new IllegalArgumentException("Cannot parse \"" + str + "\" to Boolean");
    }

    /**
     * 解析字符串为日期，使用默认格式，转换失败抛出异常
     * @param str 日期字符串
     * @return 转换后的日期
     * @throws DateTimeException 如果字符串格式不正确或为空
     */
    public static LocalDate parseLocalDate(String str){
        return parseLocalDate(str, DEFAULT_DATE_FORMAT);
    }

    /**
     * 解析字符串为日期，使用自定义格式，转换失败抛出异常
     * @param str 日期字符串
     * @param format 日期格式
     * @return 转换后的日期
     * @throws DateTimeException 如果字符串格式不正确或为空
     */
    public static LocalDate parseLocalDate(String str, String format){
        if(isBlank(str)){
            throw new DateTimeException("Cannot parse blank string to LocalDate");
        }
        return LocalDate.parse(str, DateTimeFormatter.ofPattern(format));
    }

    /**
     * 解析字符串为日期时间，使用默认格式，转换失败抛出异常
     * @param str 日期时间字符串
     * @return 转换后的日期时间
     * @throws DateTimeException 如果字符串格式不正确或为空
     */
    public static LocalDateTime parseLocalDateTime(String str){
        return parseLocalDateTime(str, DEFAULT_DATE_TIME_FORMAT);
    }

    /**
     * 解析字符串为日期时间，使用自定义格式，转换失败抛出异常
     * @param str 日期时间字符串
     * @param format 日期时间格式
     * @return 转换后的日期时间
     * @throws DateTimeException 如果字符串格式不正确或为空
     */
    public static LocalDateTime parseLocalDateTime(String str, String format){
        if(isBlank(str)){
            throw new DateTimeException("Cannot parse blank string to LocalDateTime");
        }
        return LocalDateTime.parse(str, DateTimeFormatter.ofPattern(format));
    }

    /**
     * 解析字符串为时间，使用默认格式，转换失败抛出异常
     * @param str 时间字符串
     * @return 转换后的时间
     * @throws DateTimeException 如果字符串格式不正确或为空
     */
    public static LocalTime parseLocalTime(String str){
        return parseLocalTime(str, DEFAULT_TIME_FORMAT);
    }

    /**
     * 解析字符串为时间，使用自定义格式，转换失败抛出异常
     * @param str 时间字符串
     * @param format 时间格式
     * @return 转换后的时间
     * @throws DateTimeException 如果字符串格式不正确或为空
     */
    public static LocalTime parseLocalTime(String str, String format){
        if(isBlank(str)){
            throw new DateTimeException("Cannot parse blank string to LocalTime");
        }
        return LocalTime.parse(str, DateTimeFormatter.ofPattern(format));
    }

    // ==================== 安全转换方法（不抛异常，需提供默认值） ====================

    /**
     * 安全地将字符串转换为整数，转换失败返回默认值
     * @param str 整数字符串
     * @param defaultValue 转换失败时返回的默认值
     * @return 转换后的整数，失败返回默认值
     */
    public static Integer toInteger(String str, Integer defaultValue){
        if(isBlank(str)){
            return defaultValue;
        }
        try{
            return Integer.parseInt(str);
        }catch(NumberFormatException e){
            return defaultValue;
        }
    }

    /**
     * 安全地将字符串转换为长整数，转换失败返回默认值
     * @param str 长整数字符串
     * @param defaultValue 转换失败时返回的默认值
     * @return 转换后的长整数，失败返回默认值
     */
    public static Long toLong(String str, Long defaultValue){
        if(isBlank(str)){
            return defaultValue;
        }
        try{
            return Long.parseLong(str);
        }catch(NumberFormatException e){
            return defaultValue;
        }
    }

    /**
     * 安全地将字符串转换为双精度浮点数，转换失败返回默认值
     * @param str 浮点数字符串
     * @param defaultValue 转换失败时返回的默认值
     * @return 转换后的双精度浮点数，失败返回默认值
     */
    public static Double toDouble(String str, Double defaultValue){
        if(isBlank(str)){
            return defaultValue;
        }
        try{
            return Double.parseDouble(str);
        }catch(NumberFormatException e){
            return defaultValue;
        }
    }

    /**
     * 安全地将字符串转换为单精度浮点数，转换失败返回默认值
     * @param str 浮点数字符串
     * @param defaultValue 转换失败时返回的默认值
     * @return 转换后的单精度浮点数，失败返回默认值
     */
    public static Float toFloat(String str, Float defaultValue){
        if(isBlank(str)){
            return defaultValue;
        }
        try{
            return Float.parseFloat(str);
        }catch(NumberFormatException e){
            return defaultValue;
        }
    }

    /**
     * 安全地将字符串转换为短整数，转换失败返回默认值
     * @param str 短整数字符串
     * @param defaultValue 转换失败时返回的默认值
     * @return 转换后的短整数，失败返回默认值
     */
    public static Short toShort(String str, Short defaultValue){
        if(isBlank(str)){
            return defaultValue;
        }
        try{
            return Short.parseShort(str);
        }catch(NumberFormatException e){
            return defaultValue;
        }
    }

    /**
     * 安全地将字符串转换为字节，转换失败返回默认值
     * @param str 字节字符串
     * @param defaultValue 转换失败时返回的默认值
     * @return 转换后的字节，失败返回默认值
     */
    public static Byte toByte(String str, Byte defaultValue){
        if(isBlank(str)){
            return defaultValue;
        }
        try{
            return Byte.parseByte(str);
        }catch(NumberFormatException e){
            return defaultValue;
        }
    }

    /**
     * 安全地将字符串转换为布尔值，转换失败返回默认值
     * 支持 "true"/"false"（不区分大小写）、"1"/"0"、"yes"/"no"（不区分大小写）
     * @param str 布尔字符串
     * @param defaultValue 转换失败时返回的默认值
     * @return 转换后的布尔值，失败返回默认值
     */
    public static Boolean toBoolean(String str, Boolean defaultValue){
        if(isBlank(str)){
            return defaultValue;
        }
        String lowerStr = str.toLowerCase().trim();
        if("true".equals(lowerStr) || "1".equals(lowerStr) || "yes".equals(lowerStr)){
            return true;
        }
        if("false".equals(lowerStr) || "0".equals(lowerStr) || "no".equals(lowerStr)){
            return false;
        }
        return defaultValue;
    }

    /**
     * 安全地将字符串转换为日期，使用默认格式，转换失败返回默认值
     * @param str 日期字符串
     * @param defaultValue 转换失败时返回的默认值
     * @return 转换后的日期，失败返回默认值
     */
    public static LocalDate toLocalDate(String str, LocalDate defaultValue){
        return toLocalDate(str, DEFAULT_DATE_FORMAT, defaultValue);
    }

    /**
     * 安全地将字符串转换为日期，使用自定义格式，转换失败返回默认值
     * @param str 日期字符串
     * @param format 日期格式
     * @param defaultValue 转换失败时返回的默认值
     * @return 转换后的日期，失败返回默认值
     */
    public static LocalDate toLocalDate(String str, String format, LocalDate defaultValue){
        if(isBlank(str)){
            return defaultValue;
        }
        try{
            return LocalDate.parse(str, DateTimeFormatter.ofPattern(format));
        }catch(DateTimeException e){
            return defaultValue;
        }
    }

    /**
     * 安全地将字符串转换为日期时间，使用默认格式，转换失败返回默认值
     * @param str 日期时间字符串
     * @param defaultValue 转换失败时返回的默认值
     * @return 转换后的日期时间，失败返回默认值
     */
    public static LocalDateTime toLocalDateTime(String str, LocalDateTime defaultValue){
        return toLocalDateTime(str, DEFAULT_DATE_TIME_FORMAT, defaultValue);
    }

    /**
     * 安全地将字符串转换为日期时间，使用自定义格式，转换失败返回默认值
     * @param str 日期时间字符串
     * @param format 日期时间格式
     * @param defaultValue 转换失败时返回的默认值
     * @return 转换后的日期时间，失败返回默认值
     */
    public static LocalDateTime toLocalDateTime(String str, String format, LocalDateTime defaultValue){
        if(isBlank(str)){
            return defaultValue;
        }
        try{
            return LocalDateTime.parse(str, DateTimeFormatter.ofPattern(format));
        }catch(DateTimeException e){
            return defaultValue;
        }
    }

    /**
     * 安全地将字符串转换为时间，使用默认格式，转换失败返回默认值
     * @param str 时间字符串
     * @param defaultValue 转换失败时返回的默认值
     * @return 转换后的时间，失败返回默认值
     */
    public static LocalTime toLocalTime(String str, LocalTime defaultValue){
        return toLocalTime(str, DEFAULT_TIME_FORMAT, defaultValue);
    }

    /**
     * 安全地将字符串转换为时间，使用自定义格式，转换失败返回默认值
     * @param str 时间字符串
     * @param format 时间格式
     * @param defaultValue 转换失败时返回的默认值
     * @return 转换后的时间，失败返回默认值
     */
    public static LocalTime toLocalTime(String str, String format, LocalTime defaultValue){
        if(isBlank(str)){
            return defaultValue;
        }
        try{
            return LocalTime.parse(str, DateTimeFormatter.ofPattern(format));
        }catch(DateTimeException e){
            return defaultValue;
        }
    }

}

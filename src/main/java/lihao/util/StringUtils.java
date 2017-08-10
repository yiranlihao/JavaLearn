package lihao.util;

public abstract class StringUtils {

    //目录分隔符
    private static final String FOLDER_SEPARATOR = "/";

    //Windows目录分隔符
    private static final String WINDOWS_FOLDER_SEPARATOR = "\\";

    //顶层路径
    private static final String TOP_PATH = "..";

    //当前路径
    private static final String CURRENT_PATH = ".";

    //扩展分隔符
    private static final char EXTENSION_SEPARATOR = '.';


    //---------------------------------------------------------------------
    // General convenience methods for working with Strings
    //---------------------------------------------------------------------

    /**
     * 判断字符串 {@code String} 是否为空.
     * @param str String字符串
     * @since 0.0.1
     * @uthor lihao
     */
    public static boolean isEmpty(Object str) {
        return (str == null || "".equals(str));
    }

    /**
     * 判断字符串 {@code String} 是否不为空.
     * @param str String字符串
     * @since 0.0.1
     * @uthor lihao
     */
    public static boolean isNotEmpty(Object str) {
        return !(str == null || "".equals(str));
    }

    /**
     * Check that the given {@code CharSequence} is neither {@code null} nor
     * of length 0.
     * <p>Note: this method returns {@code true} for a {@code CharSequence}
     * that purely consists of whitespace.
     * <p><pre class="code">
     * StringUtils.hasLength(null) = false
     * StringUtils.hasLength("") = false
     * StringUtils.hasLength(" ") = true
     * StringUtils.hasLength("Hello") = true
     * </pre>
     * @param str the {@code CharSequence} to check (may be {@code null})
     * @return {@code true} if the {@code CharSequence} is not {@code null} and has length
     * @see #hasText(String)
     */
    public static boolean hasLength(CharSequence str) {
        return (str != null && str.length() > 0);
    }

    /**
     * Check that the given {@code String} is neither {@code null} nor of length 0.
     * <p>Note: this method returns {@code true} for a {@code String} that
     * purely consists of whitespace.
     * @param str the {@code String} to check (may be {@code null})
     * @return {@code true} if the {@code String} is not {@code null} and has length
     * @see #hasLength(CharSequence)
     * @see #hasText(String)
     */
    public static boolean hasLength(String str) {
        return hasLength((CharSequence) str);
    }



}

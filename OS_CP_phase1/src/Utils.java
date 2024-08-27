public class Utils {
    public static boolean isWhitespace(String s) {
        return s.trim().isEmpty();
    }

    public static String getInstruction(String line, int address, int wordSize) {
        StringBuilder temp = new StringBuilder("\t".repeat(wordSize));
        for (int i = address; i < address + wordSize; i++) {
            if (i >= line.length()) {
                break;
            }
            temp.setCharAt(i - address, line.charAt(i));
        }
        return temp.toString();
    }

    public static String trim(String s) {
        return trim(s, true, true);
    }
    public static String trim(String s, boolean trimStart, boolean trimEnd) {
        String temp = s;
        if (trimStart) {
            int startIndex = 0;
            while (startIndex < temp.length() && isWhitespace(String.valueOf(temp.charAt(startIndex)))) {
                startIndex++;
            }
            temp = temp.substring(startIndex);
        }
        if (trimEnd) {
            int endIndex = temp.length() - 1;
            while (endIndex >= 0 && isWhitespace(String.valueOf(temp.charAt(endIndex)))) {
                endIndex--;
            }
            temp = temp.substring(0, endIndex + 1);
        }
        return temp;
    }

    public static String removeNonPrintable(String input) {
        StringBuilder result = new StringBuilder();
        for (char ch : input.toCharArray()) {
            if (ch >= 32 && ch <= 126) {
                result.append(ch);
            }
        }
        return result.toString();
    }
}

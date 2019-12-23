package lab1.obfuscation;

class Obfuscator {

    private static String source="ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
    private static String target="mWOyuQHI9FTgipC4noG2XcfJ0EhdeNlbx7VMAwB6r8UtPDSskzqvYZj3RaL5K1";

    static String obfuscate(String s) {
        return getString(s, source, target);
    }

    static String unobfuscate(String s) {
        return getString(s, target, source);
    }

    private static String getString(String s, String target, String source) {
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            int index = source.indexOf(c);
            stringBuilder.append(target.charAt(index));
        }
        return stringBuilder.toString();
    }
}

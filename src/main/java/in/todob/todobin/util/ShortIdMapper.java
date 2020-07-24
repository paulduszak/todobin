package in.todob.todobin.util;

public final class ShortIdMapper {
    private static final String ALPHABET = "juBT9FtJgEqbNXCAsdcGmLkvSpW7ZDn5h46yMwP3YHQraeKfoUzRx82";
    private static final int BASE = ALPHABET.length();

    public static String encode(Long id) {
        StringBuilder encodedId = new StringBuilder();
        while (id > 0) {
            encodedId.insert(0, ALPHABET.charAt((int) (id % BASE)));
            id = id / BASE;
        }
        return encodedId.toString();
    }

    public static Long decode(String shortId) {
        Long id = 0L;
        for (int i = 0; i < shortId.length(); i++) {
            id = id * BASE + ALPHABET.indexOf(shortId.charAt(i));
        }
        return id;
    }
}

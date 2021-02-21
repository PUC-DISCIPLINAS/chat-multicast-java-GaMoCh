package lib;

public class MulticastIpGenerator {
    private static final long MIN_IP_HEX = Long.parseLong("E0000000", 16);
    private static final long MAX_IP_HEX = Long.parseLong("EFFFFFFF", 16);

    long ip = MIN_IP_HEX - 1;

    public String generate() {
        if (++ip > MAX_IP_HEX) {
            ip = MIN_IP_HEX;
        }

        return String.format("%d.%d.%d.%d", (ip >> 24) & 0xff, (ip >> 16) & 0xff, (ip >> 8) & 0xff, ip & 0xff);
    }
}

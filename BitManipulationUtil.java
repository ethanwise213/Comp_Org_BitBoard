public class BitManipulationUtil {

    // Method to set a specific bit
    public static int setBit(int bitboard, int position) {
        return bitboard | (1 << position);
    }

    // Method to clear a specific bit
    public static int clearBit(int bitboard, int position) {
        return bitboard & ~(1 << position);
    }

    // Method to get the value of a specific bit
    public static boolean getBit(int bitboard, int position) {
        return (bitboard & (1 << position)) != 0;
    }

    // Method to convert the bitboard to a binary string representation
    public static String toBinaryString(int bitboard) {
        return String.format("%32s", Integer.toBinaryString(bitboard)).replace(' ', '0');
    }

    // Method to convert the bitboard to a hexadecimal string representation
    public static String toHexString(int bitboard) {
        return String.format("%08X", bitboard);
    }
}
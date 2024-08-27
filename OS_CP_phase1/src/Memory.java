public class Memory {
    private char[] data;
    public final int wordSize;
    public final int blockSize;
    public final int memorySize;

    public Memory(int wordSize, int blockSize, int memorySize) {
        this.wordSize = wordSize;
        this.blockSize = blockSize;
        this.memorySize = memorySize;
        this.data = new char[memorySize];
        reset();
    }

    public String readWord(int address) {
        StringBuilder word = new StringBuilder();
        for (int i = 0; i < wordSize; i++) {
            word.append(data[address + i]); //check
        }
        return word.toString();
    }

    public String readBlock(int address) {
        StringBuilder block = new StringBuilder();
        for (int i = 0; i < blockSize; i++) {
            block.append(data[address + i]);
        }
        return block.toString();
    }

    public int writeWord(int address, String value) {
        int size = value.length();
        if (size > wordSize) {
            return MemoryError.VALUE_TOO_LARGE;
        }
        if (size < wordSize) {
            return MemoryError.VALUE_TOO_SMALL;
        }
        if (address >= memorySize - wordSize) {
            return MemoryError.INVALID_ADDRESS;
        }
        for (int i = 0; i < wordSize; i++) {
            data[address + i] = value.charAt(i);
        }
        return 0; // success
    }

    public int writeBlock(int address, String value) {
        int size = value.length();
        if (size > blockSize) {
            return MemoryError.VALUE_TOO_LARGE;
        }
        if (size < blockSize) {
            return MemoryError.VALUE_TOO_SMALL;
        }
        if (address >= memorySize - blockSize) {
            return MemoryError.INVALID_ADDRESS;
        }
        for (int i = 0; i < Math.min(size, blockSize); i++) {
            data[address + i] = value.charAt(i);
        }
        return 0; // success;
    }

    public void printMemory() {
        for (int i = 0; i < memorySize - 200; i += wordSize) {
            System.out.println("(" + i + "): " + this.readWord(i));
        }
    }
//    public void printMemory() {
//        int rows = 100;
//        int cols = 4;
//
//        for (int i = 0; i < rows; i++) {
//            System.out.printf("(%02d) : ", i);
//            for (int j = 0; j < cols; j++) {
//                int address = i * cols + j;
//                System.out.print(memoryData[address] + "   |   ");
//            }
//            System.out.println();
//        }
//    }


    public void reset() {
        for (int i = 0; i < memorySize; i++) {
            data[i] = ' ';
        }
    }
}

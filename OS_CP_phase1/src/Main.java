import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {
        CPU cpu = new CPU();
        Memory memory = new Memory(4, 40, 400);

        cpu.setMemory(memory);
        cpu.setInputFile("sample_input.txt");
        cpu.setOutputFile("sample_output.txt");

        cpu.startExecution();
    }
}
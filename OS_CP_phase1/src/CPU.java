import java.io.*;
public class CPU {
    private int IC;
    private Register IP;
    private Register C;
    private Register R;
    private Register SI;
    private Memory memory;
    private BufferedReader input;
    private PrintWriter output;

    boolean shouldIncrementIC = true;

    public CPU() {
        this.IC = 0;
        this.IP = new Register(4);
        this.C = new Register(1);
        this.R = new Register(4);
        this.SI = new Register(2);

        this.IP.write("0000");
        this.C.write("0");
        this.R.write("0000");
        this.SI.write("00");
    }

    public void setMemory(Memory memory) {
        this.memory = memory;
    }

    public void setInputFile(String filename) {
        try {
            this.input = new BufferedReader(new FileReader(filename));
        } catch (FileNotFoundException e) {
            System.out.println("Input file not found.");
        }
    }

    public void setOutputFile(String filename) {
        try {
            this.output = new PrintWriter(new FileWriter(filename));
        } catch (IOException e) {
            System.out.println("Output file not found.");
        }
    }

    public void startExecution() throws IOException {
        if (this.memory == null) {
            System.err.println("Memory not set.");
            return;
        }
        if (this.input == null) {
            System.err.println("Input file not set.");
            return;
        }
        if (this.output ==  null) {
            System.err.println("Output file not set.");
            return;
        }

        System.out.println("Execution started...!");

        String line;
        int jobId = 0;
        int timeLimit = 0;
        int lineLimit = 0;
        int startAddress = 0;

        while ((line = input.readLine()) != null) {
            String prefix = line.substring(0, 4);
            switch (prefix) {
                case "$AMJ" -> {
                    jobId = Integer.parseInt(line.substring(4, 8));
                    timeLimit = Integer.parseInt(line.substring(8, 12));
                    lineLimit = Integer.parseInt(line.substring(12, 16));
                }
                case "$DTA" -> {
                    Job job = new Job(jobId, timeLimit, lineLimit);
                    this.executeJob(job);
                }
                case "$END" -> {
                    this.reset();
                    startAddress = 0;
                }
                default -> {
                    for (int i = 0; i < line.length(); i += this.memory.wordSize) {
                        String instruction = Utils.getInstruction(line, i, this.memory.wordSize);
                        if(Utils.isWhitespace(instruction)) {
                            continue;
                        }
                        this.memory.writeWord(startAddress, instruction);
                        startAddress += this.memory.wordSize;
                    }
                }
            }
        }
    }

    public void executeJob(Job job) throws IOException {
        System.out.println("Executing job " + job.getJobId());

        while (this.IC < job.timeLimit) {
            Instruction instruction = this.loadInstruction(this.IC * this.memory.wordSize);
            System.out.println("Executing instruction: " + instruction.getMnemonic() + " " + instruction.getOperand());

            switch (instruction.getMnemonic()) {
                case GetData:
                case PutData:
                case Halt:
                    this.SI.write(String.valueOf(instruction.getMnemonic()));
                    this.kernelSpaceInstruction(instruction);
                    break;
                default:
                    this.userSpaceInstruction(instruction);
                    break;
            }

            if (shouldIncrementIC) {
                this.IC += 1;
            } else {
                shouldIncrementIC = true;
            }

            this.printRegs();
            this.memory.printMemory();
            System.out.println("--------------------------------------------");
        }
    }

    private Instruction loadInstruction(int startAddress) {
        String instruction = this.memory.readWord(startAddress);
        this.IP.write(instruction);

        String mnemonic = instruction.substring(0, this.memory.wordSize / 2);
        String operand = instruction.substring(this.memory.wordSize / 2, this.memory.wordSize / 2);

        return new Instruction(mnemonic, operand);
    }

    private void userSpaceInstruction(Instruction instruction) {
        String data;
        switch (instruction.getMnemonic()) {
            case Load:
                System.out.println("Loading");
                data = this.memory.readWord(instruction.getOperand() * this.memory.wordSize);
                this.R.write(data);
                break;
            case Store:
                data = this.R.read();
                this.memory.writeWord(instruction.getOperand() * this.memory.wordSize, data);
                break;
            case Compare:
                data = this.memory.readWord(instruction.getOperand() * this.memory.wordSize);
                if (this.R.read().equals(data)) {
                    this.C.write("1");
                } else {
                    this.C.write("0");
                }
                break;
            case BranchIfTrue:
                if (this.C.read().equals("1")) {
                    System.out.println("Setting IC to: " + instruction.getOperand());
                    this.IC = instruction.getOperand();
                    shouldIncrementIC = false;
                }
                break;
            default:
                break;
        }
    }

    private void kernelSpaceInstruction(Instruction instruction) throws IOException {
        if (instruction.getMnemonic() == Instruction.InstructionType.Halt) {
            this.output.println();
            this.output.println();
            this.output.println();
            return;
        }

        if (instruction.getMnemonic() == Instruction.InstructionType.GetData) {
            String data = this.input.readLine();
            this.memory.writeBlock(instruction.getOperand() * this.memory.wordSize, data);
        }

        if (instruction.getMnemonic() == Instruction.InstructionType.PutData) {
            String data = this.memory.readBlock(instruction.getOperand() * this.memory.wordSize);
            data = Utils.trim(data, false, true);
            data = Utils.removeNonPrintable(data);
            this.output.write(data);
            this.output.println();
        }
    }

    private void printRegs() {
        System.out.println("IP: " + this.IP.read());
        System.out.println("IC: " + this.IC);
        System.out.println("C: " + this.C.read());
        System.out.println("R: " + this.R.read());
        System.out.println("SI: " + this.SI.read());
    }

    public void reset() {
        this.IC = 0;
        this.IP.write("0000");
        this.C.write("0");
        this.R.write("0000");
        this.SI.write("00");
        this.memory.reset();
    }





}

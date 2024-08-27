public class Instruction {
    public InstructionType getMnemonic() {
        return this.mnemonic;
    }

    public int getOperand() {
        return this.operand;
    }

    public enum InstructionType {
        GetData,
        PutData,
        Halt,
        Load,
        Store,
        Compare,
        BranchIfTrue,
        Invalid
    }

    private InstructionType mnemonic;
    public int operand;
    public int operandCount;

    public Instruction(String mnemonic, String operand) {
        mnemonic = Utils.trim(mnemonic);

        if (mnemonic.isEmpty()) {
            this.mnemonic = InstructionType.Invalid;
            this.operand = 0;
            return;
        } else if (mnemonic.equals("GD")) {
            this.mnemonic = InstructionType.GetData;
        } else if (mnemonic.equals("PD")) {
            this.mnemonic = InstructionType.PutData;
        } else if (mnemonic.equals("H")) {
            this.mnemonic = InstructionType.Halt;
            this.operand = 0;
            return;
        } else if (mnemonic.equals("LR")) {
            this.mnemonic = InstructionType.Load;
        } else if (mnemonic.equals("SR")) {
            this.mnemonic = InstructionType.Store;
        } else if (mnemonic.equals("C")) {
            this.mnemonic = InstructionType.Compare;
        } else if (mnemonic.equals("BT")) {
            this.mnemonic = InstructionType.BranchIfTrue;
        } else {
            this.mnemonic = InstructionType.Invalid;
            this.operand = 0;
            return;
        }

        this.operandCount = 1;
        this.operand = Integer.parseInt(String.valueOf(operand));
    }
}

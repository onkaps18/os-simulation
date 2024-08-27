public class Register {
    private String data;
    private int size;

    public Register(int size) {
        this.size = size;
        this.data = "0".repeat(size);
    }

    public String read() {
        return this.data;
    }

    public void write(String data) {
        if(data.length() > this.size) {
            this.data = data.substring(0, this.size);
        } else {
            this.data = data;
        }
    }
}

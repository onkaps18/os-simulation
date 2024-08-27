public class Job {
    public int jobId;
    public int timeLimit;
    public int lineLimit;

    public Job(int jobId, int timeLimit, int lineLimit) {
        this.jobId = jobId;
        this.timeLimit = timeLimit;
        this.lineLimit = lineLimit;
    }

    public int getJobId() {
        return this.jobId;
    }
}
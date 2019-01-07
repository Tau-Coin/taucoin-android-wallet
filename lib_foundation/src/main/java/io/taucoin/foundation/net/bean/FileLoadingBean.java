package io.taucoin.foundation.net.bean;

public class FileLoadingBean {
    private long total;
    private long progress;

    public long getProgress() {
        return progress;
    }

    public long getTotal() {
        return total;
    }

    public FileLoadingBean(long total, long progress) {
        this.total = total;
        this.progress = progress;
    }
}

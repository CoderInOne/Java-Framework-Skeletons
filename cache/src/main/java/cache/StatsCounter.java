package cache;

import java.util.concurrent.atomic.LongAdder;

public class StatsCounter {
    private LongAdder hitCounter;
    private LongAdder missingCounter;

    public StatsCounter() {
        this.hitCounter = new LongAdder();
        this.missingCounter = new LongAdder();
    }

    public void recordHits(int size) {
        this.hitCounter.add(size);
    }

    public long getHitsCount() {
        return this.hitCounter.longValue();
    }

    public void recordMisses(int size) {
        this.missingCounter.add(size);
    }

    public long getMissesCount() {
        return this.missingCounter.longValue();
    }
}

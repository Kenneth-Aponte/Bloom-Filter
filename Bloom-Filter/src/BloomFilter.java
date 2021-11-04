import java.io.File;

public class BloomFilter {
    final double FALSE_POSITIVE_PROBABILITY =  0.00000001;//final as this value is predetermined
    long dataCount;
    double filterSize;
    double hashCount;

    //constructor
    public BloomFilter(long dataCount, double filterSize, double hashCount){
        this.dataCount = dataCount;
        this.filterSize = filterSize;
        this.hashCount = hashCount;
    }

    //TODO: Complete class implementation

}

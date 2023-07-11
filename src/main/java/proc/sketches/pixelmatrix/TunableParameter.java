package proc.sketches.pixelmatrix;

public class TunableParameter<T> {

    public T value;
    T startingValue;
    T min;
    T max;

    public TunableParameter(T startingValue){
        this.startingValue = startingValue;
        this.value = startingValue;
    }

    public TunableParameter(T startingValue, T min, T max){
        this.startingValue = startingValue;
        this.value = startingValue;
        this.min = min;
        this.max = max;
    }
}

package ru.vlad.loyalty.util;

import java.util.function.ToDoubleFunction;
import java.util.function.ToIntFunction;
import java.util.stream.Collector;

class AverageCollector {

    private AverageCollector(){
        throw new AssertionError();
    }

    /**
     * This collector is used for calculating weighted average
     * @param valueFunction function that provides local data (average vote, consuption of water and energy)
     * @param weightFunction function that provides weights for average (i.e. number of votes, population of cities, etc)
     * @param <T>
     * @return collector for weighted average
     */
    static <T> Collector<T, ?, Box> averagingWeighted(ToDoubleFunction<T> valueFunction,
                                                         ToIntFunction<T> weightFunction) {
        return Collector.of(
                Box::new,
                (b, e) -> {
                    b.setNum(b.getNum() + valueFunction.applyAsDouble(e) * weightFunction.applyAsInt(e));
                    b.setDen(b.getDen() + weightFunction.applyAsInt(e));
                    },
                (b1, b2) -> {
                    b1.setNum(b1.getNum() + b2.getNum());
                    b1.setDen(b1.getDen() + b2.getDen());
                    return b1;
                    },
                b -> b
        );
    }
}

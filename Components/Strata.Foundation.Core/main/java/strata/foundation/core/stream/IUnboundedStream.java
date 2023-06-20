//////////////////////////////////////////////////////////////////////////////
// IUnboundedStream.java
//////////////////////////////////////////////////////////////////////////////

package strata.foundation.core.stream;

public
interface IUnboundedStream<T>
    extends IStream<T>
{
    <K> IKeyedUnboundedStream<K,T>
    keyBy(IKeySelector<K,T> selector);

    ITimeWindowedUnboundedStream<T>
    windowBy(TimeAmount window);

    ITimeWindowedUnboundedStream<T>
    windowBy(TimeAmount window,ITimestampAssigner<T> assigner);

    ICountWindowedUnboundedStream<T>
    windowBy(long count);

    @Override
    IExecutableUnboundedStream<T>
    filter(IPredicate<? super T> predicate);

    @Override
    <R> IExecutableUnboundedStream<R>
    map(IFunction<? super T,? extends R> mapper);

    @Override
    <R> IExecutableUnboundedStream<R>
    flatMap(IFunction<T,Iterable<R>> mapper);

    <R> IExecutableUnboundedStream<R>
    flatMap(IFunction<T,Iterable<R>> mapper,Class<R> outputType);

    IUnboundedStreamExecutor
    forEach(IConsumer<? super T> action);

    IUnboundedStreamExecutor
    sinkTo(IUnboundedStreamSink<T> sink);
}

//////////////////////////////////////////////////////////////////////////////
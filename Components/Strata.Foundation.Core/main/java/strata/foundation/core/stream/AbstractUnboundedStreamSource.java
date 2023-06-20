//////////////////////////////////////////////////////////////////////////////
// AbstractUnboundedStreamSource.java
//////////////////////////////////////////////////////////////////////////////

package strata.foundation.core.stream;

public abstract
class AbstractUnboundedStreamSource<T>
    implements IUnboundedStreamSource<T>
{
    @Override
    public <K> IKeyedUnboundedStream<K,T>
    keyBy(IKeySelector<K,T> selector)
    {
        return getStream().keyBy(selector);
    }

    @Override
    public ITimeWindowedUnboundedStream<T>
    windowBy(TimeAmount window)
    {
        return getStream().windowBy(window);
    }

    @Override
    public ITimeWindowedUnboundedStream<T>
    windowBy(TimeAmount window,ITimestampAssigner<T> assigner)
    {
        return getStream().windowBy(window,assigner);
    }

    @Override
    public ICountWindowedUnboundedStream<T>
    windowBy(long count)
    {
        return getStream().windowBy(count);
    }

    @Override
    public IExecutableUnboundedStream<T>
    filter(IPredicate<? super T> predicate)
    {
        return getStream().filter(predicate);
    }

    @Override
    public <R> IExecutableUnboundedStream<R>
    map(IFunction<? super T,? extends R> mapper)
    {
        return getStream().map(mapper);
    }

    @Override
    public <R> IExecutableUnboundedStream<R>
    flatMap(IFunction<T,Iterable<R>> mapper)
    {
        return getStream().flatMap(mapper);
    }

    @Override
    public <R> IExecutableUnboundedStream<R>
    flatMap(IFunction<T,Iterable<R>> mapper,Class<R> outputType)
    {
        return getStream().flatMap(mapper,outputType);
    }

    @Override
    public IUnboundedStreamExecutor
    forEach(IConsumer<? super T> action)
    {
        return getStream().forEach(action);
    }

    @Override
    public IUnboundedStreamExecutor
    sinkTo(IUnboundedStreamSink<T> sink)
    {
        return getStream().sinkTo(sink);
    }

    protected abstract IUnboundedStream<T>
    getStream();
}

//////////////////////////////////////////////////////////////////////////////

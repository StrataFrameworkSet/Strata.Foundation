/// ///////////////////////////////////////////////////////////////////////////
// StoppableBlockingQueue.java
//////////////////////////////////////////////////////////////////////////////

package strata.foundation.core.concurrent;

import strata.foundation.core.utility.OptionalExtension;

import java.util.*;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

public
class StoppableBlockingQueue<T>
    implements IBlockingQueue<T>
{
    private final BlockingQueue<StopContext<T>> implementation;
    private final AtomicBoolean                 stopped;

    public
    StoppableBlockingQueue()
    {
        this(new LinkedBlockingQueue<>());
    }

    public
    StoppableBlockingQueue(BlockingQueue<StopContext<T>> queue)
    {
        Objects.requireNonNull(queue,"queue must not be null");
        implementation = queue;
        stopped = new AtomicBoolean(false);
    }

    @Override
    public boolean
    add(T t)
    {
        stopped.set(false);
        return implementation.add(StopContext.of(t));
    }

    @Override
    public boolean
    offer(T t)
    {
        stopped.set(false);
        return implementation.offer(StopContext.of(t));
    }

    @Override
    public T
    remove()
        throws StoppedException
    {
        if (isStopped())
            throw new StoppedException("Queue was stopped");

        StopContext<T> context = implementation.remove();

        if (isStopped(context))
            throw new StoppedException("Queue was stopped");

        return getElement(context);
    }

    @Override
    public T
    poll()
        throws StoppedException
    {
        if (isStopped())
            throw new StoppedException("Queue was stopped");

        StopContext<T> context = implementation.poll();

        if (isStopped(context))
            throw new StoppedException("Queue was stopped");

        return getElement(context);
    }

    @Override
    public T
    element()
        throws StoppedException
    {
        if (isStopped())
            throw new StoppedException("Queue was stopped");

        StopContext<T> context = implementation.element();

        if (isStopped(context))
            throw new StoppedException("Queue was stopped");

        return getElement(context);
    }

    @Override
    public T
    peek()
        throws StoppedException
    {
        if (isStopped())
            throw new StoppedException("Queue was stopped");

        StopContext<T> context = implementation.peek();

        if (isStopped(context))
            throw new StoppedException("Queue was stopped");

        return getElement(context);
    }

    @Override
    public void
    put(T t)
        throws InterruptedException
    {
        stopped.set(false);
        implementation.put(StopContext.of(t));
    }

    @Override
    public boolean
    offer(T t,long timeout,TimeUnit unit) throws InterruptedException
    {
        return implementation.offer(StopContext.of(t), timeout, unit);
    }

    @Override
    public T
    take()
        throws InterruptedException,StoppedException
    {
        if (isStopped())
            throw new StoppedException("Queue was stopped");

        StopContext<T> context = implementation.take();

        if (isStopped(context))
            throw new StoppedException("Queue was stopped");

        return getElement(context);
    }

    @Override
    public T
    poll(long timeout,TimeUnit unit) throws InterruptedException
    {
        if (isStopped())
            throw new StoppedException("Queue was stopped");

        StopContext<T> context = implementation.poll(timeout, unit);

        if (isStopped(context))
            throw new StoppedException("Queue was stopped");

        return getElement(context);
    }

    @Override
    public int
    remainingCapacity()
    {
        return implementation.remainingCapacity();
    }

    @Override
    public boolean
    remove(Object o)
    {
        return
            OptionalExtension
                .ifPresentOrElse(
                    implementation
                        .stream()
                        .filter(
                            context ->
                                Objects.equals(getElement(context), o))
                        .findFirst(),
                    context -> implementation.remove(context),
                    () -> false);
    }

    @Override
    public boolean
    containsAll(Collection<?> elements)
    {
        return
            implementation.containsAll(
                elements
                    .stream()
                    .map(StopContext::of)
                    .toList());
    }

    @Override
    public boolean
    addAll(Collection<? extends T> elements)
    {
        StopContext<T>[] contexts =
            elements
                .stream()
                .map(StopContext::of)
                .toArray(StopContext[]::new);

        return implementation.addAll(Arrays.asList(contexts));
    }

    @Override
    public boolean
    removeAll(Collection<?> elements)
    {
        return
            implementation.removeAll(
                elements
                    .stream()
                    .map(StopContext::of)
                    .toList());
    }

    @Override
    public boolean
    retainAll(Collection<?> elements)
    {
        return
            implementation.retainAll(
                elements
                    .stream()
                    .map(StopContext::of)
                    .toList());
    }

    @Override
    public void
    clear()
    {
        implementation.clear();
    }

    @Override
    public int
    size()
    {
        return implementation.size();
    }

    @Override
    public boolean
    isEmpty()
    {
        return implementation.isEmpty();
    }

    @Override
    public boolean
    contains(Object element)
    {
        return implementation.contains(StopContext.of(element));
    }

    @Override
    public Iterator<T>
    iterator()
    {
        return null;
    }

    @Override
    public Object[]
    toArray()
    {
        return
            implementation
                .stream()
                .map(StopContext::getElement)
                .toArray();
    }

    @Override
    public <T1> T1[]
    toArray(T1[] a)
    {
        return
            implementation
                .stream()
                .map(StopContext::getElement)
                .toArray(
                    size ->
                        Arrays.copyOf(
                            a,
                            size,
                            (Class<? extends T1[]>) a.getClass()));
    }

    @Override
    public int
    drainTo(Collection<? super T> sink)
        throws PartiallyDrainedException
    {
        List<StopContext<T>> temp = new ArrayList<>(implementation.size());
        int                  transferred = implementation.drainTo(temp);
        List<T>              drained = new ArrayList<>();

        temp
            .stream()
            .map(context -> checkStopped(context,drained))
            .forEach(context -> sink.add(getElement(context)));

        return transferred;
    }

    @Override
    public int
    drainTo(Collection<? super T> sink,int maxElements)
        throws PartiallyDrainedException
    {
        List<StopContext<T>> temp = new ArrayList<>(implementation.size());
        int                  transferred = implementation.drainTo(temp,maxElements);
        List<T>              drained = new ArrayList<>();

        temp
            .stream()
            .map(context -> checkStopped(context,drained))
            .forEach(context -> sink.add(getElement(context)));

        return transferred;
    }

    @Override
    public IBlockingQueue<T>
    start()
    {
        stopped.set(false);
        return this;
    }

    @Override
    public StoppableBlockingQueue<T>
    stop()
        throws InterruptedException
    {
        implementation.put(StopContext.stop());
        return this;
    }

    @Override
    public boolean
    isStarted()
    {
        return !isStopped();
    }

    @Override
    public boolean
    isStopped()
    {
        return stopped.get();
    }

    protected T
    getElement(StopContext<T> context)
    {
        return context != null ? context.getElement() : null;
    }

    protected boolean
    isStopped(StopContext<T> context)
    {
        stopped.set(context != null && context.mustStop());
        return stopped.get();
    }

    protected StopContext<T>
    checkStopped(StopContext<T> context,List<T> drained)
        throws PartiallyDrainedException
    {
        if (isStopped(context))
            throw new PartiallyDrainedException(drained);

        drained.add(getElement(context));
        return context;
    }
}

//////////////////////////////////////////////////////////////////////////////

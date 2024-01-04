//////////////////////////////////////////////////////////////////////////////
// BlockingBufferBasedCompletableFuture.java
//////////////////////////////////////////////////////////////////////////////

package strata.foundation.core.concurrent;

import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.Executor;
import java.util.function.*;

public
class BlockingBufferBasedCompletableFuture<T>
    implements CompletionStage<T>
{
    private IBlockingBuffer<T>   buffer;
    private CompletableFuture<T> future;

    public
    BlockingBufferBasedCompletableFuture(IBlockingBuffer<T> source)
    {
        Objects.requireNonNull(source);
        buffer = source;
        future = null;
    }

    public
    BlockingBufferBasedCompletableFuture(CompletableFuture<T> f)
    {
        Objects.requireNonNull(f);
        buffer = null;
        future = f;
    }

    @Override
    public <U> CompletionStage<U>
    thenApply(Function<? super T,? extends U> fn)
    {
        return getFuture().thenApply(fn);
    }

    @Override
    public <U> CompletionStage<U>
    thenApplyAsync(Function<? super T,? extends U> fn)
    {
        return getFuture().thenApplyAsync(fn);
    }

    @Override
    public <U> CompletionStage<U>
    thenApplyAsync(Function<? super T,? extends U> fn,Executor executor)
    {
        return getFuture().thenApplyAsync(fn,executor);
    }

    @Override
    public CompletionStage<Void>
    thenAccept(Consumer<? super T> action)
    {
        return getFuture().thenAccept(action);
    }

    @Override
    public CompletionStage<Void>
    thenAcceptAsync(Consumer<? super T> action)
    {
        return getFuture().thenAcceptAsync(action);
    }

    @Override
    public CompletionStage<Void>
    thenAcceptAsync(Consumer<? super T> action,Executor executor)
    {
        return getFuture().thenAcceptAsync(action,executor);
    }

    @Override
    public CompletionStage<Void>
    thenRun(Runnable action)
    {
        return getFuture().thenRun(action);
    }

    @Override
    public CompletionStage<Void>
    thenRunAsync(Runnable action)
    {
        return getFuture().thenRunAsync(action);
    }

    @Override
    public CompletionStage<Void>
    thenRunAsync(Runnable action,Executor executor)
    {
        return getFuture().thenRunAsync(action,executor);
    }

    @Override
    public <U,V> CompletionStage<V>
    thenCombine(CompletionStage<? extends U> other,BiFunction<? super T,? super U,? extends V> fn)
    {
        return getFuture().thenCombine(other,fn);
    }

    @Override
    public <U,V> CompletionStage<V>
    thenCombineAsync(CompletionStage<? extends U> other,BiFunction<? super T,? super U,? extends V> fn)
    {
        return getFuture().thenCombineAsync(other,fn);
    }

    @Override
    public <U,V> CompletionStage<V>
    thenCombineAsync(CompletionStage<? extends U> other,BiFunction<? super T,? super U,? extends V> fn,Executor executor)
    {
        return getFuture().thenCombineAsync(other,fn,executor);
    }

    @Override
    public <U> CompletionStage<Void>
    thenAcceptBoth(CompletionStage<? extends U> other,BiConsumer<? super T,? super U> action)
    {
        return getFuture().thenAcceptBoth(other,action);
    }

    @Override
    public <U> CompletionStage<Void>
    thenAcceptBothAsync(CompletionStage<? extends U> other,BiConsumer<? super T,? super U> action)
    {
        return getFuture().thenAcceptBothAsync(other,action);
    }

    @Override
    public <U> CompletionStage<Void>
    thenAcceptBothAsync(CompletionStage<? extends U> other,BiConsumer<? super T,? super U> action,Executor executor)
    {
        return getFuture().thenAcceptBothAsync(other,action,executor);
    }

    @Override
    public CompletionStage<Void>
    runAfterBoth(CompletionStage<?> other,Runnable action)
    {
        return getFuture().runAfterBoth(other,action);
    }

    @Override
    public CompletionStage<Void>
    runAfterBothAsync(CompletionStage<?> other,Runnable action)
    {
        return getFuture().runAfterBothAsync(other,action);
    }

    @Override
    public CompletionStage<Void>
    runAfterBothAsync(CompletionStage<?> other,Runnable action,Executor executor)
    {
        return getFuture().runAfterBothAsync(other,action,executor);
    }

    @Override
    public <U> CompletionStage<U>
    applyToEither(CompletionStage<? extends T> other,Function<? super T,U> fn)
    {
        return getFuture().applyToEither(other,fn);
    }

    @Override
    public <U> CompletionStage<U>
    applyToEitherAsync(CompletionStage<? extends T> other,Function<? super T,U> fn)
    {
        return getFuture().applyToEitherAsync(other,fn);
    }

    @Override
    public <U> CompletionStage<U>
    applyToEitherAsync(CompletionStage<? extends T> other,Function<? super T,U> fn,Executor executor)
    {
        return getFuture().applyToEitherAsync(other,fn,executor);
    }

    @Override
    public CompletionStage<Void>
    acceptEither(CompletionStage<? extends T> other,Consumer<? super T> action)
    {
        return getFuture().acceptEither(other,action);
    }

    @Override
    public CompletionStage<Void>
    acceptEitherAsync(CompletionStage<? extends T> other,Consumer<? super T> action)
    {
        return getFuture().acceptEitherAsync(other,action);
    }

    @Override
    public CompletionStage<Void>
    acceptEitherAsync(CompletionStage<? extends T> other,Consumer<? super T> action,Executor executor)
    {
        return getFuture().acceptEitherAsync(other,action,executor);
    }

    @Override
    public CompletionStage<Void>
    runAfterEither(CompletionStage<?> other,Runnable action)
    {
        return getFuture().runAfterEither(other,action);
    }

    @Override
    public CompletionStage<Void>
    runAfterEitherAsync(CompletionStage<?> other,Runnable action)
    {
        return getFuture().runAfterEitherAsync(other,action);
    }

    @Override
    public CompletionStage<Void>
    runAfterEitherAsync(CompletionStage<?> other,Runnable action,Executor executor)
    {
        return getFuture().runAfterEitherAsync(other,action,executor);
    }

    @Override
    public <U> CompletionStage<U>
    thenCompose(Function<? super T,? extends CompletionStage<U>> fn)
    {
        return getFuture().thenCompose(fn);
    }

    @Override
    public <U> CompletionStage<U>
    thenComposeAsync(Function<? super T,? extends CompletionStage<U>> fn)
    {
        return getFuture().thenComposeAsync(fn);
    }

    @Override
    public <U> CompletionStage<U>
    thenComposeAsync(Function<? super T,? extends CompletionStage<U>> fn,Executor executor)
    {
        return getFuture().thenComposeAsync(fn,executor);
    }

    @Override
    public <U> CompletionStage<U>
    handle(BiFunction<? super T,Throwable,? extends U> fn)
    {
        return getFuture().handle(fn);
    }

    @Override
    public <U> CompletionStage<U>
    handleAsync(BiFunction<? super T,Throwable,? extends U> fn)
    {
        return getFuture().handleAsync(fn);
    }

    @Override
    public <U> CompletionStage<U>
    handleAsync(BiFunction<? super T,Throwable,? extends U> fn,Executor executor)
    {
        return getFuture().handleAsync(fn,executor);
    }

    @Override
    public CompletionStage<T>
    whenComplete(BiConsumer<? super T,? super Throwable> action)
    {
        return getFuture().whenComplete(action);
    }

    @Override
    public CompletionStage<T>
    whenCompleteAsync(BiConsumer<? super T,? super Throwable> action)
    {
        return getFuture().whenCompleteAsync(action);
    }

    @Override
    public CompletionStage<T>
    whenCompleteAsync(BiConsumer<? super T,? super Throwable> action,Executor executor)
    {
        return getFuture().whenCompleteAsync(action,executor);
    }

    @Override
    public CompletionStage<T>
    exceptionally(Function<Throwable,? extends T> fn)
    {
        return getFuture().exceptionally(fn);
    }

    @Override
    public synchronized CompletableFuture<T>
    toCompletableFuture()
    {

        return getFuture();
    }

    public static <T> BlockingBufferBasedCompletableFuture<T>
    supplyAsync(Supplier<T> supplier)
    {
        return new BlockingBufferBasedCompletableFuture<>(CompletableFuture.supplyAsync(supplier));
    }

    protected CompletableFuture<T>
    getFuture()
    {
        try
        {
            if (future ==  null )
             future = CompletableFuture.completedFuture(buffer.get());

            return future;
        }
        catch (Exception e)
        {
            throw new RuntimeException(e);
        }
    }
}

//////////////////////////////////////////////////////////////////////////////

//////////////////////////////////////////////////////////////////////////////
// BlockingBufferBasedCompletableSendResult.java
//////////////////////////////////////////////////////////////////////////////

package strata.foundation.core.event;

import strata.foundation.core.concurrent.IBlockingBuffer;

import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.Executor;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;

public
class BlockingBufferBasedCompletableSendResult<E>
    implements ICompletableSendResult<E>
{
    private final IBlockingBuffer<ICompletableSendResult<E>> buffer;

    public
    BlockingBufferBasedCompletableSendResult(IBlockingBuffer<ICompletableSendResult<E>> source)
    {
        Objects.requireNonNull(source);
        buffer = source;
    }

    @Override
    public <U> CompletionStage<U>
    thenApply(Function<? super SendResult<E>,? extends U> fn)
    {
        return getResult().thenApply(fn);
    }

    @Override
    public <U> CompletionStage<U>
    thenApplyAsync(Function<? super SendResult<E>,? extends U> fn)
    {
        return getResult().thenApplyAsync(fn);
    }

    @Override
    public <U> CompletionStage<U>
    thenApplyAsync(Function<? super SendResult<E>,? extends U> fn,Executor executor)
    {
        return getResult().thenApplyAsync(fn,executor);
    }

    @Override
    public CompletionStage<Void>
    thenAccept(Consumer<? super SendResult<E>> action)
    {
        return getResult().thenAccept(action);
    }

    @Override
    public CompletionStage<Void>
    thenAcceptAsync(Consumer<? super SendResult<E>> action)
    {
        return getResult().thenAcceptAsync(action);
    }

    @Override
    public CompletionStage<Void>
    thenAcceptAsync(Consumer<? super SendResult<E>> action,Executor executor)
    {
        return getResult().thenAcceptAsync(action,executor);
    }

    @Override
    public CompletionStage<Void>
    thenRun(Runnable action)
    {
        return getResult().thenRun(action);
    }

    @Override
    public CompletionStage<Void>
    thenRunAsync(Runnable action)
    {
        return getResult().thenRunAsync(action);
    }

    @Override
    public CompletionStage<Void>
    thenRunAsync(Runnable action,Executor executor)
    {
        return getResult().thenRunAsync(action,executor);
    }

    @Override
    public <U,V> CompletionStage<V>
    thenCombine(CompletionStage<? extends U> other,BiFunction<? super SendResult<E>,? super U,? extends V> fn)
    {
        return getResult().thenCombine(other,fn);
    }

    @Override
    public <U,V> CompletionStage<V>
    thenCombineAsync(CompletionStage<? extends U> other,BiFunction<? super SendResult<E>,? super U,? extends V> fn)
    {
        return getResult().thenCombineAsync(other,fn);
    }

    @Override
    public <U,V> CompletionStage<V>
    thenCombineAsync(CompletionStage<? extends U> other,BiFunction<? super SendResult<E>,? super U,? extends V> fn,Executor executor)
    {
        return getResult().thenCombineAsync(other,fn,executor);
    }

    @Override
    public <U> CompletionStage<Void>
    thenAcceptBoth(CompletionStage<? extends U> other,BiConsumer<? super SendResult<E>,? super U> action)
    {
        return getResult().thenAcceptBoth(other,action);
    }

    @Override
    public <U> CompletionStage<Void>
    thenAcceptBothAsync(CompletionStage<? extends U> other,BiConsumer<? super SendResult<E>,? super U> action)
    {
        return getResult().thenAcceptBothAsync(other,action);
    }

    @Override
    public <U> CompletionStage<Void>
    thenAcceptBothAsync(CompletionStage<? extends U> other,BiConsumer<? super SendResult<E>,? super U> action,Executor executor)
    {
        return getResult().thenAcceptBothAsync(other,action,executor);
    }

    @Override
    public CompletionStage<Void>
    runAfterBoth(CompletionStage<?> other,Runnable action)
    {
        return getResult().runAfterBoth(other,action);
    }

    @Override
    public CompletionStage<Void>
    runAfterBothAsync(CompletionStage<?> other,Runnable action)
    {
        return getResult().runAfterBothAsync(other,action);
    }

    @Override
    public CompletionStage<Void>
    runAfterBothAsync(CompletionStage<?> other,Runnable action,Executor executor)
    {
        return getResult().runAfterBothAsync(other,action,executor);
    }

    @Override
    public <U> CompletionStage<U>
    applyToEither(CompletionStage<? extends SendResult<E>> other,Function<? super SendResult<E>,U> fn)
    {
        return getResult().applyToEither(other,fn);
    }

    @Override
    public <U> CompletionStage<U>
    applyToEitherAsync(CompletionStage<? extends SendResult<E>> other,Function<? super SendResult<E>,U> fn)
    {
        return getResult().applyToEitherAsync(other,fn);
    }

    @Override
    public <U> CompletionStage<U>
    applyToEitherAsync(CompletionStage<? extends SendResult<E>> other,Function<? super SendResult<E>,U> fn,Executor executor)
    {
        return getResult().applyToEitherAsync(other,fn,executor);
    }

    @Override
    public CompletionStage<Void>
    acceptEither(CompletionStage<? extends SendResult<E>> other,Consumer<? super SendResult<E>> action)
    {
        return getResult().acceptEither(other,action);
    }

    @Override
    public CompletionStage<Void>
    acceptEitherAsync(CompletionStage<? extends SendResult<E>> other,Consumer<? super SendResult<E>> action)
    {
        return getResult().acceptEitherAsync(other,action);
    }

    @Override
    public CompletionStage<Void>
    acceptEitherAsync(CompletionStage<? extends SendResult<E>> other,Consumer<? super SendResult<E>> action,Executor executor)
    {
        return getResult().acceptEitherAsync(other,action,executor);
    }

    @Override
    public CompletionStage<Void>
    runAfterEither(CompletionStage<?> other,Runnable action)
    {
        return getResult().runAfterEither(other,action);
    }

    @Override
    public CompletionStage<Void>
    runAfterEitherAsync(CompletionStage<?> other,Runnable action)
    {
        return getResult().runAfterEitherAsync(other,action);
    }

    @Override
    public CompletionStage<Void>
    runAfterEitherAsync(CompletionStage<?> other,Runnable action,Executor executor)
    {
        return getResult().runAfterEitherAsync(other,action,executor);
    }

    @Override
    public <U> CompletionStage<U>
    thenCompose(Function<? super SendResult<E>,? extends CompletionStage<U>> fn)
    {
        return getResult().thenCompose(fn);
    }

    @Override
    public <U> CompletionStage<U>
    thenComposeAsync(Function<? super SendResult<E>,? extends CompletionStage<U>> fn)
    {
        return getResult().thenComposeAsync(fn);
    }

    @Override
    public <U> CompletionStage<U>
    thenComposeAsync(Function<? super SendResult<E>,? extends CompletionStage<U>> fn,Executor executor)
    {
        return getResult().thenComposeAsync(fn,executor);
    }

    @Override
    public <U> CompletionStage<U>
    handle(BiFunction<? super SendResult<E>,Throwable,? extends U> fn)
    {
        return getResult().handle(fn);
    }

    @Override
    public <U> CompletionStage<U>
    handleAsync(BiFunction<? super SendResult<E>,Throwable,? extends U> fn)
    {
        return getResult().handleAsync(fn);
    }

    @Override
    public <U> CompletionStage<U>
    handleAsync(BiFunction<? super SendResult<E>,Throwable,? extends U> fn,Executor executor)
    {
        return getResult().handleAsync(fn,executor);
    }

    @Override
    public CompletionStage<SendResult<E>>
    whenComplete(BiConsumer<? super SendResult<E>,? super Throwable> action)
    {
        return getResult().whenComplete(action);
    }

    @Override
    public CompletionStage<SendResult<E>>
    whenCompleteAsync(BiConsumer<? super SendResult<E>,? super Throwable> action)
    {
        return getResult().whenCompleteAsync(action);
    }

    @Override
    public CompletionStage<SendResult<E>>
    whenCompleteAsync(BiConsumer<? super SendResult<E>,? super Throwable> action,Executor executor)
    {
        return getResult().whenCompleteAsync(action,executor);
    }

    @Override
    public CompletionStage<SendResult<E>>
    exceptionally(Function<Throwable,? extends SendResult<E>> fn)
    {
        return getResult().exceptionally(fn);
    }

    @Override
    public CompletableFuture<SendResult<E>>
    toCompletableFuture()
    {
        return getResult().toCompletableFuture();
    }

    protected ICompletableSendResult<E>
    getResult() { return buffer.get(); }
}

//////////////////////////////////////////////////////////////////////////////

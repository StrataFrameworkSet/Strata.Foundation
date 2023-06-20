//////////////////////////////////////////////////////////////////////////////
// CompletableSendResult.java
//////////////////////////////////////////////////////////////////////////////

package strata.foundation.core.event;

import java.util.concurrent.*;
import java.util.function.*;

public
class CompletableSendResult<E>
    implements ICompletableSendResult<E>
{
    private final CompletableFuture<SendResult<E>> implementation;

    public
    CompletableSendResult(CompletableFuture<SendResult<E>> imp)
    {
        implementation = imp;
    }

    @Override
    public <U> CompletionStage<U>
    thenApply(Function<? super SendResult<E>,? extends U> fn)
    {
        return implementation.thenApply(fn);
    }

    @Override
    public <U> CompletionStage<U>
    thenApplyAsync(Function<? super SendResult<E>,? extends U> fn)
    {
        return implementation.thenApplyAsync(fn);
    }

    @Override
    public <U> CompletionStage<U>
    thenApplyAsync(Function<? super SendResult<E>,? extends U> fn,Executor executor)
    {
        return implementation.thenApplyAsync(fn,executor);
    }

    @Override
    public CompletionStage<Void>
    thenAccept(Consumer<? super SendResult<E>> action)
    {
        return implementation.thenAccept(action);
    }

    @Override
    public CompletionStage<Void>
    thenAcceptAsync(Consumer<? super SendResult<E>> action)
    {
        return implementation.thenAcceptAsync(action);
    }

    @Override
    public CompletionStage<Void>
    thenAcceptAsync(Consumer<? super SendResult<E>> action,Executor executor)
    {
        return implementation.thenAcceptAsync(action,executor);
    }

    @Override
    public CompletionStage<Void>
    thenRun(Runnable action)
    {
        return implementation.thenRun(action);
    }

    @Override
    public CompletionStage<Void>
    thenRunAsync(Runnable action)
    {
        return implementation.thenRunAsync(action);
    }

    @Override
    public CompletionStage<Void>
    thenRunAsync(Runnable action,Executor executor)
    {
        return implementation.thenRunAsync(action,executor);
    }

    @Override
    public <U,V> CompletionStage<V>
    thenCombine(CompletionStage<? extends U> other,BiFunction<? super SendResult<E>,? super U,? extends V> fn)
    {
        return implementation.thenCombine(other,fn);
    }

    @Override
    public <U,V> CompletionStage<V>
    thenCombineAsync(CompletionStage<? extends U> other,BiFunction<? super SendResult<E>,? super U,? extends V> fn)
    {
        return implementation.thenCombineAsync(other,fn);
    }

    @Override
    public <U,V> CompletionStage<V>
    thenCombineAsync(CompletionStage<? extends U> other,BiFunction<? super SendResult<E>,? super U,? extends V> fn,Executor executor)
    {
        return implementation.thenCombineAsync(other,fn,executor);
    }

    @Override
    public <U> CompletionStage<Void>
    thenAcceptBoth(CompletionStage<? extends U> other,BiConsumer<? super SendResult<E>,? super U> action)
    {
        return implementation.thenAcceptBoth(other,action);
    }

    @Override
    public <U> CompletionStage<Void>
    thenAcceptBothAsync(CompletionStage<? extends U> other,BiConsumer<? super SendResult<E>,? super U> action)
    {
        return implementation.thenAcceptBothAsync(other,action);
    }

    @Override
    public <U> CompletionStage<Void>
    thenAcceptBothAsync(CompletionStage<? extends U> other,BiConsumer<? super SendResult<E>,? super U> action,Executor executor)
    {
        return implementation.thenAcceptBothAsync(other,action,executor);
    }

    @Override
    public CompletionStage<Void>
    runAfterBoth(CompletionStage<?> other,Runnable action)
    {
        return implementation.runAfterBoth(other,action);
    }

    @Override
    public CompletionStage<Void>
    runAfterBothAsync(CompletionStage<?> other,Runnable action)
    {
        return implementation.runAfterBothAsync(other,action);
    }

    @Override
    public CompletionStage<Void>
    runAfterBothAsync(CompletionStage<?> other,Runnable action,Executor executor)
    {
        return implementation.runAfterBothAsync(other,action,executor);
    }

    @Override
    public <U> CompletionStage<U>
    applyToEither(CompletionStage<? extends SendResult<E>> other,Function<? super SendResult<E>,U> fn)
    {
        return implementation.applyToEither(other,fn);
    }

    @Override
    public <U> CompletionStage<U>
    applyToEitherAsync(CompletionStage<? extends SendResult<E>> other,Function<? super SendResult<E>,U> fn)
    {
        return implementation.applyToEitherAsync(other,fn);
    }

    @Override
    public <U> CompletionStage<U>
    applyToEitherAsync(CompletionStage<? extends SendResult<E>> other,Function<? super SendResult<E>,U> fn,Executor executor)
    {
        return implementation.applyToEitherAsync(other,fn,executor);
    }

    @Override
    public CompletionStage<Void>
    acceptEither(CompletionStage<? extends SendResult<E>> other,Consumer<? super SendResult<E>> action)
    {
        return implementation.acceptEither(other,action);
    }

    @Override
    public CompletionStage<Void>
    acceptEitherAsync(CompletionStage<? extends SendResult<E>> other,Consumer<? super SendResult<E>> action)
    {
        return implementation.acceptEitherAsync(other,action);
    }

    @Override
    public CompletionStage<Void>
    acceptEitherAsync(CompletionStage<? extends SendResult<E>> other,Consumer<? super SendResult<E>> action,Executor executor)
    {
        return implementation.acceptEitherAsync(other,action,executor);
    }

    @Override
    public CompletionStage<Void>
    runAfterEither(CompletionStage<?> other,Runnable action)
    {
        return implementation.runAfterEither(other,action);
    }

    @Override
    public CompletionStage<Void>
    runAfterEitherAsync(CompletionStage<?> other,Runnable action)
    {
        return implementation.runAfterEitherAsync(other,action);
    }

    @Override
    public CompletionStage<Void>
    runAfterEitherAsync(CompletionStage<?> other,Runnable action,Executor executor)
    {
        return implementation.runAfterEitherAsync(other,action,executor);
    }

    @Override
    public <U> CompletionStage<U>
    thenCompose(Function<? super SendResult<E>,? extends CompletionStage<U>> fn)
    {
        return implementation.thenCompose(fn);
    }

    @Override
    public <U> CompletionStage<U>
    thenComposeAsync(Function<? super SendResult<E>,? extends CompletionStage<U>> fn)
    {
        return implementation.thenComposeAsync(fn);
    }

    @Override
    public <U> CompletionStage<U>
    thenComposeAsync(Function<? super SendResult<E>,? extends CompletionStage<U>> fn,Executor executor)
    {
        return implementation.thenComposeAsync(fn,executor);
    }

    @Override
    public <U> CompletionStage<U>
    handle(BiFunction<? super SendResult<E>,Throwable,? extends U> fn)
    {
        return implementation.handle(fn);
    }

    @Override
    public <U> CompletionStage<U>
    handleAsync(BiFunction<? super SendResult<E>,Throwable,? extends U> fn)
    {
        return implementation.handleAsync(fn);
    }

    @Override
    public <U> CompletionStage<U>
    handleAsync(BiFunction<? super SendResult<E>,Throwable,? extends U> fn,Executor executor)
    {
        return implementation.handleAsync(fn,executor);
    }

    @Override
    public CompletableSendResult<E>
    whenComplete(BiConsumer<? super SendResult<E>,? super Throwable> action)
    {
        return
            new CompletableSendResult<>(
                implementation.whenComplete(action));
    }

    @Override
    public CompletableSendResult<E>
    whenCompleteAsync(BiConsumer<? super SendResult<E>,? super Throwable> action)
    {
        return
            new CompletableSendResult<>(
                implementation.whenCompleteAsync(action));
    }

    @Override
    public CompletableSendResult<E>
    whenCompleteAsync(BiConsumer<? super SendResult<E>,? super Throwable> action,Executor executor)
    {
        return
            new CompletableSendResult<>(
                implementation.whenCompleteAsync(action,executor));
    }

    @Override
    public CompletableSendResult<E>
    exceptionally(Function<Throwable,? extends SendResult<E>> fn)
    {
        return
            new CompletableSendResult<>(
                implementation.exceptionally(fn));
    }

    @Override
    public CompletableFuture<SendResult<E>>
    toCompletableFuture()
    {
        return implementation;
    }

    public static <E> CompletableSendResult<E>
    supplyAsync(Supplier<SendResult<E>> supplier)
    {
        return new CompletableSendResult<>(CompletableFuture.supplyAsync(supplier));
    }

    public static <E> CompletableSendResult<E>
    supplyAsync(Supplier<SendResult<E>> supplier,Executor executor)
    {
        return
            new CompletableSendResult<>(
                CompletableFuture.supplyAsync(supplier,executor));
    }

    public static <E> CompletableSendResult<E>
    completedWith(SendResult<E> value)
    {
        return
            new CompletableSendResult<>(
                CompletableFuture.completedFuture(value));
    }


    public static <E> CompletableSendResult<E>
    completedWith(Throwable exception)
    {
        return
            new CompletableSendResult<>(
                CompletableFuture.failedFuture(exception));
    }

}

//////////////////////////////////////////////////////////////////////////////

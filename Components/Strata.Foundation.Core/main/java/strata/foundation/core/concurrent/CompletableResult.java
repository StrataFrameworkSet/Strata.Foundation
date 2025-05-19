/// ///////////////////////////////////////////////////////////////////////////
// CompletableResult.java
/// ///////////////////////////////////////////////////////////////////////////

package strata.foundation.core.concurrent;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.Executor;
import java.util.function.*;

public
class CompletableResult<T,R extends CompletedResult<T>>
    implements ICompletableResult<T,R>
{
    private final CompletableFuture<R> future;

    public
    CompletableResult(CompletableFuture<R> future)
    {
        this.future = future;
    }

    @Override
    public <U> CompletionStage<U>
    thenApply(Function<? super R,? extends U> fn)
    {
        return future.thenApply(fn);
    }

    @Override
    public <U> CompletionStage<U>
    thenApplyAsync(Function<? super R,? extends U> fn)
    {
        return future.thenApplyAsync(fn);
    }

    @Override
    public <U> CompletionStage<U>
    thenApplyAsync(
        Function<? super R,? extends U> fn,
        Executor executor)
    {
        return future.thenApplyAsync(fn,executor);
    }

    @Override
    public CompletionStage<Void>
    thenAccept(Consumer<? super R> action)
    {
        return future.thenAccept(action);
    }

    @Override
    public CompletionStage<Void>
    thenAcceptAsync(Consumer<? super R> action)
    {
        return future.thenAcceptAsync(action);
    }

    @Override
    public CompletionStage<Void>
    thenAcceptAsync(
        Consumer<? super R> action,
        Executor executor)
    {
        return future.thenAcceptAsync(action,executor);
    }

    @Override
    public CompletionStage<Void>
    thenRun(Runnable action)
    {
        return future.thenRun(action);
    }

    @Override
    public CompletionStage<Void>
    thenRunAsync(Runnable action)
    {
        return future.thenRunAsync(action);
    }

    @Override
    public CompletionStage<Void>
    thenRunAsync(Runnable action,Executor executor)
    {
        return future.thenRunAsync(action,executor);
    }

    @Override
    public <U,V> CompletionStage<V>
    thenCombine(
        CompletionStage<? extends U> other,
        BiFunction<? super R,? super U,? extends V> fn)
    {
        return future.thenCombine(other,fn);
    }

    @Override
    public <U,V> CompletionStage<V>
    thenCombineAsync(
        CompletionStage<? extends U> other,
        BiFunction<? super R,? super U,? extends V> fn)
    {
        return future.thenCombineAsync(other,fn);
    }

    @Override
    public <U,V> CompletionStage<V>
    thenCombineAsync(
        CompletionStage<? extends U> other,
        BiFunction<? super R,? super U,? extends V> fn,
        Executor executor)
    {
        return future.thenCombineAsync(other,fn,executor);
    }

    @Override
    public <U> CompletionStage<Void>
    thenAcceptBoth(
        CompletionStage<? extends U> other,
        BiConsumer<? super R,? super U> action)
    {
        return future.thenAcceptBoth(other,action);
    }

    @Override
    public <U> CompletionStage<Void>
    thenAcceptBothAsync(
        CompletionStage<? extends U> other,
        BiConsumer<? super R,? super U> action)
    {
        return future.thenAcceptBothAsync(other,action);
    }

    @Override
    public <U> CompletionStage<Void>
    thenAcceptBothAsync(
        CompletionStage<? extends U> other,
        BiConsumer<? super R,? super U> action,
        Executor executor)
    {
        return future.thenAcceptBothAsync(other,action,executor);
    }

    @Override
    public CompletionStage<Void>
    runAfterBoth(CompletionStage<?> other,Runnable action)
    {
        return future.runAfterBoth(other,action);
    }

    @Override
    public CompletionStage<Void>
    runAfterBothAsync(CompletionStage<?> other,Runnable action)
    {
        return future.runAfterBothAsync(other,action);
    }

    @Override
    public CompletionStage<Void>
    runAfterBothAsync(
        CompletionStage<?> other,
        Runnable action,
        Executor executor)
    {
        return future.runAfterBothAsync(other,action,executor);
    }

    @Override
    public <U> CompletionStage<U>
    applyToEither(
        CompletionStage<? extends R> other,
        Function<? super R,U> fn)
    {
        return future.applyToEither(other,fn);
    }

    @Override
    public <U> CompletionStage<U>
    applyToEitherAsync(
        CompletionStage<? extends R> other,
        Function<? super R,U> fn)
    {
        return future.applyToEitherAsync(other,fn);
    }

    @Override
    public <U> CompletionStage<U>
    applyToEitherAsync(
        CompletionStage<? extends R> other,
        Function<? super R,U> fn,
        Executor executor)
    {
        return future.applyToEitherAsync(other,fn,executor);
    }

    @Override
    public CompletionStage<Void>
    acceptEither(
        CompletionStage<? extends R> other,
        Consumer<? super R> action)
    {
        return future.acceptEither(other,action);
    }

    @Override
    public CompletionStage<Void>
    acceptEitherAsync(
        CompletionStage<? extends R> other,
        Consumer<? super R> action)
    {
        return future.acceptEitherAsync(other,action);
    }

    @Override
    public CompletionStage<Void>
    acceptEitherAsync(
        CompletionStage<? extends R> other,
        Consumer<? super R> action,
        Executor executor)
    {
        return future.acceptEitherAsync(other,action,executor);
    }

    @Override
    public CompletionStage<Void>
    runAfterEither(CompletionStage<?> other,Runnable action)
    {
        return future.runAfterEither(other,action);
    }

    @Override
    public CompletionStage<Void>
    runAfterEitherAsync(CompletionStage<?> other,Runnable action)
    {
        return future.runAfterEitherAsync(other,action);
    }

    @Override
    public CompletionStage<Void>
    runAfterEitherAsync(
        CompletionStage<?> other,Runnable action,
        Executor executor)
    {
        return future.runAfterEitherAsync(other,action,executor);
    }

    @Override
    public <U> CompletionStage<U>
    thenCompose(Function<? super R,? extends CompletionStage<U>> fn)
    {
        return future.thenCompose(fn);
    }

    @Override
    public <U> CompletionStage<U>
    thenComposeAsync(
        Function<? super R,? extends CompletionStage<U>> fn)
    {
        return future.thenComposeAsync(fn);
    }

    @Override
    public <U> CompletionStage<U>
    thenComposeAsync(
        Function<? super R,? extends CompletionStage<U>> fn,
        Executor executor)
    {
        return future.thenComposeAsync(fn,executor);
    }

    @Override
    public <U> CompletionStage<U>
    handle(BiFunction<? super R,Throwable,? extends U> fn)
    {
        return future.handle(fn);
    }

    @Override
    public <U> CompletionStage<U>
    handleAsync(BiFunction<? super R,Throwable,? extends U> fn)
    {
        return future.handleAsync(fn);
    }

    @Override
    public <U> CompletionStage<U>
    handleAsync(
        BiFunction<? super R,Throwable,? extends U> fn,
        Executor executor)
    {
        return future.handleAsync(fn,executor);
    }

    @Override
    public ICompletableResult<T,R>
    whenComplete(
        BiConsumer<? super R,? super Throwable> action)
    {
        return new CompletableResult<>(future.whenComplete(action));
    }

    @Override
    public ICompletableResult<T,R>
    whenCompleteAsync(
        BiConsumer<? super R,? super Throwable> action)
    {
        return new CompletableResult<>(future.whenCompleteAsync(action));
    }

    @Override
    public ICompletableResult<T,R>
    whenCompleteAsync(
        BiConsumer<? super R,? super Throwable> action,
        Executor executor)
    {
        return
            new CompletableResult<>(
                future.whenCompleteAsync(action,executor));
    }

    @Override
    public ICompletableResult<T,R>
    exceptionally(Function<Throwable,? extends R> fn)
    {
        return new CompletableResult<>(future.exceptionally(fn));
    }

    @Override
    public CompletableFuture<R>
    toCompletableFuture()
    {
        return future;
    }

    @Override
    public R
    join()
    {
        return toCompletableFuture().join();
    }

    public static <
        T,
        R extends CompletedResult<T>,
        C extends ICompletableResult<T,R>> C
    supplyAsync(
        ICompletableResultFactory<T,R,C> factory,
        Supplier<R>                      supplier)
    {
        return factory.apply(CompletableFuture.supplyAsync(supplier));
    }

    public static <
        T,
        R extends CompletedResult<T>,
        C extends ICompletableResult<T,R>> C
    supplyAsync(
        ICompletableResultFactory<T,R,C> factory,
        Supplier<R>                      supplier,
        Executor                         executor)
    {
        return
            factory.apply(
                CompletableFuture.supplyAsync(supplier,executor));
    }

}

//////////////////////////////////////////////////////////////////////////////

//////////////////////////////////////////////////////////////////////////////
// PartitionedChronicleEventSender.java
//////////////////////////////////////////////////////////////////////////////

package strata.foundation.chronicle.event;

import strata.foundation.core.event.CompletableSendResult;
import strata.foundation.core.event.ICompletableSendResult;
import strata.foundation.core.event.IEventSender;
import strata.foundation.core.event.SendResult;
import strata.foundation.core.utility.OptionalExtension;

import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

public
class MulticastedChronicleEventSender<E,S extends IEventSender<E>>
    extends CompositeChronicleEventSender<E,S>
{
    public
    MulticastedChronicleEventSender(Set<S> senders)
    {
        super(senders);
    }

    @Override
    public ICompletableSendResult<E>
    send(E event)
    {
        return
            OptionalExtension
                .ifPresentOrElse(
                    mapSenders(sender -> sender.send(event))
                        .reduce(this::combine),
                    result -> result,
                    () ->
                        CompletableSendResult.completedWith(
                            new SendResult<>(
                                new IllegalStateException(
                                    "Missing send result"))));
    }

    private ICompletableSendResult<E>
    combine(ICompletableSendResult<E> left,ICompletableSendResult<E> right)
    {
        return
            new CompletableSendResult<>(
                left
                    .thenCombine(
                        right,
                        (r1,r2) -> combineSendResults(r1,r2))
                    .toCompletableFuture());
    }

    private SendResult<E>
    combineSendResults(SendResult<E> r1,SendResult<E> r2)
    {
        if (r1.isSuccess() && r2.isSuccess())
            return r1;

        if (!r1.isSuccess() && r2.isSuccess())
            return r1;

        if (r1.isSuccess() && !r2.isSuccess())
            return r2;

        return
            new SendResult<>(
                new CompositeException()
                    .setCause((Exception)r1.getException().get())
                    .setCause((Exception)r2.getException().get()));
    }
}

//////////////////////////////////////////////////////////////////////////////

import {ICompletableSendResult} from "./ICompletableSendResult";

export
interface IEventSender<E>
{
    open(): IEventSender<E>;

    close(): IEventSender<E>;

    send(event: E): ICompletableSendResult<E>;

    isOpen(): boolean;

    isClosed(): boolean;

}
import {Optional} from "../Utility";

export
class SendResult<E>
{
    private readonly sentEvent: Optional<E>;
    private readonly exception: Optional<Error>;

    constructor(event: E,exception: Error)
    {
        this.sentEvent = Optional.ofNullable(event);
        this.exception = Optional.ofNullable(exception);
    }

    isSuccess(): boolean { return this.sentEvent.isPresent(); }

    getSentEvent(): Optional<E> { return this.sentEvent; }

    getException(): Optional<Error> { return this.exception; }

    static
    ofEvent<E>(event: E): SendResult<E>
    {
        return new SendResult<E>(event,null);
    }

    static
    ofException<E>(exception: Error): SendResult<E>
    {
        return new SendResult<E>(null,exception);
    }
}
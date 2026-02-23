import {ICompletionStage} from "../Concurrent";
import {SendResult} from "./SendResult";

export
interface ICompletableSendResult<E>
    extends ICompletionStage<SendResult<E>> {}
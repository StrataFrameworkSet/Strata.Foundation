import {ExceptionData} from "./ExceptionData";

export
interface AbstractServiceReply
{
    replyId: string;
    originatingRequestId: string;
    timestamp: string;
    success: boolean;
    successMessage: string;
    failureMessage: string;
    exception: ExceptionData;

}
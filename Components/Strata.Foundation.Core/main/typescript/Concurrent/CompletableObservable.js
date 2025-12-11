"use strict";
Object.defineProperty(exports, "__esModule", { value: true });
exports.CompletableObservable = void 0;
var rxjs_1 = require("rxjs");
var operators_1 = require("rxjs/operators");
var CompletionContext_1 = require("./CompletionContext");
var CompletionError_1 = require("./CompletionError");
var CompletableObservable = /** @class */ (function () {
    function CompletableObservable(observable, hasContext) {
        if (hasContext === void 0) { hasContext = false; }
        if (hasContext)
            this.observable = observable;
        else
            this.observable = this.initializeObservable(observable);
    }
    CompletableObservable.prototype.thenApply = function (func) {
        var _this = this;
        return new CompletableObservable(this
            .observable
            .pipe((0, operators_1.map)(function (context) { return _this.doApply(context, func); })), true);
    };
    CompletableObservable.prototype.thenCompose = function (func) {
        var _this = this;
        return new CompletableObservable(this
            .observable
            .pipe(
        // @ts-ignore
        (0, operators_1.mergeMap)(function (context, i) {
            return _this.toObservable(_this.doCompose(func, context))
                .pipe((0, operators_1.map)(function (context) { return context; }));
        })), true);
    };
    CompletableObservable.prototype.thenAccept = function (consumer, handler) {
        var _this = this;
        return new CompletableObservable(this
            .observable
            .pipe((0, operators_1.map)(function (context) { return _this.doAccept(context, consumer, handler); })), true);
    };
    CompletableObservable.prototype.exceptionally = function (func) {
        var _this = this;
        return new CompletableObservable(this
            .observable
            .pipe((0, operators_1.map)(function (context) { return _this.doExceptionally(context, func); })), true);
    };
    CompletableObservable.prototype.subscribe = function (consumer, handler) {
        if (consumer == null)
            consumer = function (result) { };
        if (handler == null)
            handler = function (error) { };
        this
            .observable
            .subscribe(function (context) {
            if (context.hasError())
                handler(context.getError());
            else
                consumer(context.getResult());
        }, function (error) { return handler(error); });
    };
    CompletableObservable.fromResult = function (result) {
        return new CompletableObservable((0, rxjs_1.of)(result), false);
    };
    CompletableObservable.fromObservable = function (observable) {
        return new CompletableObservable(observable, false);
    };
    CompletableObservable.fromPromise = function (promise) {
        return new CompletableObservable((0, rxjs_1.from)(promise), false);
    };
    CompletableObservable.fromError = function (error) {
        return new CompletableObservable(new rxjs_1.Observable(function (observer) {
            return observer.next(CompletionContext_1.CompletionContext.ofError(new CompletionError_1.CompletionError(error)));
        }), true);
    };
    CompletableObservable.supplyAsync = function (supplier) {
        return new CompletableObservable(new rxjs_1.Observable(function (observer) { return observer.next(supplier()); }), false);
    };
    CompletableObservable.prototype.initializeObservable = function (input) {
        return input.pipe((0, operators_1.map)(function (value) { return CompletionContext_1.CompletionContext.ofResult(value); }));
    };
    CompletableObservable.prototype.doApply = function (context, func) {
        console.log("doApply");
        if (context.hasError())
            return CompletionContext_1.CompletionContext.ofError(context.getError());
        try {
            return CompletionContext_1.CompletionContext.ofResult(func(context.getResult()));
        }
        catch (e) {
            console.log("Caught exception in doApply");
            return CompletionContext_1.CompletionContext.ofError(new CompletionError_1.CompletionError(e));
        }
    };
    CompletableObservable.prototype.doCompose = function (func, context) {
        console.log("doCompose");
        if (context.hasError())
            return CompletionContext_1.CompletionContext.ofError(context.getError());
        try {
            return CompletionContext_1.CompletionContext.ofResult(func(context.getResult()));
        }
        catch (e) {
            console.log("Caught exception in doCompose");
            return CompletionContext_1.CompletionContext.ofError(e);
        }
    };
    CompletableObservable.prototype.doAccept = function (context, consumer, handler) {
        console.log("doAccept");
        if (context.hasError()) {
            if (handler != null) {
                try {
                    handler(context.getError());
                }
                catch (e) {
                    console.log("Caught exception in doAccept");
                    return context.setError(new CompletionError_1.CompletionError(e));
                }
            }
        }
        else {
            try {
                consumer(context.getResult());
            }
            catch (e) {
                return context.setError(new CompletionError_1.CompletionError(e));
            }
        }
        return context;
    };
    CompletableObservable.prototype.doExceptionally = function (context, func) {
        console.log("doExceptionally");
        if (context.hasError())
            return CompletionContext_1.CompletionContext.ofResult(func(context.getError()));
        return context;
    };
    CompletableObservable.prototype.toObservable = function (input) {
        if (input.hasResult())
            return input.getResult().observable;
        return this.observable.pipe((0, operators_1.map)(function (context, ignore) {
            return CompletionContext_1.CompletionContext.ofError(input.getError());
        }));
    };
    return CompletableObservable;
}());
exports.CompletableObservable = CompletableObservable;

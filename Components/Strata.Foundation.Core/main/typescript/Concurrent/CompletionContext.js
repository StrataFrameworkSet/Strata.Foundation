"use strict";
Object.defineProperty(exports, "__esModule", { value: true });
exports.CompletionContext = void 0;
var CompletionError_1 = require("./CompletionError");
var CompletionContext = /** @class */ (function () {
    function CompletionContext(result, error) {
        this.result = result;
        this.error = error;
    }
    CompletionContext.prototype.setResult = function (result) {
        this.result = result;
        return this;
    };
    CompletionContext.prototype.clearResult = function () {
        this.result = null;
        return this;
    };
    CompletionContext.prototype.setError = function (error) {
        this.error = error;
        return this;
    };
    CompletionContext.prototype.clearError = function () {
        this.error = null;
        return this;
    };
    CompletionContext.prototype.getResult = function () {
        return this.result;
    };
    CompletionContext.prototype.getError = function () {
        return this.error;
    };
    CompletionContext.prototype.hasResult = function () {
        return this.result != null;
    };
    CompletionContext.prototype.hasError = function () {
        return this.error != null;
    };
    CompletionContext.prototype.combine = function (other, combiner) {
        if (this.hasError() || other.hasError()) {
            var x = this.getError();
            var y = other.getError();
            if (x != null && y != null)
                return new CompletionContext(combiner(this.getResult(), other.getResult()), new CompletionError_1.CompletionError([x.getCause(), y.getCause()]));
            else if (x != null)
                return new CompletionContext(combiner(this.getResult(), other.getResult()), x);
            else
                return new CompletionContext(combiner(this.getResult(), other.getResult()), y);
        }
        else {
            return CompletionContext.ofResult(combiner(this.getResult(), other.getResult()));
        }
    };
    CompletionContext.prototype.accept = function (consumer) {
        consumer(this);
        return this;
    };
    CompletionContext.empty = function () {
        return new CompletionContext(null, null);
    };
    CompletionContext.ofResult = function (result) {
        return new CompletionContext(result, null);
    };
    CompletionContext.ofError = function (error) {
        return new CompletionContext(null, error);
    };
    return CompletionContext;
}());
exports.CompletionContext = CompletionContext;

"use strict";
var __extends = (this && this.__extends) || (function () {
    var extendStatics = function (d, b) {
        extendStatics = Object.setPrototypeOf ||
            ({ __proto__: [] } instanceof Array && function (d, b) { d.__proto__ = b; }) ||
            function (d, b) { for (var p in b) if (Object.prototype.hasOwnProperty.call(b, p)) d[p] = b[p]; };
        return extendStatics(d, b);
    };
    return function (d, b) {
        if (typeof b !== "function" && b !== null)
            throw new TypeError("Class extends value " + String(b) + " is not a constructor or null");
        extendStatics(d, b);
        function __() { this.constructor = d; }
        d.prototype = b === null ? Object.create(b) : (__.prototype = b.prototype, new __());
    };
})();
Object.defineProperty(exports, "__esModule", { value: true });
exports.CompletionError = void 0;
var CompletionError = /** @class */ (function (_super) {
    __extends(CompletionError, _super);
    function CompletionError(cause) {
        var _this = _super.call(this, cause instanceof Error ? cause.message : cause.toString()) || this;
        _this.cause = cause;
        return _this;
    }
    CompletionError.prototype.getCause = function () {
        return this.cause;
    };
    return CompletionError;
}(Error));
exports.CompletionError = CompletionError;

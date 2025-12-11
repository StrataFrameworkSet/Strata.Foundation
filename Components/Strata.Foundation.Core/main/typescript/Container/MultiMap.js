"use strict";
Object.defineProperty(exports, "__esModule", { value: true });
exports.MultiMap = void 0;
var MultiMap = /** @class */ (function () {
    function MultiMap() {
        this.imp = new Map();
    }
    MultiMap.prototype.put = function (key, value) {
        if (key == null)
            throw new Error("key is null");
        if (!this.imp.has(key))
            this.imp.set(key, new Array());
        this.imp.get(key).push(value);
        return this;
    };
    MultiMap.prototype.remove = function (key, value) {
        if (value) {
            if (this.imp.has(key)) {
                var values = this.imp.get(key);
                values.slice(values.indexOf(value), 1);
            }
        }
        else {
            if (this.imp.has(key))
                this.imp.delete(key);
        }
        return this;
    };
    MultiMap.prototype.get = function (key) {
        return this.imp.get(key);
    };
    MultiMap.prototype.getAt = function (key, index) {
        return this.imp.get(key)[index];
    };
    MultiMap.prototype.getCardinality = function (key) {
        return this.imp.get(key).length;
    };
    MultiMap.prototype.getKeys = function () {
        return Array.from(this.imp.keys());
    };
    MultiMap.prototype.getValues = function () {
        return Array.from(this.imp.values());
    };
    MultiMap.prototype.containsKey = function (key) {
        return this.imp.has(key);
    };
    MultiMap.prototype.containsValue = function (key, value) {
        if (this.containsKey(key))
            return this.get(key).includes(value);
        return false;
    };
    return MultiMap;
}());
exports.MultiMap = MultiMap;

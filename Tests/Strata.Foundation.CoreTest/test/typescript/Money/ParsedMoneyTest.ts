import {Money} from "strata.foundation.core/Money/Money";
import {ParsedMoney} from "strata.foundation.core/Money/ParsedMoney";

describe(
    "ParsedMoney to Money conversion",
    () => {
        it(
            "converts to Money with correct format",
            () => {
                const parsed = ParsedMoney.of("USD", "99.95");
                const money: Money = parsed.toMoney();

                expect(money.money).toBe("USD99.95");
            }
        );

        it(
            "converts integer amount to Money",
            () => {
                const parsed = ParsedMoney.of("JPY", "1000");
                const money: Money = parsed.toMoney();

                expect(money.money).toBe("JPY1000");
            }
        );

        it(
            "converts negative amount to Money",
            () => {
                const parsed = ParsedMoney.of("EUR", "-42.50");
                const money: Money = parsed.toMoney();

                expect(money.money).toBe("EUR-42.50");
            }
        );

        it(
            "converts zero to Money",
            () => {
                const parsed = ParsedMoney.of("CAD", "0");
                const money: Money = parsed.toMoney();

                expect(money.money).toBe("CAD0");
            }
        );
    }
);

describe(
    "Money to ParsedMoney conversion",
    () => {
        it(
            "parses currency from Money",
            () => {
                const money: Money = {money: "USD99.95"};
                const parsed = ParsedMoney.ofMoney(money);

                expect(parsed.getCurrency()).toBe("USD");
            }
        );

        it(
            "parses amount from Money",
            () => {
                const money: Money = {money: "EUR1234.56"};
                const parsed = ParsedMoney.ofMoney(money);

                expect(parsed.getNumber()).toBe("1234.56");
            }
        );

        it(
            "parses negative amount from Money",
            () => {
                const money: Money = {money: "GBP-42.50"};
                const parsed = ParsedMoney.ofMoney(money);

                expect(parsed.getNumber()).toBe("-42.50");
            }
        );

        it(
            "parses zero from Money",
            () => {
                const money: Money = {money: "CAD0"};
                const parsed = ParsedMoney.ofMoney(money);

                expect(parsed.getCurrency()).toBe("CAD");
                expect(parsed.getNumber()).toBe("0");
            }
        );

        it(
            "parses integer from Money",
            () => {
                const money: Money = {money: "JPY1000"};
                const parsed = ParsedMoney.ofMoney(money);

                expect(parsed.getCurrency()).toBe("JPY");
                expect(parsed.getNumber()).toBe("1000");
            }
        );
    }
);

describe(
    "ParsedMoney from string",
    () => {
        it(
            "parses currency from string",
            () => {
                const parsed = ParsedMoney.ofString("USD99.95");

                expect(parsed.getCurrency()).toBe("USD");
            }
        );

        it(
            "parses amount from string",
            () => {
                const parsed = ParsedMoney.ofString("EUR1234.56");

                expect(parsed.getNumber()).toBe("1234.56");
            }
        );

        it(
            "parses negative amount from string",
            () => {
                const parsed = ParsedMoney.ofString("GBP-42.50");

                expect(parsed.getNumber()).toBe("-42.50");
            }
        );
    }
);

describe(
    "ParsedMoney round-trip through Money",
    () => {
        it(
            "round-trips positive decimal",
            () => {
                const original = ParsedMoney.of("USD", "99.95");
                const money: Money = original.toMoney();
                const restored = ParsedMoney.ofMoney(money);

                expect(restored.getCurrency()).toBe(original.getCurrency());
                expect(restored.getNumber()).toBe(original.getNumber());
            }
        );

        it(
            "round-trips negative decimal",
            () => {
                const original = ParsedMoney.of("EUR", "-1234.56");
                const money: Money = original.toMoney();
                const restored = ParsedMoney.ofMoney(money);

                expect(restored.getCurrency()).toBe(original.getCurrency());
                expect(restored.getNumber()).toBe(original.getNumber());
            }
        );

        it(
            "round-trips zero",
            () => {
                const original = ParsedMoney.of("GBP", "0");
                const money: Money = original.toMoney();
                const restored = ParsedMoney.ofMoney(money);

                expect(restored.getCurrency()).toBe(original.getCurrency());
                expect(restored.getNumber()).toBe(original.getNumber());
            }
        );

        it(
            "round-trips large integer",
            () => {
                const original = ParsedMoney.of("JPY", "999999999999");
                const money: Money = original.toMoney();
                const restored = ParsedMoney.ofMoney(money);

                expect(restored.getCurrency()).toBe(original.getCurrency());
                expect(restored.getNumber()).toBe(original.getNumber());
            }
        );
    }
);

describe(
    "Money JSON serialization",
    () => {
        it(
            "serializes Money to JSON",
            () => {
                const money: Money = ParsedMoney.of("USD", "99.95").toMoney();
                const json = JSON.stringify(money);

                expect(json).toBe('{"money":"USD99.95"}');
            }
        );

        it(
            "deserializes Money from JSON",
            () => {
                const json = '{"money":"EUR1234.56"}';
                const money: Money = JSON.parse(json);

                expect(money.money).toBe("EUR1234.56");
            }
        );

        it(
            "round-trips through JSON",
            () => {
                const original: Money = ParsedMoney.of("GBP", "500.25").toMoney();
                const json = JSON.stringify(original);
                const restored: Money = JSON.parse(json);

                expect(restored.money).toBe(original.money);
            }
        );

        it(
            "round-trips through JSON and ParsedMoney",
            () => {
                const original = ParsedMoney.of("CHF", "250.75");
                const json = JSON.stringify(original.toMoney());
                const restored: Money = JSON.parse(json);
                const parsed = ParsedMoney.ofMoney(restored);

                expect(parsed.getCurrency()).toBe(original.getCurrency());
                expect(parsed.getNumber()).toBe(original.getNumber());
            }
        );

        it(
            "round-trips negative amount through JSON",
            () => {
                const original: Money = ParsedMoney.of("EUR", "-42.50").toMoney();
                const json = JSON.stringify(original);
                const restored: Money = JSON.parse(json);

                expect(restored.money).toBe(original.money);
            }
        );

        it(
            "round-trips zero through JSON",
            () => {
                const original: Money = ParsedMoney.of("USD", "0").toMoney();
                const json = JSON.stringify(original);
                const restored: Money = JSON.parse(json);

                expect(restored.money).toBe(original.money);
            }
        );
    }
);

describe(
    "ParsedMoney validates number format",
    () => {
        it(
            "rejects empty string",
            () => {
                expect(() => ParsedMoney.of("USD", "")).toThrow("Number cannot be empty");
            }
        );

        it(
            "rejects non-numeric string",
            () => {
                expect(() => ParsedMoney.of("USD", "abc")).toThrow("Invalid number format");
            }
        );

        it(
            "rejects scientific notation",
            () => {
                expect(() => ParsedMoney.of("USD", "1e10")).toThrow("Invalid number format");
                expect(() => ParsedMoney.of("USD", "1E+5")).toThrow("Invalid number format");
                expect(() => ParsedMoney.of("USD", "1.5e2")).toThrow("Invalid number format");
            }
        );

        it(
            "rejects NaN",
            () => {
                expect(() => ParsedMoney.of("USD", "NaN")).toThrow("Invalid number format");
            }
        );

        it(
            "rejects Infinity",
            () => {
                expect(() => ParsedMoney.of("USD", "Infinity")).toThrow("Invalid number format");
                expect(() => ParsedMoney.of("USD", "-Infinity")).toThrow("Invalid number format");
            }
        );

        it(
            "rejects trailing non-numeric characters",
            () => {
                expect(() => ParsedMoney.of("USD", "123abc")).toThrow("Invalid number format");
            }
        );

        it(
            "rejects bare sign",
            () => {
                expect(() => ParsedMoney.of("USD", "+")).toThrow("Invalid number format");
                expect(() => ParsedMoney.of("USD", "-")).toThrow("Invalid number format");
            }
        );

        it(
            "rejects bare dot",
            () => {
                expect(() => ParsedMoney.of("USD", ".")).toThrow("Invalid number format");
            }
        );

        it(
            "accepts positive integer",
            () => {
                expect(ParsedMoney.of("USD", "123").getNumber()).toBe("123");
            }
        );

        it(
            "accepts negative integer",
            () => {
                expect(ParsedMoney.of("USD", "-123").getNumber()).toBe("-123");
            }
        );

        it(
            "accepts positive decimal",
            () => {
                expect(ParsedMoney.of("USD", "123.45").getNumber()).toBe("123.45");
            }
        );

        it(
            "accepts negative decimal",
            () => {
                expect(ParsedMoney.of("USD", "-123.45").getNumber()).toBe("-123.45");
            }
        );

        it(
            "accepts leading decimal point",
            () => {
                expect(ParsedMoney.of("USD", ".45").getNumber()).toBe(".45");
            }
        );

        it(
            "accepts trailing decimal point",
            () => {
                expect(ParsedMoney.of("USD", "123.").getNumber()).toBe("123.");
            }
        );

        it(
            "accepts explicit positive sign",
            () => {
                expect(ParsedMoney.of("USD", "+123").getNumber()).toBe("+123");
            }
        );

        it(
            "rejects Money with invalid number part",
            () => {
                const money: Money = {money: "USDabc"};

                expect(() => ParsedMoney.ofMoney(money)).toThrow("Invalid number format");
            }
        );

        it(
            "rejects string with invalid number part",
            () => {
                expect(() => ParsedMoney.ofString("USD1e5")).toThrow("Invalid number format");
            }
        );
    }
);


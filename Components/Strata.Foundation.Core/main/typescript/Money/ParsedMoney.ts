import {Money} from "./Money";

export
class ParsedMoney
{
    private readonly currency: string;
    private readonly amount: string;

    private static readonly ISO_CURRENCY_CODES: ReadonlySet<string> = new Set([
        "AED","AFN","ALL","AMD","ANG","AOA","ARS","AUD","AWG","AZN",
        "BAM","BBD","BDT","BGN","BHD","BIF","BMD","BND","BOB","BRL",
        "BSD","BTN","BWP","BYN","BZD","CAD","CDF","CHF","CLP","CNY",
        "COP","CRC","CUP","CVE","CZK","DJF","DKK","DOP","DZD","EGP",
        "ERN","ETB","EUR","FJD","FKP","GBP","GEL","GHS","GIP","GMD",
        "GNF","GTQ","GYD","HKD","HNL","HRK","HTG","HUF","IDR","ILS",
        "INR","IQD","IRR","ISK","JMD","JOD","JPY","KES","KGS","KHR",
        "KMF","KPW","KRW","KWD","KYD","KZT","LAK","LBP","LKR","LRD",
        "LSL","LYD","MAD","MDL","MGA","MKD","MMK","MNT","MOP","MRU",
        "MUR","MVR","MWK","MXN","MYR","MZN","NAD","NGN","NIO","NOK",
        "NPR","NZD","OMR","PAB","PEN","PGK","PHP","PKR","PLN","PYG",
        "QAR","RON","RSD","RUB","RWF","SAR","SBD","SCR","SDG","SEK",
        "SGD","SHP","SLE","SOS","SRD","SSP","STN","SVC","SYP","SZL",
        "THB","TJS","TMT","TND","TOP","TRY","TTD","TWD","TZS","UAH",
        "UGX","USD","UYU","UZS","VES","VND","VUV","WST","XAF","XCD",
        "XOF","XPF","YER","ZAR","ZMW","ZWL"
    ]);

    constructor(currency: string, amount: string)
    {
        this.currency = ParsedMoney.validateCurrency(currency);
        this.amount = ParsedMoney.validateNumber(amount);
    }

    public getCurrency(): string
    {
        return this.currency;
    }

    public getNumber(): string
    {
        return this.amount;
    }

    public toMoney(): Money
    {
        return {money: this.currency + this.amount.toString()};
    }

    public static of(currency: string, amount: string): ParsedMoney
    {
        return new ParsedMoney(currency, amount);
    }

    public static ofMoney(money: Money): ParsedMoney
    {
        return new ParsedMoney(
            money.money.substring(0,3),
            money.money.substring(3));
    }

    public static ofString(money: string): ParsedMoney
    {
        return new ParsedMoney(
            money.substring(0,3),
            money.substring(3));
    }

    private static validateCurrency(currency: string): string
    {
        if (!/^[A-Z]{3}$/.test(currency))
            throw new Error(`Invalid currency code format: ${currency}`);

        if (!ParsedMoney.ISO_CURRENCY_CODES.has(currency))
            throw new Error(`Unknown ISO 4217 currency code: ${currency}`);

        return currency;
    }

    private static validateNumber(amount: string): string
    {
        if (amount.length == 0)
            throw new Error("Number cannot be empty");

        if (!/^[+-]?(\d+\.?\d*|\.\d+)$/.test(amount))
            throw new Error(`Invalid number format: ${amount}`);

        return amount;
    }
}
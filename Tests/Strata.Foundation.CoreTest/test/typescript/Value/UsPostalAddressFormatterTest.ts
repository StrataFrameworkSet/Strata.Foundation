import {UsPostalAddressFormatter} from "strata.foundation.core/Value/UsPostalAddressFormatter";
import {PostalAddressBuilder} from "strata.foundation.core/Value/PostalAddressBuilder";

describe(
    "UsPostalAddressFormatter",
    () => {
        it(
            "formats postal addresses with addressee",
            () => {
                const addressee = "Mr. John Doe";
                const formatter = new UsPostalAddressFormatter(addressee);
                const address = new PostalAddressBuilder()
                    .setStreet("123 Main St")
                    .setCity("Springfield")
                    .setState("IL")
                    .setPostalCode("62701")
                    .build();

                const expected = "Mr. John Doe\n123 Main St\nSpringfield, IL 62701";
                expect(formatter.format(address)).toBe(expected);
            }
        );

        it(
            "formats postal addresses without addressee",
            () => {
                const formatter = new UsPostalAddressFormatter();
                const address = new PostalAddressBuilder()
                    .setStreet("123 Main St")
                    .setCity("Springfield")
                    .setState("IL")
                    .setPostalCode("62701")
                    .build();

                const expected = "123 Main St\nSpringfield, IL 62701";
                expect(formatter.format(address)).toBe(expected);
            }
        );

        it(
            "changes addressee via setter",
            () => {
                const oldAddressee = "Mr. John Doe";
                const newAddressee = "Dr. Jane Smith";
                const formatter = new UsPostalAddressFormatter(oldAddressee);
                const address = new PostalAddressBuilder()
                    .setStreet("123 Main St")
                    .setCity("Springfield")
                    .setState("IL")
                    .setPostalCode("62701")
                    .build();

                formatter.setAddressee(newAddressee);
                const expected = "Dr. Jane Smith\n123 Main St\nSpringfield, IL 62701";
                expect(formatter.format(address)).toBe(expected);
            }
        );

        it(
            "handles addresses with both address and street components",
            () => {
                const addressee = "Mrs. Susan Taylor";
                const formatter = new UsPostalAddressFormatter(addressee);
                const address = new PostalAddressBuilder()
                    .setAddress("Apt 4B")
                    .setStreet("350 Fifth Avenue")
                    .setCity("New York")
                    .setState("NY")
                    .setPostalCode("10118")
                    .build();

                const expected = "Mrs. Susan Taylor\nApt 4B 350 Fifth Avenue\nNew York, NY 10118";
                expect(formatter.format(address)).toBe(expected);
            }
        );

        it(
            "handles addresses with only address component",
            () => {
                const addressee = "Rodriguez Family";
                const formatter = new UsPostalAddressFormatter(addressee);
                const address = new PostalAddressBuilder()
                    .setAddress("1600 Amphitheatre Parkway")
                    .setCity("Mountain View")
                    .setState("CA")
                    .setPostalCode("94043")
                    .build();

                const expected = "Rodriguez Family\n1600 Amphitheatre Parkway\nMountain View, CA 94043";
                expect(formatter.format(address)).toBe(expected);
            }
        );

        it(
            "handles addresses with only street component",
            () => {
                const addressee = "Ms. Emily Chen";
                const formatter = new UsPostalAddressFormatter(addressee);
                const address = new PostalAddressBuilder()
                    .setStreet("233 S Wacker Dr")
                    .setCity("Chicago")
                    .setState("IL")
                    .setPostalCode("60606")
                    .build();

                const expected = "Ms. Emily Chen\n233 S Wacker Dr\nChicago, IL 60606";
                expect(formatter.format(address)).toBe(expected);
            }
        );
    }
);
import {BasicPersonNameFormatter} from "strata.foundation.core/Value/BasicPersonNameFormatter";
import {PersonName} from "strata.foundation.core/Value/PersonName";
import {PersonNameBuilder} from "strata.foundation.core/Value/PersonNameBuilder";

describe(
    "BasicPersonNameFormatter",
    () => {
        it(
            "formats name with all components",
            () => {
                const formatter: BasicPersonNameFormatter = new BasicPersonNameFormatter();
                formatter.setNameSuffix("Jr.");
                const name: PersonName = new PersonNameBuilder()
                    .setTitle("Dr.")
                    .setFirstName("John")
                    .setMiddleName("William")
                    .setLastName("Doe")
                    .build();

                const expected: string = "Dr. John William Doe, Jr.";
                expect(formatter.format(name)).toBe(expected);
            }
        );

        it(
            "formats name with title only",
            () => {
                const formatter: BasicPersonNameFormatter = new BasicPersonNameFormatter();
                const name: PersonName = new PersonNameBuilder()
                    .setTitle("Dr.")
                    .setFirstName("John")
                    .setLastName("Doe")
                    .build();

                const expected: string = "Dr. John Doe";
                expect(formatter.format(name)).toBe(expected);
            }
        );

        it(
            "formats name with suffix only",
            () => {
                const formatter: BasicPersonNameFormatter = new BasicPersonNameFormatter();
                formatter.setNameSuffix("Jr.");
                const name: PersonName = new PersonNameBuilder()
                    .setFirstName("John")
                    .setLastName("Doe")
                    .build();

                const expected: string = "John Doe, Jr.";
                expect(formatter.format(name)).toBe(expected);
            }
        );

        it(
            "formats name with middle name only",
            () => {
                const formatter: BasicPersonNameFormatter = new BasicPersonNameFormatter();
                const name: PersonName = new PersonNameBuilder()
                    .setFirstName("John")
                    .setMiddleName("William")
                    .setLastName("Doe")
                    .build();

                const expected: string = "John William Doe";
                expect(formatter.format(name)).toBe(expected);
            }
        );

        it(
            "formats name with just first and last name",
            () => {
                const formatter: BasicPersonNameFormatter = new BasicPersonNameFormatter();
                const name: PersonName = new PersonNameBuilder()
                    .setFirstName("John")
                    .setLastName("Doe")
                    .build();

                const expected: string = "John Doe";
                expect(formatter.format(name)).toBe(expected);
            }
        );
    }
);
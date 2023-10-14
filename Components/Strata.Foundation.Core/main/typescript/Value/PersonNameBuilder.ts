import {PersonName} from "./PersonName";

export
class PersonNameBuilder
{
    private title?: string;
    private firstName: string;
    private middleName?: string;
    private lastName: string;

    constructor()
    {
        this.title = undefined;
        this.firstName = null;
        this.middleName = undefined;
        this.lastName = null;
    }

    setTitle(title: string): PersonNameBuilder
    {
        this.title = title;
        return this;
    }

    setFirstName(firstName: string): PersonNameBuilder
    {
        this.firstName = firstName;
        return this;
    }

    setMiddleName(middleName: string): PersonNameBuilder
    {
        this.middleName = middleName;
        return this;
    }

    setLastName(lastName: string): PersonNameBuilder
    {
        this.lastName = lastName;
        return this;
    }

    clear(): PersonNameBuilder
    {
        this.title = undefined;
        this.firstName = null;
        this.middleName = undefined;
        this.lastName = null;
        return this;
    }

    getTitle(): string|undefined
    {
        return this.title;
    }

    getFirstName(): string
    {
        return this.firstName;
    }

    getMiddleName(): string|undefined
    {
        return this.middleName;
    }

    getLastName(): string
    {
        return this.lastName;
    }

    hasTitle(): boolean
    {
        return this.title != undefined;
    }

    hasFirstName(): boolean
    {
        return this.firstName != null;
    }

    hasMiddleName(): boolean
    {
        return this.middleName != undefined;
    }

    hasLastName(): boolean
    {
        return this.lastName != null;
    }

    build(): PersonName
    {
        return {
            title: this.title,
            firstName: this.firstName,
            middleName: this.middleName,
            lastName: this.lastName
        };
    }
}
import React from "react";
import {render, screen} from "@testing-library/react";

import Carpet from "./Carpet";


describe("the Carpet", () => {
    const carpet = {colour: "pink", size: 10, orderNumber: 5};

    test("displays a colour", async () => {
        render(<Carpet carpet={carpet}/>);
        const colour = await screen.findByText(carpet.colour, {exact: false});
        expect(colour).toBeInTheDocument();
    });

    test("displays an order number", async () => {
        render(<Carpet carpet={carpet}/>);
        const colour = await screen.findByText(carpet.orderNumber, {exact: false});
        expect(colour).toBeInTheDocument();
    });
});

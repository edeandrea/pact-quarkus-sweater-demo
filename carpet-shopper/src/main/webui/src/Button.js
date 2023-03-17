import {useState} from "react";
import axios from "axios";
import Carpet from "./Carpet";
import styled from "styled-components"

const CarpetOrderer = styled.div`
  padding: 1rem;
  display: flex;
  flex-direction: column;
`

const PushButton = styled.button`
  margin-top: 10px;
`

const Prompt = styled.div`
  margin-bottom: 10px;
`

const Button = () => {
    const [carpets, setCarpets] = useState([]);
    const [colour, setColour] = useState("");
    const [orderNumber, setOrderNumber] = useState(1);

    const handleSubmit = async () => {
        const newOrderNumber = orderNumber + 1
        setOrderNumber(newOrderNumber)
        try {
            const res = await axios.post(
                "http://localhost:8080/bff/order",
                {
                    colour: colour, orderNumber
                },
                {
                    headers: {
                        "Content-Type": "application/json",
                        "X-carpet-correlation-id": orderNumber
                    },
                }
            );
            setCarpets((carpets) => [res.data, ...carpets]);
            setColour("");
        } catch (err) {
            console.error(err.message);
        }
    };

    const handleKeyPress = (e) => {
      if (e.which === 13) {
          e.preventDefault();
          handleSubmit();
      }
    };

    const handleColourChange = (event) => {
        setColour(event.target.value);
    };

    return (
        <>
            <CarpetOrderer>
                <Prompt>What colour carpet would you like?</Prompt>
                <input
                    type="text"
                    className="form-control"
                    onChange={handleColourChange}
                    value={colour}
                    onKeyUp={handleKeyPress}
                />
                <PushButton type="submit" onClick={handleSubmit}>
                    Order
                </PushButton>
            </CarpetOrderer>
            <div className="carpets-container">
                {carpets.map((carpet) => {
                    return (
                        <Carpet key={carpet.orderNumber} carpet={carpet}/>
                    );
                })}
            </div>
        </>
    );
};

export default Button;

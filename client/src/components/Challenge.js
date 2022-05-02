
import React from "react";
import { Component } from "react";

class Candy {
  constructor(ID, name, stock, capacity, orderAmount){
    this.ID = ID;
    this.name = name;
    this.stock = stock;
    this.capacity = capacity;
    this.orderAmount = orderAmount;
  }
}

class Challenge extends Component{
  constructor(props){
    super(props);
    this.state = {
      items: [],
      totalCost: 0.00
    }
  }

  getLowStock = () => {
    fetch("http://localhost:4567/low-stock", {
      method: "GET",
      headers: {"content-type": "application/json"}
    })
    .then(response => response.json())
    .then(lowItems => {
      this.setState({
        items: []
      });
      let nextState = this.state;
      for (let i = 0; i < lowItems.length; i++){
        const candy = new Candy(lowItems[i].ID, lowItems[i].name, lowItems[i].stock, lowItems[i].capacity, lowItems[i].orderAmount)
        nextState.items.push(candy);
      }
      this.setState(nextState);
    });
  }

  getTotalCost = () => {
    fetch("http://localhost:4567/restock-cost", {
      method: "POST",
      headers: {"content-type": "applicaitons"},
      body: JSON.stringify(this.state.items)
    })
    .then(response => response.json())
    .then(tCost => {
      this.setState({
        totalCost: tCost
      })
    });

  }

  updateCandy = (event,i) => {
    //sets orderAmount for candy at the given row
    this.state.items.at(i).orderAmount = event.target.value;
  }


  render() {
    return (
      <>
      <table>
        <thead>
          <tr>
            <td>SKU</td>
            <td>Item Name</td>
            <td>Amount in Stock</td>
            <td>Capacity</td>
            <td>Order Amount</td>
          </tr>
        </thead>
        <tbody>
          {this.state.items.map((candy,i) =>
          <tr>
            <td>{candy.ID}</td>
            <td>{candy.name}</td>
            <td>{candy.stock}</td>
            <td>{candy.capacity}</td>
            <td><input onChange={(event) => this.updateCandy(event,i)}></input></td>
          </tr>
          )}
        </tbody>
      </table>
      {/* TODO: Display total cost returned from the server */}
      <div>Total Cost: $ {this.state.totalCost}</div>
      {/* 
      TODO: Add event handlers to these buttons that use the Java API to perform their relative actions.
      */}
      <button onClick={this.getLowStock}>Get Low-Stock Items</button>
      <button onClick={this.getTotalCost}>Determine Re-Order Cost</button>
    </>

    );
  }
}

export default Challenge;



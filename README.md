# pact-quarkus-wookie-carpet-demo
A sample application for illustrating pact contract testing ideas.

This application has an observability mechanism baked into it. 
The `architecture-recorder` service persists and serves information about the architectural components and runtime interactions. 
The `observer-extension` intercepts interactions, using a Jakarta `Interceptor`, and sends them to the architecture recorder. The observer extension also introduces a delay into each interaction. This would be bad in a real system, but is useful for the visualisation of the system flow.

[//]: # (![the flow of the application]&#40;images/app-flow.png&#41;)

Note that only the [`wookie-tamer`](wookie-tamer) and [`weaver`](weaver) projects have contract tests. 

See [the demo instructions](./demo-script.md) for a detailed walkthrough.

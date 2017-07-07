# GOOS


## TDD
- judging **where** to set the boundaries of **what** to test.
- **how** to eventually cover everything. 
- uses Acceptance tests to show incremental progress.
- each acceptance test should have just enough new requirements to force a manageable increase in functionality.
- write a high-level end-to-end test to describe **what** the system should implements.
- write long unit test names to tell us **what** a class does.
- extract new classes to **tease apart** fine-grained aspects of the functionality.
- write lots of little methods to keep each layer of code at a consistent level of **abstraction**.
- write a rough implementation to **prove** that we know **how** to make the code do **what**'s required and then we refactor.


## Incremental Development

- how to slice up the functionality so that it can be built a little at a time?
  - each slice should be **significant** and **concrete** enough so that the team can tell **when** it's done.
  - each slice should be **small** enough to be focused on one concept and achievable quickly.

### Benefit

- dividing the work into **small**, **coherent** chunks helps us manage the development risk.
- we get regular, concrete **feedback** on the progress we'are making, so we can adjust the plan as the team discovers more about the domain and the technologies.


## Walking Skeleton 

developing the walking skeleton takes a surprising amount of effort.
- deciding **what** to do will flush out all sorts of questions about the application and its place in the world.
- the **automation** of building, packaging, and deploying into a production-like environment will flush out all sorts of technical and organizational questions.

### Benefit
- help us understand the requirements well enough to **propose** and **validate** a broad-brush system structure.
- it's very important to be able to **assess** the approach we've chosen and to **test** our decisions.


### Iteration Zero

doing initial analysis, **setting up** its physical and technical environments, and otherwise getting started. 
it isn't adding much visible functionality since almost all the work is infrastructure.
One important task for iteration zero is to use the walking skeleton to **test-drive** the initial architecture.

Iteration zero usually brings up project chartering issues as the team looks for criteria to guide its decisions,
so the project's sponsors should expect to field some deep questions about its purpose.


### Programming by Intention

working backwards from the test helps us focus on **what** we want the system to do,
instead of getting caught up in the complexity of **how** we will make it work.


### One Domain at a Time

keeping the language **consistent** helps us understand what's **significant** in this test,
with a nice side effect of protecting us when the implementation **inevitably** changes.


## A Minimal Fake Implementation 

the fake is a minimal implementation just to support testing, so it can be as simple as possible.
e.g: use a single instance variable `currentChat` to hold the chat object.

## How to pass the failing test?

- adding functionality, **a tiny slice** at a time, until eventually make the test pass.
  following this approach will makes our progress much more *predicable*.
  focusing on just **one aspect** at a time helps us to make sure we understand it.
  
- writing a small amount of **ugly** code and seeing **how** it falls out.
  it helps us to **test** our ideas before we've gone too bar, and sometimes the results can be surprising.

## The Necessary Minimum 

The point is to **design** and **validate** the initial structure of the end-to-end system --
where end-to-end includes deployment to a working environment -- 
to **prove** that our choices of packages, libraries, and tooling will actually work.
A sense of **urgency** will help the team to **strip** the functionality down to the absolute **minimum sufficient** to test their assumptions. 


## Outside-In Development

- Test-Driven Development is to start with the outside event that triggers the behavior we want to implement and work our way into the code an object at a time, until we reach a visible effect indicating that we've achieved our goal.

- Entry point is the event that triggers the next round of behavior we want to make work.



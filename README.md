# GOOS

## TDD
- judging where to set the boundaries of what to test
- how to eventually cover everything. 

## Incremental Development

- how to slice up the functionality so that it can be built a little at a time?
  - each slice should be significant and concrete enough so that the team can tell when it's done.
  - each slice should be small enough to be focused on one concept and achievable quickly.

### Benefit

- dividing the work into small, conherent chunks helps us manage the development risk.
- we get regular, concrete feedback on the progress we'are making, so we can adjust your plan as the team discovers more about the domain and the technologies.


## Walking Skeleton 
developing the walking skeleton takes a suprising amount of effort.
- decising what to do will flush out all sorts of questions about the application and its place in the world.
- the automation of building, packaging, and deploying into a production-like environment will flush out all sorts of technical and organizational questions.

### Benefit
- help us understand the requirements well enough to propose and validate a broad-brush system structure.
- it's very important to be able to assess the approach we've chosen and to test our decisions.


### Iteration Zero

doing initial analysis, setting up its physical and technical environments, and otherwise getting started. it isn't adding much visible functionality since almost all the work is infrastructure. One important task for iteration zero is to se the walking skeleton to test-drive the initial architecture.

### Programming by Intention

working backwards from the test helps us focus on **what** we want the system to do, instead of getting caught up in the complexity of **how** we will make it work.

### One Domain at a Time
keeping the language consistent helps us understand what's significant in this test, with a nice side effect of protecting us when the implementation inevitably changes.

# Multiblock Die Calculator

## What is it?

I work in the steel industry as the operator of a multiblock wiredraw machine. Part of operating the machine is setting up the dies required to progressively draw the material down to a smaller diameter. With a single-block machine it is fairly easy to know what die(s) to use, as there is only one bullblock operating on the material, you can practically use any combination to achieve the final result. When it comes to multiblock operation, the reductions between bullblocks needs to be calculated such that each head can maintain optimal rotational speed to maintain a line speed that is consistent.

## Why?

Calculating these reductions by hand is a very long and complex process, with several calculations that need to go together in the right order to build the correct die setup. Add to that, and no offense to my fellow co-workers, the employees on a wiremill floor generally aren't capable of making these calculations, at least not in the sort of time frame that financially makes sense, and rely on either software or higher-ups to create and organize these setups.

## Operation

### For the machine operator:

This software could not be easier to use. Assuming your management/QA/IT team(s) have correctly configured it, you should simply be able to select your machine, enter your start and finish sizes, and have a setup that will be optimally designed for the chosen equipment. You can find less significant options, like the use of pressure dies or display of elongation, in the menus at the top of the window. If you need further assistance, your supervisor should be able to help.

### For management:

For your operators to have the easiest time using the software, it is **highly** recommended that you make a copy of the `example.xml` file and rename it to `machines.xml`, configuring the settings within for the equipment your employees will be using. The structure of the file is fairly straightforward, should be easy to follow, and can be extended to a virtually infinite number of drwaing machines. Further documentation regarding the construction of this file will come in the future.


## Project Status

From the first few commits this project has come quite a ways. We started with a very procedurally-oriented CLI program which slowly branched out into proper object-oriented philosophies, adding a user-friendly GUI and paving the way to becoming a better programmer. This has become a small labour of love designed to make my day-to-day life a little easier (isn't that the goal of all software?), if you would like to implement this in your own facility and need the help doing so, please feel free to reach out. I feel like it is at a point where it could be deployed to a shop floor, assuming that regular updates are performed to ensure you have the latest features and bug-fixes.
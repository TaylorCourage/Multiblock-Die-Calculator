# Multiblock Die Calculator

## What is it?

I work in the steel industry as the operator of a multiblock wiredraw machine. Part of operating the machine is setting up the dies required to progressively draw the material down to a smaller diameter. With a single-block machine it is fairly easy to know what die(s) to use, as there is only one bullblock operating on the material, you can practically use any combination to achieve the final result. When it comes to multiblock operation, the reductions between bullblocks needs to be calculated such that each head can maintain optimal rotational speed to maintain a line speed that is consistent.

## Why?

Calculating these reductions by hand is neigh-impossible without spending more time than it is worth. Add to that, and no offense to my fellow co-workers, the employees on a wiremill floor generally don't have the capability to make these calculations, and must rely on either software, or their supervisors to create and organize these setups. At our facility for example, there was only one management employee capable of creating these setups in a reasonable time frame, but problems would arise when this employee had vacation time and bespoke orders would come in from customers. This often left us scrambling to figure out what dies we can use, as you often cannot just toss in whatever dies 'feel' right without over-stressing the machine components.

## How does it work?

So far this software is incredibly rudimentary, but functional where necessary. *Many* changes will come in the future to aid in operation. I would not recommend putting this software into service for quite some time, at least not until a user-friendly GUI is introduced.

Machine parameters are stored in a `.xml` file, an example of which can be found in the root directory of this repository. You **must** rename this file to `machines.xml` before first-run, preferably after adding the parameters of your own equipment.

When running the software you will be asked for the ID of the machine you are using. This ID corresponds to the ID field in the `.xml` file. You will then be asked for an initial material diameter, then a final diameter. If the machine parameters were correctly set, it should generate a die setup that falls within those.

## Project Status

It's pretty rough, I'm well aware. I need a lot more comments across all the code, for example. Things need to be cleaned up as well, especially following more object-oriented principles. There is no GUI yet, but one is planned. It doesn't handle tapers yet, and error handling for machines that don't exist isn't there yet. This might be a long-time labour of love considering it *works* for my purposes right now.
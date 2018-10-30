# What is MASS? #

(Multi-Agent Spatial Simulation)

For more than the last two decades, multi-agent simulations have been highlighted to model mega-scale social or biological agents and to simulate their emergent collective behavior that may be difficult only with mathematical and macroscopic approaches. A successful key for simulating megascale agents is to speed up the execution with parallelization. Although many parallelization attempts have been made to multiagent simulations, most work has been done on shared-memory programming environments such as OpenMP, CUDA, and Global Array, or still has left several programming problems specific to distributed-memory systems, such as machine unawareness, ghost space management, and cross-processor agent management (including migration, propagation, and termination). To address these parallelization challenges, we have been developing MASS, a new parallel-computing library for multi-agent and spatial simulation over a cluster of computing nodes. MASS composes a user application of distributed arrays and multi-agents, each representing an individual simulation place or an active entity. All computation is enclosed in each array element or agent; all communication is scheduled as periodic data exchanges among those entities, using machine-independent identifiers; and agents migrate to a remote array element for rendezvousing with each other. Our unique agent-based approach takes advantage of these merits for parallelizing big data analysis using climate change and biological network motif searches as well as individual-based simulation such as neural network simulation and influenza epidemic simulation as practical application examples.

More information about MASS can be found out the University of Washington Distributed Systems Lab [Homepage](http://depts.washington.edu/dslab/MASS).

# What is ProvMASS #

The MASS software environment coordinates multi-agents in a distributed memory formed within a cluster of computing nodes. The details of parallelization and data locale are abstracted away from the simulation, removing the coordination burden from the developer. A caveat of such machine/thread-unawareness is that it introduces difficulty in determining the cause of logical errors and understanding the impact of newly added agent behaviors. The user is left to guess at the connections between input, output, and static source code. MASS logging mechanisms allow the addition of custom message generation to model source code. While this certainly helps with the guesswork, it falls short in determining the order of events in a distributed environment. To this end, we have created ProvMASS, an approach to capture causally ordered data operations in MASS distributed memory (i.e. concurrent operations over distributed shared resources). Such an approach accommodates the verification and validation requirements in inspecting multi-agent models in execution.

# Provenance Capture Instrumentation #

Provenance capture takes place through procedural instrumentation within the MASS framework and any multi-agent / spatial model classes.

# Project Contents and Deployment #

The ProvMASS project is 


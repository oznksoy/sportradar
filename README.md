# Sportradar Coding Exercise
## Scoreboard Implementation:

Aim of the module is to create a scoreboard module that can record and display:
- Start of the match between two teams as home team and away team.
- Recording updates to the scoreboard
- Finalizing matches, thus removing the finished matches from the scoreboard.
- Returning a summary of the currently running matches from the scoreboard records.

## Developer Notes:

### Tests:

- Current code coverage is %88. This is after targeting to cover the main behavioral code artifacts, thus this seems to be the functionally liable threshold for the current structure of the module. Further extension to the data entity classes are not implemented as it will create redundancy and further test maintenance requirements in the future.
- Test are written in consideration to Behavioral Driven Development (BDD) standards. Although an additional framework has not been used, the test method names and test scenarios are there to mimic actual use cases.
- Test scenarios are explained mainly on the test method signature. They follow BDD keywords and thus self-explanatory.

### Development Considerations:

- Scoreboard module was first designed to use a shared memory management as a singleton cache memory. Later on, single this is a library module, it was decided that the best implementation would be to have independent instances with unlinked memory spaces, since there can be many scoreboards running at the same time, thus they would need separates memories.
- Scoreboard module only exposes necessary parts of the module to ensure functional use and resisting software decay that can happen in time after the library module release.
- Main use of the scoreboard is to call the static builder on the factory class. This factory can be enhanced to support more complex initiations in the future, or it can be replaced with a builder.

#### Isolated and Segregated logical elements:

- To enable separation of concerns and atomize the method structures, main Scoreboard behavior is managed by four aspects of implementation.

<ol>
<li>Scoreboard Implementation: Main Scoreboard manager class which orchestrates all other responsible aspects and binds them to a final behavior.</li>
<li>Scoreboard Audit Class: Responsible for maintaining exceptions and data and behavior state controls for the scoreboard module</li>
<li>Scoreboard Cache: Responsible for the memory management.</li>
<li>Scoreboard Clock: Responsible for stubbing time related functions.</li>
</ol>

- All these classes are package private to ensure proper separation from the rest of the library modules and to signal the developer what to use intuitively.
- All public sources are final to stop inheritance and coupling with user modules in the scoreboard library module.

##### Atomization of data entities and data mapping:

- Data entities are structured to atomize and capture the current state of the incoming match information. When called for the summary, these data entities are mapped into a *Match* entity.
- Data entities are signed as serialized for further transfer and storage capabilities.
- Atomized data entities follow a key set map value structure:

<ol>
<li>MatchTeamPair : Two teams that define the match</li>
<li>MatchDetails : Match details which includes start time of the match, and score points</li>
<li>MatchScore : Hold home and away score data and directly used by match details</li>
</ol>

- This Structure enables the following:

<ol>
<li> It is possible to extend the data structure without minimal alterations to the overlaying structure.</li>
<li> It makes it mappable and manageable withing a standard key and value map data structure. </li>
</ol>

### Logical Consistency and Validations:
- There are two main aspects that are checked to ensure valid state of the module.
<ol>
<li>Validation of the inputs in structural aspects, such as null, empty or blank.</li>
<li>Validation of the inputs in logical aspects.</li>
</ol>

#### Logical aspects are:
- Requesting to insert a lower score than before.
- Trying start a match that has already started.
- Trying to update a match that has not started yet.
- Trying to enter the same state of a match once again redundantly.

### Naming Conventions:
Names are selected to be descriptive and without shortening. Class names starts with arch grouping names, such as Scoreboard and Match. Method names are to the point yet descriptive. These are done so that both dependent modules for the library module can understand the flows intuitively and it would also be easier to maintain and extend the development of the module in the future.

### Method Sizes and Behavioral Complexity:
Methods are designed to closely follow the SOLID guidelines. The separation of the implementing structures are also follows the same principles.

### Build and Deployment Considerations:
Project is using gradle 8.6 with a kotlin based script definition and also includes wrapper for convenience. The wrapper can be removed while further deployment to ensure light-weight aspect of the module.

### Further Development:
It is possible to enlist a more functional perspective on data mapping methods. This can be achieved using functional interfaces and mapper classes that are separate from default scoreboard implementation class. However for now, since the input structure of the project does not consist of large data streams and events, this approach was not deemed necessary, and it was avoided to keep a more simple outline for the module structure.


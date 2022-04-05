# Interview assessment

This project represents a hypothetical service to manage parties for a game.

The requirements for a party service are: 
- Each party can have from 1 to 5 players
- The role integrity for a party is defined as: 1 tank, 1 healer, 3 dps
- Removing the last member of a party should disband the party itself
- Each player must include their name and age

The assessment goals are:
- Review the code for the api, There's multiple issues and missing parts. Write a document with suggestions, required fixes and possible improvements.
- Complete the test suit to ensure the functionality
- Choose one: 
  - fix what's not working in the api and make all tests pass
  - leave tests not passing that expose the flaws in the service

It should take 1 to 3 hours to complete. There are a few TODOs in the app with hints.

For simplicity all the storage is done in memory and the data is initialized as part of the [config](src/main/java/com/interview/assessment/config/AssessmentConfiguration.java)
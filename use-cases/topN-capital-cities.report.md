# USE CASE: Produce a Report on Top N Populated Capital Cities

## CHARACTERISTIC INFORMATION

### Goal in Context

As a user, I want to produce reports on the top N populated capital cities in the world, continents, and regions, so that organizational resources can be allocated efficiently based on up-to-date demographic trends.

### Scope

System (black-box)

### Level

Primary task.

### Preconditions

The database is current with global capital city population data.

### Success End Condition

A user-specific report on the top N populated capital cities is created.

### Failed End Condition

No report is produced.

### Primary Actor

User.

### Trigger

The user requests top N populated capital city data for a specified global, continental, or regional scope.

## MAIN SUCCESS SCENARIO

1. The user defines N and selects the geographical scope (world, continent, region).
2. The query is executed against the database.
3. A report is provided to the user, showing the top N populated capital cities in the selected scope, organized from largest to smallest by population.

## EXTENSIONS

None.

## SUB-VARIATIONS

-   Top N populated capital cities in the world
-   Top N populated capital cities in a continent
-   Top N populated capital cities in a region

## SCHEDULE

**DUE DATE**: Release 0.1.2

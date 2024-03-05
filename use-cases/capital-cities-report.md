# USE CASE: Produce a Report on Capital Cities

## CHARACTERISTIC INFORMATION

### Goal in Context

As a user, I want to produce reports on capital cities globally, and across continents and regions, organized by largest population to smallest, so that I can support decision-making with up-to-date demographic information on national capitals.

### Scope

System (black-box)

### Level

Primary task.

### Preconditions

Database contains accurate and current population data for capital cities.

### Success End Condition

A report listing capital cities as per the specified scope is available to the user.

### Failed End Condition

No report is produced.

### Primary Actor

User.

### Trigger

AA request for capital city population data by world, continent, or region is made to the database.

## MAIN SUCCESS SCENARIO

1. User specifies the desired scope for the report (world, continent, region).
2. User executes the query against the database.
3. User obtains a printed report of capital cities within the chosen scope, organized by population size from largest to smallest.

## EXTENSIONS

None.

## SUB-VARIATIONS

-   All capital cities in the world
-   All capital cities in the continent
-   All capital cities in the region

## SCHEDULE

**DUE DATE**: Release 0.1.2

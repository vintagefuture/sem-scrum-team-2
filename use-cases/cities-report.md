# USE CASE: Produce a Report on Cities in the world, continent, region, country, and district

## CHARACTERISTIC INFORMATION

### Goal in Context

As a user, I want to produce reports on cities across the world, within continents, regions, countries, and districts, organized by largest population to smallest, so that I can facilitate easy access to detailed urban population information for the organization.

### Scope

System (black-box)

### Level

Primary task.

### Preconditions

Database containing world population data.

### Success End Condition

A report on city populations is made available to the user for further analysis and processing.

### Failed End Condition

No report on city populations is produced.

### Primary Actor

User.

### Trigger

A request for city population data by world, continent, region, country, or district is sent to the database.

## MAIN SUCCESS SCENARIO

1. User selects the geographic scope of the report (world, continent, region, country, district).
2. User executes the query against the database.
3. User receives a printed report of city populations within the chosen scope, organized from largest to smallest.

## EXTENSIONS

None.

## SUB-VARIATIONS

-   World cities population
-   Continent cities population
-   Region cities population
-   Country cities population
-   District cities population

## SCHEDULE

**DUE DATE**: Release 0.1.2

# USE CASE: Produce a Report on Top N Populated Cities

## CHARACTERISTIC INFORMATION

### Goal in Context

As a user, I want to produce reports on the top N populated cities globally, in continents, regions, countries, and districts, so that I can provide quick access to information on the most populous urban areas for organizational planning and analysis.

### Scope

System (black-box)

### Level

Primary task.

### Preconditions

Database is up-to-date with global city population data.

### Success End Condition

A report on the top N populated cities as specified by the user is produced.

### Failed End Condition

No report is produced.

### Primary Actor

User.

### Trigger

The user requests data for the top N populated cities in a specified geographical area.

## MAIN SUCCESS SCENARIO

1. The user specifies N and selects the geographical scope for the report (world, continent, region, country, district).
2. The user runs the query against the database.
3. The user receives a report detailing the top N populated cities in the specified scope, organized by population size from largest to smallest.

## EXTENSIONS

None.

## SUB-VARIATIONS

-   Top N populated cities in the world
-   Top N populated cities in a continent
-   Top N populated cities in a region
-   Top N populated cities in a country
-   Top N populated cities in a district

## SCHEDULE

**DUE DATE**: Release 0.1.6

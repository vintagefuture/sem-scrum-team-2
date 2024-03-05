# USE CASE: Produce a Report on Countries in world, continent, region

## CHARACTERISTIC INFORMATION

### Goal in Context

As a *user* I want to *produce reports on world, continent and region population, organised by largest population to smallest*, 
so that *I can allow the organisation easy access to this population information.*

### Scope

System (black-box)

### Level

Primary task.

### Preconditions

Database containing world population data.

### Success End Condition

A report is available to the user for further processing.

### Failed End Condition

No report is produced.

### Primary Actor

User.

### Trigger

A request for either world, continent or region population data is sent to the database.

## MAIN SUCCESS SCENARIO

1. User identifies the resolution desired (world, continent, region).
2. User runs the desired query against the database.
3. User obtains a printed report of the desired data, organised by largest population to smallest. 

## EXTENSIONS

None.

## SUB-VARIATIONS

- World population
- Continent population
- Region population

## SCHEDULE

**DUE DATE**: Release 0.1.2

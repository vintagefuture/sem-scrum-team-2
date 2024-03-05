# USE CASE: Produce a Report on Top N Populated Countries

## CHARACTERISTIC INFORMATION

### Goal in Context

As a user, I want to produce reports on the top N populated countries globally, within continents, and within regions, so that I can facilitate access to critical population data for strategic planning and analysis within the organization.

### Scope

System (black-box)

### Level

Primary task.

### Preconditions

The database is updated and contains comprehensive population data for countries worldwide.

### Success End Condition

A report listing the top N populated countries according to the specified criteria is generated for the user.

### Failed End Condition

The system is unable to generate the requested report on the top N populated countries.

### Primary Actor

User.

### Trigger

The user defines a specific number (N) and geographic criteria (world, continent, region) and requests population data from the database.

## MAIN SUCCESS SCENARIO

1. The user specifies the number (N) and selects the geographic scope of interest (world, continent, region) for the report.
2. The user's request initiates the query process to retrieve the top N populated countries based on the specified criteria.
3. The system accesses the necessary data from the database to compile the report.
4. A report is generated, providing detailed information on the top N populated countries within the selected scope, organized by population size from largest to smallest.

## EXTENSIONS

None.

## SUB-VARIATIONS

-   Top N World Countries: A report on the top N populated countries globally.
- Top N Continent Countries: A report on the top N populated countries within a specified continent.
- Top N Region Countries: A report on the top N populated countries within a specified region.

## SCHEDULE

**DUE DATE**: Release 0.1.2
